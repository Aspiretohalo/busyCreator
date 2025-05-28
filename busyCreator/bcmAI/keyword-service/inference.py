from transformers import BertTokenizer, BertForTokenClassification
from peft import PeftModel
from config import Config


def load_model():
    config = Config()

    # 加载基础模型
    base_model = BertForTokenClassification.from_pretrained(
        config.model_name,
        num_labels=config.num_labels
    )

    # 加载LoRA适配器
    model = PeftModel.from_pretrained(base_model, config.model_save_path)
    return model.to(config.device)


def predict(text, model, tokenizer):
    encoding = tokenizer(
        text,
        max_length=Config().max_length,
        padding="max_length",
        truncation=True,
        return_tensors="pt"
    ).to(Config().device)

    with torch.no_grad():
        outputs = model(**encoding)

    logits = outputs.logits.squeeze()
    predictions = torch.sigmoid(logits) > 0.5

    # 提取关键词
    tokens = tokenizer.convert_ids_to_tokens(encoding["input_ids"][0])
    keywords = []
    current_keyword = []

    for i, (token, pred) in enumerate(zip(tokens, predictions)):
        if pred and i != 0:  # 跳过[CLS]
            current_keyword.append(token)
        elif current_keyword:
            keyword = tokenizer.convert_tokens_to_string(current_keyword).strip()
            keywords.append(keyword)
            current_keyword = []

    return list(set(keywords))  # 去重


if __name__ == "__main__":
    model = load_model()
    tokenizer = BertTokenizer.from_pretrained(Config().model_name)

    test_text = "Natural language processing enables computers to understand human language."
    keywords = predict(test_text, model, tokenizer)
    print("Extracted Keywords:", keywords)