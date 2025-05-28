import torch


class Config:
    # 模型参数
    model_name = "bert-base-chinese"
    num_labels = 1  # 二分类问题（是否是关键词）

    # LoRA参数
    lora_rank = 8
    lora_alpha = 32
    lora_dropout = 0.1

    # 训练参数
    batch_size = 16
    learning_rate = 2e-5
    num_epochs = 3
    max_length = 128  # 文本最大长度

    # 路径配置
    train_data_path = "data/train.json"
    dev_data_path = "data/dev.json"
    model_save_path = "output/lora_adapter/"

    device = "cuda" if torch.cuda.is_available() else "cpu"