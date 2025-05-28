from transformers import BertTokenizer
import json
import torch
from config import Config


class KeywordDataset(torch.utils.data.Dataset):
    def __init__(self, data_path):
        self.config = Config()
        self.tokenizer = BertTokenizer.from_pretrained(self.config.model_name)
        self.data = self.load_data(data_path)

    def load_data(self, path):
        with open(path) as f:
            raw_data = json.load(f)
        return [(item["text"], item["keywords"]) for item in raw_data]

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        text, keywords = self.data[idx]
        encoding = self.tokenizer(
            text,
            max_length=self.config.max_length,
            padding="max_length",
            truncation=True,
            return_tensors="pt"
        )

        # 创建标签（简单示例：关键词位置标记为1）
        tokens = self.tokenizer.tokenize(text)
        labels = [0] * self.config.max_length
        for keyword in keywords:
            keyword_tokens = self.tokenizer.tokenize(keyword)
            # 这里需要实现关键词位置匹配逻辑（简化为首token标记）
            # 实际应用需要更精确的匹配算法
            labels[1] = 1  # 示例位置

        return {
            "input_ids": encoding["input_ids"].flatten(),
            "attention_mask": encoding["attention_mask"].flatten(),
            "labels": torch.tensor(labels, dtype=torch.float)
        }