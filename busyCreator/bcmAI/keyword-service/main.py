# from flask import Flask, request, jsonify
# import torch
#
# app = Flask(__name__)
#
# # 加载微调后的模型
# model.load_state_dict(torch.load('bert_finetuned.pth'))
# model.eval()
#
# @app.route('/extract_keywords', methods=['POST'])
# def extract_keywords():
#     # 获取请求中的文本数据
#     data = request.get_json()
#     if not data or 'text' not in data:
#         return jsonify({'error': 'No text provided'}), 400
#
#     text = data['text']
#
#     # 预处理文本
#     preprocessed_text = preprocess_text(text)
#
#     # 编码文本
#     encoding = tokenizer.encode_plus(
#         preprocessed_text,
#         max_length=128,
#         padding='max_length',
#         truncation=True,
#         return_attention_mask=True,
#         return_tensors='pt'
#     )
#
#     # 提取关键词
#     with torch.no_grad():
#         outputs = model(encoding['input_ids'], encoding['attention_mask'])
#         logits = torch.sigmoid(outputs)
#         keywords = [word for word, score in zip(jieba.cut(preprocessed_text), logits[0]) if score > 0.5]
#
#     return jsonify({'keywords': keywords})
#
# if __name__ == '__main__':
#     app.run(debug=True, host='0.0.0.0', port=5002)