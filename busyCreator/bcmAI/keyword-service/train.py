import torch
from torch.utils.data import DataLoader
from model import create_model
from data_processor import KeywordDataset
from config import Config
from tqdm import tqdm


def train():
    config = Config()

    # 准备数据
    train_dataset = KeywordDataset(config.train_data_path)
    train_loader = DataLoader(train_dataset, batch_size=config.batch_size, shuffle=True)

    # 初始化模型
    model = create_model()

    # 优化器（只训练可训练参数）
    optimizer = torch.optim.AdamW(
        model.parameters(),
        lr=config.learning_rate
    )

    # 训练循环
    for epoch in range(config.num_epochs):
        model.train()
        total_loss = 0

        for batch in tqdm(train_loader, desc=f"Epoch {epoch + 1}"):
            inputs = {
                "input_ids": batch["input_ids"].to(config.device),
                "attention_mask": batch["attention_mask"].to(config.device),
                "labels": batch["labels"].to(config.device)
            }

            outputs = model(**inputs)
            loss = outputs.loss
            loss.backward()

            optimizer.step()
            optimizer.zero_grad()

            total_loss += loss.item()

        avg_loss = total_loss / len(train_loader)
        print(f"Epoch {epoch + 1} Loss: {avg_loss:.4f}")

    # 保存LoRA适配器
    model.save_pretrained(config.model_save_path)


if __name__ == "__main__":
    train()