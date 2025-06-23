> # 🚀 BusyCreator - 智能文本内容创作平台
>
> > **高效创作 · AI赋能 · 团队协作**  
> > 开源整合版 | 作者：caoyanghalo@qq.com
>
> ------
>
> ## 🌟 项目简介
>
> **BusyCreator** 是一个功能强大的开源智能文本创作平台，深度融合大语言模型能力，提供从内容生成到团队协作的全流程解决方案。平台支持小说创作、广告文案、新闻润色等多种文本类型，赋能创作者提升内容质量和生产效率。
>
> ------
>
> ## 🛠 核心功能
>
> ### 🤖 AI智能创作
> - **多模型支持**：DeepSeek-R1核心引擎 + 星火多模态解析
> - **文本生成**：小说剧情、广告文案、新闻稿等智能创作
> - **内容增强**：错别字检测（pycorrect）、文本润色
>
> ### 📚 创作管理
> - 多项目管理与版本控制（Git式管理）
> - 富文本编辑器与Markdown双模式
> - 审核发布工作流
> - 作品库管理与分类浏览
>
> ### 👥 团队协作
> - 实时协同编辑
> - 任务分配与进度追踪
> - 角色权限管理
> - 修改历史与版本对比
>
> ### 💬 AI交互体验
> - **流式响应**：SSE服务端推送技术
> - **打字机效果**：实时渐进式内容展示
> - 多轮对话上下文管理
> - 个性化推荐系统
>
> ------
>
> ## 🖼 界面预览
>
> | 功能模块         | 界面展示                                                     |
> | ---------------- | ------------------------------------------------------------ |
> | **AI咨询对话**   | ![AI咨询界面](https://files-1317662942.cos.ap-nanjing.myqcloud.com/files_to_view/image-20250529133905239.png) |
> | **流式响应效果** | ![流式响应](https://files-1317662942.cos.ap-nanjing.myqcloud.com/files_to_view/image-20250529133958205.png) |
> | **项目管理**     | ![项目管理](https://files-1317662942.cos.ap-nanjing.myqcloud.com/files_to_view/image-20250529134104104.png) |
> | **任务指派**     | ![任务指派](https://files-1317662942.cos.ap-nanjing.myqcloud.com/files_to_view/image-20250529134302870.png) |
> | **团队管理**     | ![团队管理](https://files-1317662942.cos.ap-nanjing.myqcloud.com/files_to_view/image-20250529134330777.png) |
> | **个人中心**     | ![个人信息](https://files-1317662942.cos.ap-nanjing.myqcloud.com/files_to_view/image-20250529134359099.png) |
> | **创作详情页**   | ![创作详情页](https://files-1317662942.cos.ap-nanjing.myqcloud.com/files_to_view/1750663334760.png) |
> | **主界面概览**   | ![平台主界面](https://files-1317662942.cos.ap-nanjing.myqcloud.com/files_to_view/image-20250529133829744.png) |
>
> ------
>
> ## ⚙️ 技术栈
>
> ### 前端
> <div align="center">
>   <img src="https://img.shields.io/badge/Vue.js-3-42b883?logo=vue.js" alt="Vue3">
>   <img src="https://img.shields.io/badge/Element_Plus-2.3-409EFF?logo=element" alt="Element Plus">
>   <img src="https://img.shields.io/badge/SSE-流式通信-007ACC?logo=html5" alt="SSE">
> </div>
>
> - **Vue 3** (Composition API + Pinia状态管理)
> - Element Plus UI框架
> - SSE流式通信实现
>
> ### 后端
> <div align="center">
>   <img src="https://img.shields.io/badge/Spring_Boot-2.7-6DB33F?logo=spring" alt="Spring Boot">
>   <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql" alt="MySQL">
>   <img src="https://img.shields.io/badge/Redis-7.0-DC382D?logo=redis" alt="Redis">
>   <img src="https://img.shields.io/badge/RabbitMQ-3.12-FF6600?logo=rabbitmq" alt="RabbitMQ">
>   <img src="https://img.shields.io/badge/Elasticsearch-8.9-005571?logo=elasticsearch" alt="Elasticsearch">
> </div>
>
> - **Spring Boot 2.7** (核心框架)
> - **MySQL** (主数据存储)
> - **Redis** (缓存与会话管理)
> - **RabbitMQ** (异步任务队列)
> - **Elasticsearch** (内容检索)
>
> ### AI子系统
> <div align="center">
>   <img src="https://img.shields.io/badge/DeepSeek_R1-7B-0d6efd?logo=deepseek" alt="DeepSeek-R1">
>   <img src="https://img.shields.io/badge/星火多模态-3.0-FF6600?logo=iflytek" alt="星火大模型">
>   <img src="https://img.shields.io/badge/Milvus-2.3-00d1b2?logo=python" alt="Milvus">
>   <img src="https://img.shields.io/badge/FastAPI-0.100-009688?logo=fastapi" alt="FastAPI">
> </div>
>
> - **模型服务**：
>   - DeepSeek-R1 7B蒸馏模型（微调）
>   - 星火多模态大模型（文件解析）
> - **AI框架**：
>   - Flask（轻量级服务）
>   - FastAPI（高性能接口）
> - **智能处理**：
>   - pycorrect（错别字检测）
>   - **Milvus**（向量数据库/语义检索）
> - 模型蒸馏与微调工具链
>
> ------
>
> ## 📦 开源信息
>
> **版本**: v1.0 开源整合版  
> **许可**: [![Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)  
> **代码仓库**: [![GitHub Repo](https://img.shields.io/badge/GitHub-BusyCreator-181717?logo=github)](https://github.com/your-repo-link) 
>
> > ✨ 赋能每一个创作梦想，让灵感高效流动