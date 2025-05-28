"""
@author: caoyanghalo@qq.com
@description: 封装为Web项目的文本校正工具，支持保留词处理
"""

from flask import Flask, request, jsonify
from pycorrector import Corrector

app = Flask(__name__)
corrector = Corrector()
reserved_words = ["林悦", "沈逸"]  # 用户定义的保留词列表


def replace_reserved_words(text, reserved_list):
    """
    将文本中的保留词替换为唯一占位符，避免纠错时修改
    :param text: 原始文本
    :param reserved_list: 保留词列表
    :return: 替换后的文本，排序后的保留词列表，占位符列表
    """
    # 按长度降序排列，避免短词先被替换
    sorted_reserved = sorted(reserved_list, key=lambda x: len(x), reverse=True)
    placeholders = []
    replaced_text = text
    for idx, word in enumerate(sorted_reserved):
        placeholder = f'__RESERVED_{idx}__'  # 生成唯一占位符
        replaced_text = replaced_text.replace(word, placeholder)
        placeholders.append(placeholder)
    return replaced_text, sorted_reserved, placeholders


def restore_reserved_words(text, sorted_reserved, placeholders):
    """
    将文本中的占位符恢复为原始保留词
    :param text: 包含占位符的文本
    :param sorted_reserved: 排序后的保留词列表
    :param placeholders: 占位符列表
    :return: 恢复后的文本
    """
    restored_text = text
    for placeholder, word in zip(placeholders, sorted_reserved):
        restored_text = restored_text.replace(placeholder, word)
    return restored_text


@app.route('/correct', methods=['POST'])
def correct_text():
    try:
        data = request.json
        original_text = data.get('text', '')

        # 替换保留词为占位符
        text_with_placeholder, sorted_words, placeholders = replace_reserved_words(original_text, reserved_words)

        # 执行纠错
        corrected_result = corrector.correct(text_with_placeholder)

        # 恢复保留词
        corrected_result['target'] = restore_reserved_words(corrected_result['target'], sorted_words, placeholders)
        corrected_result['source'] = restore_reserved_words(corrected_result['source'], sorted_words, placeholders)

        return jsonify({
            'source': original_text,
            'target': corrected_result['target'],
            'errors': corrected_result['errors']
        }), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/correct_batch', methods=['POST'])
def correct_batch_text():
    try:
        data = request.json
        sentences = data.get('sentences', [])

        # 替换保留词为占位符
        sentences_with_placeholders = []
        sorted_reserved_list = []
        placeholders_list = []
        for sentence in sentences:
            replaced_text, sorted_reserved, placeholders = replace_reserved_words(sentence, reserved_words)
            sentences_with_placeholders.append(replaced_text)
            sorted_reserved_list.append(sorted_reserved)
            placeholders_list.append(placeholders)

        # 批量校正文本
        batch_result_with_placeholders = corrector.correct_batch(sentences_with_placeholders)

        # 恢复保留词
        results = []
        for i, result in enumerate(batch_result_with_placeholders):
            source = restore_reserved_words(result['source'], sorted_reserved_list[i], placeholders_list[i])
            target = restore_reserved_words(result['target'], sorted_reserved_list[i], placeholders_list[i])
            errors = result['errors']
            results.append({
                'source': source,
                'target': target,
                'errors': errors
            })

        # 包装为一个对象
        response = {
            "results": results
        }
        return jsonify(response), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5010, debug=True)
