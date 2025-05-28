-- 用户表 user
CREATE TABLE user
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID，主键，自增',
    user_name     VARCHAR(24)  NOT NULL COMMENT '用户名',
    user_account  VARCHAR(11)  NOT NULL UNIQUE COMMENT '用户账号，使用手机号作为账号',
    user_password VARCHAR(255) NOT NULL COMMENT '用户密码，使用MD5加密',
    user_avatar   VARCHAR(255) COMMENT '用户头像，存储在腾讯云COS',
    profile       VARCHAR(255) COMMENT '用户简介',
    email         VARCHAR(50) COMMENT '用户邮箱',
    sex           VARCHAR(2) COMMENT '用户性别',
    role          VARCHAR(24) CHECK (role IN ('admin', 'user')) COMMENT '用户角色：admin 或 user',
    delete_flag   VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除',
    created_at    TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at    TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    deleted_at    TIMESTAMP    NULL COMMENT '记录删除时间'
) COMMENT ='用户信息表';

-- 插入用户数据
INSERT INTO `user` (`id`, `user_name`, `user_account`, `user_password`, `user_avatar`, `profile`, `email`, `sex`,
                    `role`, `delete_flag`, `created_at`, `updated_at`, `deleted_at`)
VALUES (1, '蕾蕾天下第一', '060321', 'b0dd3697a192885d7c055db46155b26a',
        'https://img2.baidu.com/it/u=386597271,2553686160&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500', NULL, NULL, NULL,
        'admin', '0',
        '2024-07-14 17:23:27', '2024-07-26 23:20:48', NULL);

INSERT INTO `user` (`id`, `user_name`, `user_account`, `user_password`, `user_avatar`, `profile`, `email`, `sex`,
                    `role`, `delete_flag`, `created_at`, `updated_at`, `deleted_at`)
VALUES (2, 'cy天下第一', '030321', 'b0dd3697a192885d7c055db46155b26a',
        'https://ss2.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3156919332,3306242375&fm=253&gp=0.jpg', NULL, NULL,
        NULL, 'user', '0',
        '2024-07-26 23:17:38', '2024-07-26 23:20:48', NULL);

-- 项目表 bcmcreate_project
CREATE TABLE bcmcreate_project
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '项目ID，主键，自增',
    name        VARCHAR(100) NOT NULL COMMENT '项目名称',
    profile     VARCHAR(255) COMMENT '项目简介',
    cover       TEXT COMMENT '封面',
    tags        TEXT COMMENT '标签',
    version     BIGINT COMMENT '项目当前版本，对应项目内容表的ID',
    team_id     BIGINT COMMENT '团队ID，每个项目对应一个团队',
    is_public   VARCHAR(1) CHECK (is_public IN ('0', '1')) COMMENT '是否公开为作品：0-否，1-是',
    insert_user BIGINT COMMENT '创建该项目的用户ID',
    insert_time DATETIME COMMENT '项目创建时间',
    update_user BIGINT COMMENT '最后更新该项目的用户ID',
    update_time DATETIME COMMENT '项目最后更新时间',
    delete_user BIGINT COMMENT '删除该项目的用户ID',
    delete_time DATETIME COMMENT '项目删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='项目（作品）信息表';

-- 项目内容、更新记录表 bcmcreate_project_record
CREATE TABLE bcmcreate_project_record
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '项目记录ID，主键，自增',
    project_id     BIGINT COMMENT '项目ID，关联项目表',
    content        TEXT COMMENT '项目内容，当前版本的快照',
    content_add    TEXT COMMENT '新增内容',
    content_minus  TEXT COMMENT '减少的内容',
    record_comment VARCHAR(255) COMMENT '备注评论	每次提交都要有备注评论，到底本次修改了什么',
    team_id        BIGINT COMMENT '团队ID，关联团队表',
    insert_user    BIGINT COMMENT '创建该记录的用户ID',
    insert_time    DATETIME COMMENT '记录创建时间',
    update_user    BIGINT COMMENT '最后更新该记录的用户ID',
    update_time    DATETIME COMMENT '记录最后更新时间',
    delete_user    BIGINT COMMENT '删除该记录的用户ID',
    delete_time    DATETIME COMMENT '记录删除时间',
    delete_flag    VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除',
    is_submit      VARCHAR(1) DEFAULT '0' COMMENT '该记录是否已提交保存：0为个人暂存，1为已提交的版本'
) COMMENT ='项目内容及更新记录表';

-- 项目点赞收藏表 bcmcreate_project_thumb
CREATE TABLE bcmcreate_project_thumb
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID，主键，自增',
    project_id  BIGINT      NOT NULL COMMENT '所属项目id',
    user_id     BIGINT      NOT NULL COMMENT '用户id',
    type        VARCHAR(24) NOT NULL COMMENT '点赞thumb或收藏collect',
    insert_user BIGINT COMMENT '创建人员',
    insert_time DATETIME COMMENT '创建时间',
    update_user BIGINT COMMENT '更新人员',
    update_time DATETIME COMMENT '更新时间',
    delete_user BIGINT COMMENT '删除人员',
    delete_time DATETIME COMMENT '删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除'
) COMMENT ='项目点赞收藏表';

CREATE TABLE bcmcreate_project_audit
(
    id                BIGINT AUTO_INCREMENT COMMENT 'ID，主键，自增',
    project_id        BIGINT COMMENT '所属项目id',
    project_record_id BIGINT COMMENT '所属项目记录id',
    status            VARCHAR(4) COMMENT '审核进度状态，审核中11，审核通过21，审核退回22',
    insert_user       BIGINT COMMENT '创建人员',
    insert_time       DATETIME COMMENT '创建时间',
    update_user       BIGINT COMMENT '更新人员',
    update_time       DATETIME COMMENT '更新时间',
    delete_user       BIGINT COMMENT '删除人员',
    delete_time       DATETIME COMMENT '删除时间',
    delete_flag       VARCHAR(1) DEFAULT '0' COMMENT '删除标记，逻辑删除',
    PRIMARY KEY (id)
) COMMENT ='项目审核表';

-- 团队表 bcmcreate_team
CREATE TABLE bcmcreate_team
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '团队ID，主键，自增',
    name        VARCHAR(255) COMMENT '团队名',
    avatar      VARCHAR(1024) COMMENT '团队头像',
    profile     VARCHAR(255) COMMENT '团队简介',
    admin_id    BIGINT COMMENT '团队管理员ID',
    member_list VARCHAR(1024) COMMENT '团队成员列表，以逗号分隔的用户ID',
    insert_user BIGINT COMMENT '创建该团队的用户ID',
    insert_time DATETIME COMMENT '团队创建时间',
    update_user BIGINT COMMENT '最后更新该团队的用户ID',
    update_time DATETIME COMMENT '团队最后更新时间',
    delete_user BIGINT COMMENT '删除该团队的用户ID',
    delete_time DATETIME COMMENT '团队删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='团队信息表';

-- 消息通知表 bcmcreate_msg
CREATE TABLE bcmcreate_msg
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '消息ID，主键，自增',
    title       VARCHAR(255) NOT NULL COMMENT '消息标题',
    content     TEXT COMMENT '消息内容',
    own_user    BIGINT COMMENT '消息归属用户ID',
    is_read     INT        DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    insert_user BIGINT COMMENT '创建该消息的用户ID',
    insert_time DATETIME COMMENT '消息创建时间',
    update_user BIGINT COMMENT '最后更新该消息的用户ID',
    update_time DATETIME COMMENT '消息最后更新时间',
    delete_user BIGINT COMMENT '删除该消息的用户ID',
    delete_time DATETIME COMMENT '消息删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='消息通知表';

-- 生成记录表 bcmcreate_generate_record
CREATE TABLE bcmcreate_generate_record
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '生成记录ID，主键，自增',
    type        VARCHAR(24) CHECK (type IN ('DRAFT', 'CREATIVITY')) COMMENT '生成内容类型：DRAFT-文章草稿，CREATIVITY-创意激发',
    content     TEXT COMMENT '生成的内容',
    summary     VARCHAR(255) COMMENT '内容摘要',
    project_id  BIGINT COMMENT '归属项目ID',
    insert_user BIGINT COMMENT '创建该记录的用户ID',
    insert_time DATETIME COMMENT '记录创建时间',
    update_user BIGINT COMMENT '最后更新该记录的用户ID',
    update_time DATETIME COMMENT '记录最后更新时间',
    delete_user BIGINT COMMENT '删除该记录的用户ID',
    delete_time DATETIME COMMENT '记录删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='生成记录表';

-- 搜索记录表 bcmcreate_search_record
CREATE TABLE bcmcreate_search_record
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '搜索记录ID，主键，自增',
    keyword     VARCHAR(255) NOT NULL COMMENT '搜索关键词',
    user_id     BIGINT COMMENT '搜索用户ID',
    insert_user BIGINT COMMENT '创建该记录的用户ID',
    insert_time DATETIME COMMENT '记录创建时间',
    update_user BIGINT COMMENT '最后更新该记录的用户ID',
    update_time DATETIME COMMENT '记录最后更新时间',
    delete_user BIGINT COMMENT '删除该记录的用户ID',
    delete_time DATETIME COMMENT '记录删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='搜索记录表';

-- 用户接口访问情况表 bcmcreate_interface_access
CREATE TABLE bcmcreate_interface_access
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '访问情况ID，主键，自增',
    interface_path VARCHAR(100) NOT NULL COMMENT '接口路径',
    request_ip     VARCHAR(100) COMMENT '请求IP',
    request_params TEXT COMMENT '请求携带参数',
    response_time  BIGINT COMMENT '响应时长（单位：毫秒）',
    user_id        BIGINT COMMENT '用户ID',
    insert_user    BIGINT COMMENT '创建人员',
    insert_time    DATETIME COMMENT '创建时间',
    update_user    BIGINT COMMENT '更新人员',
    update_time    DATETIME COMMENT '更新时间',
    delete_user    BIGINT COMMENT '删除人员',
    delete_time    DATETIME COMMENT '删除时间',
    delete_flag    VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='用户接口访问情况表';

-- 用户作品浏览情况表 bcmcreate_project_access
CREATE TABLE bcmcreate_project_access
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '作品浏览记录ID，主键，自增',
    project_id      BIGINT COMMENT '作品ID（已公开项目）',
    project_name    VARCHAR(100) NOT NULL COMMENT '作品名称，冗余字段，减少连表查询',
    user_id         BIGINT COMMENT '浏览用户ID',
    access_duration DOUBLE COMMENT '浏览总时长（秒）',
    access_count    BIGINT     DEFAULT 0 COMMENT '浏览次数',
    insert_user     BIGINT COMMENT '创建该记录的用户ID',
    insert_time     DATETIME COMMENT '记录创建时间',
    update_user     BIGINT COMMENT '最后更新该记录的用户ID',
    update_time     DATETIME COMMENT '记录最后更新时间',
    delete_user     BIGINT COMMENT '删除该记录的用户ID',
    delete_time     DATETIME COMMENT '记录删除时间',
    delete_flag     VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='用户作品浏览情况表';

-- AI会话表 bcmcreate_ai_session
CREATE TABLE bcmcreate_ai_session
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'AI会话ID，主键，自增',
    user_id     BIGINT COMMENT '发起会话的用户ID',
    title       VARCHAR(100) NOT NULL COMMENT '会话标题',
    insert_user BIGINT COMMENT '创建该记录的用户ID',
    insert_time DATETIME COMMENT '记录创建时间',
    update_user BIGINT COMMENT '最后更新该记录的用户ID',
    update_time DATETIME COMMENT '记录最后更新时间',
    delete_user BIGINT COMMENT '删除该记录的用户ID',
    delete_time DATETIME COMMENT '记录删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='AI会话表';

-- AI提问表 bcmcreate_ai_question
CREATE TABLE bcmcreate_ai_question
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'AI问题ID，主键，自增',
    session_id    BIGINT COMMENT '所属AI会话ID',
    question_text TEXT COMMENT '问题内容',
    insert_user   BIGINT COMMENT '创建该记录的用户ID',
    insert_time   DATETIME COMMENT '记录创建时间',
    update_user   BIGINT COMMENT '最后更新该记录的用户ID',
    update_time   DATETIME COMMENT '记录最后更新时间',
    delete_user   BIGINT COMMENT '删除该记录的用户ID',
    delete_time   DATETIME COMMENT '记录删除时间',
    delete_flag   VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='AI提问表';

-- AI回复表 bcmcreate_ai_answer
CREATE TABLE bcmcreate_ai_answer
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'AI回答ID，主键，自增',
    question_id BIGINT COMMENT '所属问题ID',
    answer_text TEXT COMMENT '回答内容',
    insert_user BIGINT COMMENT '创建该记录的用户ID',
    insert_time DATETIME COMMENT '记录创建时间',
    update_user BIGINT COMMENT '最后更新该记录的用户ID',
    update_time DATETIME COMMENT '记录最后更新时间',
    delete_user BIGINT COMMENT '删除该记录的用户ID',
    delete_time DATETIME COMMENT '记录删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='AI答复表';

-- AI回复思维链表 bcmcreate_ai_reasoning
CREATE TABLE bcmcreate_ai_reasoning
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'AI回复思维链ID，主键，自增',
    question_id       BIGINT COMMENT '所属问题ID',
    reasoning_content TEXT COMMENT '思维链内容',
    insert_user       BIGINT COMMENT '创建该记录的用户ID',
    insert_time       DATETIME COMMENT '记录创建时间',
    update_user       BIGINT COMMENT '最后更新该记录的用户ID',
    update_time       DATETIME COMMENT '记录最后更新时间',
    delete_user       BIGINT COMMENT '删除该记录的用户ID',
    delete_time       DATETIME COMMENT '记录删除时间',
    delete_flag       VARCHAR(1) DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除'
) COMMENT ='AI回复思维链表';

CREATE TABLE bcmcreate_task
(
    id            BIGINT AUTO_INCREMENT COMMENT 'ID，主键，自增',
    project_id    BIGINT COMMENT '所属项目id',
    project_name  VARCHAR(100) COMMENT '所属项目名称',
    content       VARCHAR(255) COMMENT '任务内容',
    assignee_id   BIGINT COMMENT '任务对象id',
    assignee_name VARCHAR(100) COMMENT '任务对象名',
    finish_date   DATE COMMENT '完成日期',
    work_time     INT COMMENT '耗费工时',
    note          VARCHAR(255) COMMENT '备注',
    insert_user   BIGINT COMMENT '创建人员',
    insert_time   DATETIME COMMENT '创建时间',
    update_user   BIGINT COMMENT '更新人员',
    update_time   DATETIME COMMENT '更新时间',
    delete_user   BIGINT COMMENT '删除人员',
    delete_time   DATETIME COMMENT '删除时间',
    delete_flag   VARCHAR(1) DEFAULT '0' COMMENT '删除标记，逻辑删除',
    PRIMARY KEY (id)
) COMMENT ='任务表';

CREATE TABLE bcmcreate_inspiration
(
    id          BIGINT AUTO_INCREMENT COMMENT 'ID，主键，自增',
    title       VARCHAR(255) COMMENT '灵感主题',
    content     TEXT COMMENT '灵感内容',
    tags        TEXT COMMENT '标签，多个标签用逗号分隔',
    type        VARCHAR(100) COMMENT '灵感类型：小说、广告、新闻',
    insert_user BIGINT COMMENT '创建人员',
    insert_time DATETIME COMMENT '创建时间',
    update_user BIGINT COMMENT '更新人员',
    update_time DATETIME COMMENT '更新时间',
    delete_user BIGINT COMMENT '删除人员',
    delete_time DATETIME COMMENT '删除时间',
    delete_flag VARCHAR(1) DEFAULT '0' COMMENT '删除标记，逻辑删除',
    PRIMARY KEY (id)
) COMMENT ='灵感表';