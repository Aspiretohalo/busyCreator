import configparser
import numpy as np
from flask import Flask, request, jsonify
from pymilvus import MilvusClient, DataType
from sentence_transformers import SentenceTransformer

app = Flask(__name__)

# 读取配置文件
cfp = configparser.RawConfigParser()
cfp.read('config.ini')  # 读取配置文件 config.ini
milvus_uri = cfp.get('example', 'uri')  # 获取 Milvus 服务器的 URI
token = cfp.get('example', 'token')  # 获取访问 Milvus 的认证令牌

# 连接到 Milvus 服务器
milvus_client = MilvusClient(uri=milvus_uri, token=token)
print(f"Connected to DB: {milvus_uri} successfully")

# 加载 sbert 模型
model = SentenceTransformer("uer/sbert-base-chinese-nli")
# 或者加载本地模型路径
model = SentenceTransformer("./sbert-base-chinese-nli")

# 定义集合名称
collection_name = "works"

# 检查集合是否存在，如果不存在则创建
if not milvus_client.has_collection(collection_name):
    dim = 768  # 假设使用 sbert-base-chinese-nli 模型，其输出维度为 768
    schema = milvus_client.create_schema()
    schema.add_field("works_id", DataType.INT64, is_primary=True, description="customized primary id")
    schema.add_field("works_tag_vector", DataType.FLOAT_VECTOR, dim=dim, description="tag vector of works")
    schema.add_field("works_tag", DataType.VARCHAR, max_length=1024, description="works tag string")
    index_params = milvus_client.prepare_index_params()
    index_params.add_index("works_tag_vector", metric_type="COSINE")
    milvus_client.create_collection(collection_name, schema=schema, index_params=index_params)
    print(f"Collection {collection_name} created successfully")


@app.route('/insert', methods=['POST'])
def insert():
    data = request.json
    works_id = data.get('works_id', 0)  # 从请求中获取 works_id，默认值为 0
    works_tag = data.get('tags', [])  # 从请求中获取 tags，默认值为空列表

    filter_expr = f"works_id == {works_id}"
    results = milvus_client.query(
        collection_name=collection_name,
        filter=filter_expr,
        output_fields=["works_id", "works_tag"]
    )
    print('向量数据库中已存在：', results)
    # 检查 works_id 是否已存在
    if results:
        return jsonify({"status": "repeated", "message": "Data already exists"})

    embeddings = model.encode(works_tag)
    combined_embedding = np.mean(embeddings, axis=0)
    rows = [
        {
            "works_id": works_id,  # 使用传入的 works_id
            "works_tag_vector": combined_embedding.tolist(),
            "works_tag": " ".join(works_tag)
        }
    ]
    milvus_client.insert(collection_name, rows)
    milvus_client.flush(collection_name)
    return jsonify({"status": "success", "message": "Data inserted successfully"})


@app.route('/search', methods=['POST'])
def search():
    data = request.json
    query_text = data.get('query', "")
    query_vector = model.encode([query_text])[0]
    # limit = data.get('limit', 5)
    search_params = {"metric_type": "COSINE", "params": {"level": 2}}
    results = milvus_client.search(
        collection_name,
        data=[query_vector],
        # limit=limit,
        search_params=search_params,
        anns_field="works_tag_vector",
        output_fields=["works_tag"]
    )
    return jsonify({"results": results})


if __name__ == '__main__':
    app.run(debug=True)
