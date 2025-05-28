import json
import torch
from transformers import AutoModelForCausalLM, AutoTokenizer


def calculate_perplexity(model, tokenizer, text, device='cuda'):
    """
    计算给定文本的困惑度。

    参数:
        model: 微调后的语言模型。
        tokenizer: 对应的分词器。
        text: 待评估的文本。
        device: 运行设备（默认为 'cuda'）。

    返回:
        困惑度值。
    """
    # 将文本编码为输入 ID
    inputs = tokenizer(text, return_tensors='pt', padding=True, truncation=True)
    input_ids = inputs['input_ids'].to(device)
    attention_mask = inputs['attention_mask'].to(device)

    # 将模型设置为评估模式
    model.eval()

    # 禁用梯度计算
    with torch.no_grad():
        # 计算每个 token 的对数似然
        outputs = model(input_ids=input_ids, attention_mask=attention_mask, labels=input_ids)
        loss = outputs.loss
        perplexity = torch.exp(loss).item()

    return perplexity


def load_eval_data(file_path):
    """
    从 eval.jsonl 文件中加载数据，并提取每个 JSON 对象中的 'output' 字段。

    参数:
        file_path: 文件路径。

    返回:
        包含所有 'output' 字段内容的列表。
    """
    outputs = []
    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            data = json.loads(line)
            if 'output' in data:
                outputs.append(data['output'])
    return outputs


# 加载微调后的模型和分词器
model_name = "/root/autodl-tmp/Models/deepseek-r1-7b-merged"  # 替换为你的模型路径
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForCausalLM.from_pretrained(model_name)

# 指定设备（如果有 GPU，使用 'cuda'；否则使用 'cpu'）
device = 'cuda' if torch.cuda.is_available() else 'cpu'
model.to(device)

# 加载评估数据
eval_file_path = "/root/autodl-tmp/LLaMA-Factory/data/adgen_eval.jsonl"  # 替换为你的文件路径
outputs = load_eval_data(eval_file_path)

# 计算每个输出的困惑度
results = []
for output in outputs:
    perplexity = calculate_perplexity(model, tokenizer, output, device=device)
    results.append({"output": output, "perplexity": perplexity})
    print(f"文本: {output}")
    print(f"困惑度: {perplexity}")

# 打印结果

# for result in results:
#     print(f"文本: {result['output']}")
#     print(f"困惑度: {result['perplexity']:.2f}")
#     print("-" * 50)

# 可选：将结果保存到文件
with open("perplexity_results.json", 'w', encoding='utf-8') as outfile:
    json.dump(results, outfile, ensure_ascii=False, indent=4)
