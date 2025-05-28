import configparser
import time
from pymilvus import MilvusClient, DataType
import numpy as np
from gensim.models import FastText

# 读取配置文件
cfp = configparser.RawConfigParser()
cfp.read('config.ini')  # 读取配置文件 config.ini
milvus_uri = cfp.get('example', 'uri')  # 获取 Milvus 服务器的 URI
token = cfp.get('example', 'token')  # 获取访问 Milvus 的认证令牌

# 连接到 Milvus 服务器
milvus_client = MilvusClient(uri=milvus_uri, token=token)
print(f"Connected to DB: {milvus_uri} successfully")

# 定义文本数据
works_tag = [
    "人工智能",
    "Java",
    "深度学习",
    "自然语言处理",
    "Python",
    "数学",
    "英语",
    "欣欣",
    "小蕾"
]
# 训练 FastText 模型
model = FastText(works_tag, vector_size=300, window=5, min_count=1, sg=0)

# 生成向量
embeddings = model.wv(works_tag)
print(embeddings)

# 将所有标签的向量合并为一个向量
combined_embedding = np.mean(embeddings, axis=0)
print("Combined embedding:", combined_embedding)

# 检查集合是否存在
collection_name = "works"  # 定义集合名称:作品
check_collection = milvus_client.has_collection(collection_name)  # 检查集合是否存在

# 如果集合存在，则删除它
if check_collection:
    milvus_client.drop_collection(collection_name)
    print(f"Dropped the existing collection {collection_name} successfully")

# 定义向量的维度
dim = len(combined_embedding)  # 获取嵌入向量的维度

# 创建集合的 schema
print("Start to create the collection schema")
schema = milvus_client.create_schema()  # 创建一个新的 schema
schema.add_field("works_id", DataType.INT64, is_primary=True, description="customized primary id")  # 添加主键字段
schema.add_field("works_tag_vector", DataType.FLOAT_VECTOR, dim=dim, description="tag vector of works")  # 添加向量字段
schema.add_field("works_tag", DataType.VARCHAR, max_length=1024, description="works tag string")  # 添加字符串字段

print("Start to prepare index parameters with default AUTOINDEX")
index_params = milvus_client.prepare_index_params()  # 准备索引参数
index_params.add_index("works_tag_vector", metric_type="COSINE")  # 为向量字段添加索引，使用余弦相似度

# 创建集合，并自动加载
print(f"Start to create example collection: {collection_name}")
milvus_client.create_collection(collection_name, schema=schema, index_params=index_params)
collection_property = milvus_client.describe_collection(collection_name)  # 获取集合的详细信息
print("Collection details: %s" % collection_property)

# 插入数据
rows = [
    {
        "works_id": 0,
        "works_tag_vector": combined_embedding.tolist(),  # 将 NumPy 数组转换为列表
        "works_tag": " ".join(works_tag)  # 将所有标签合并为一个字符串
    }
]  # 构造插入的数据
t0 = time.time()  # 记录开始时间
milvus_client.insert(collection_name, rows)  # 插入数据
ins_rt = time.time() - t0  # 计算插入时间
print(f"Insert completed in {round(ins_rt, 4)} seconds")

# 刷新集合
print("Start to flush")
start_flush = time.time()  # 记录开始时间
milvus_client.flush(collection_name)  # 刷新集合，确保数据持久化
end_flush = time.time()  # 记录结束时间
print(f"Flush completed in {round(end_flush - start_flush, 4)} seconds")

# 搜索数据
query_text = "人工智能"  # 查询文本
query_vector = model.wv([query_text])[0]  # 生成查询向量
limit = 5  # 返回的最近邻数量
search_params = {"metric_type": "COSINE", "params": {"level": 2}}  # 搜索参数，使用余弦相似度

t0 = time.time()  # 记录开始时间
results = milvus_client.search(
    collection_name,
    data=[query_vector],
    limit=limit,
    search_params=search_params,
    anns_field="works_tag_vector",
    output_fields=["works_tag"]
)
t1 = time.time()  # 记录结束时间

# 打印搜索结果
print(f"Search results: {results}")
print(f"Search latency: {round(t1 - t0, 4)} seconds")
