import configparser
import time
from sentence_transformers import SentenceTransformer
from pymilvus import MilvusClient, DataType
import numpy as np

# 读取配置文件
cfp = configparser.RawConfigParser()
cfp.read('config.ini')  # 读取配置文件 config.ini
milvus_uri = cfp.get('example', 'uri')  # 获取 Milvus 服务器的 URI
token = cfp.get('example', 'token')  # 获取访问 Milvus 的认证令牌

# 连接到 Milvus 服务器
milvus_client = MilvusClient(uri=milvus_uri, token=token)
print(f"Connected to DB: {milvus_uri} successfully")

# 加载 sbert 模型
model = SentenceTransformer("./sbert-base-chinese-nli")  # 或者加载本地模型路径

# 定义用户标签
user_tags = [
    "人工智能",
    "Java",
    "欣欣",
    "小蕾"
]

# 生成用户标签的嵌入向量
user_tags_embedding = model.encode(user_tags)

# 将所有标签的向量合并为一个向量
combined_embedding = np.mean(user_tags_embedding, axis=0)

# 检查集合是否存在
collection_name = "works"  # 定义集合名称
check_collection = milvus_client.has_collection(collection_name)  # 检查集合是否存在

# 定义向量的维度
dim = len(combined_embedding)  # 获取嵌入向量的维度

# 刷新集合
print("Start to flush")
start_flush = time.time()  # 记录开始时间
milvus_client.flush(collection_name)  # 刷新集合，确保数据持久化
end_flush = time.time()  # 记录结束时间
print(f"Flush completed in {round(end_flush - start_flush, 4)} seconds")

# 搜索数据
query_text = "小蕾"  # 查询文本
query_vector = model.encode([query_text])[0]  # 生成查询向量
limit = 5  # 返回的最近邻数量
search_params = {"metric_type": "COSINE", "params": {"level": 2}}  # 搜索参数，使用余弦相似度

t0 = time.time()  # 记录开始时间
results = milvus_client.search(
    collection_name,
    data=[combined_embedding],
    limit=limit,
    search_params=search_params,
    anns_field="works_tag_vector",
    output_fields=["works_id", "works_tag"]
)
t1 = time.time()  # 记录结束时间

# 打印搜索结果
print(f"Search results: {results}")
print(f"Search latency: {round(t1 - t0, 4)} seconds")