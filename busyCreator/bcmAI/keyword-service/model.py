from transformers import BertForTokenClassification
from peft import LoraConfig, get_peft_model
from config import Config


def create_model():
    config = Config()

    # 加载基础模型
    model = BertForTokenClassification.from_pretrained(
        config.model_name,
        num_labels=config.num_labels
    )

    # LoRA配置
    lora_config = LoraConfig(
        r=config.lora_rank,
        lora_alpha=config.lora_alpha,
        target_modules=["query", "value"],  # 在BERT的query和value矩阵添加LoRA
        lora_dropout=config.lora_dropout,
        bias="none",
        task_type="TOKEN_CLS"
    )

    # 应用LoRA
    model = get_peft_model(model, lora_config)
    model.print_trainable_parameters()

    return model.to(config.device)