package com.busymango.busymangoBackend.AI.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.busymango.busymangoBackend.AI.model.dto.AIQuestionRequest;
import com.busymango.busymangoBackend.AI.model.dto.IdeaGenerateRequest;
import com.busymango.busymangoBackend.AI.model.vo.AIParseVO;
import com.busymango.busymangoBackend.AI.service.AIChatService;
import com.busymango.busymangoBackend.AI.service.AiParseService;
import com.busymango.busymangoBackend.core.utils.ImageUtil;
import com.busymango.busymangoBackend.inspiration.model.enums.ContentTypeEnum;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static com.busymango.busymangoBackend.core.utils.StringUtil.replaceDoubleQuotesWithSingleQuotes;

@Service
@Slf4j
public class AiParseServiceImpl implements AiParseService {
    @Value("${xfyun.host-url}")
    private String hostUrl;

    @Value("${xfyun.appid}")
    private String appid;

    @Value("${xfyun.app-secret}")
    private String apiSecret;

    @Value("${xfyun.app-key}")
    private String apiKey;
    @Resource
    private AIChatService aiChatService;
    private static final Gson gson = new Gson();

    /**
     * 从文件解析创意
     *
     * @param file
     * @return
     */
    @Override
    public AIParseVO parseIdeasByFile(MultipartFile file) {
        AIParseVO aiParseVO = new AIParseVO();

        try {
            // 保存文件到临时路径
            String tempFilePath = saveFileToTemp(file);

            // 调用大模型解析创意
            String result = callModel("描述这张图片的内容", tempFilePath);
            log.info("大模型返回结果： {}", result);

            String theme = "万圣节 节日氛围 猫咪 宠物装扮 蓬松白色毛发 大眼睛 呆萌表情 幽默 浅色墙面 温馨";
            // 封装到 AIParseVO 对象
            aiParseVO.setTheme(theme);
            aiParseVO.setDescription(result);

        } catch (Exception e) {
            log.error("通过文件解析创意失败", e);
            aiParseVO.setTheme("解析错误...");
            aiParseVO.setDescription("解析错误...");
        }

        return aiParseVO;
    }

    private String saveFileToTemp(MultipartFile file) throws IOException {
        // 生成临时文件路径
        String tempFilePath = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString() + file.getOriginalFilename();
        file.transferTo(new java.io.File(tempFilePath));
        return tempFilePath;
    }

    @Override
    public SseEmitter generateIdea(IdeaGenerateRequest ideaGenerateRequest) {
        log.info(ideaGenerateRequest.toString());
        String newContent = replaceDoubleQuotesWithSingleQuotes(ideaGenerateRequest.getContent());
        String fullQuestion = "请按照" + ideaGenerateRequest.getGenre() + "的文体风格，以及" + ideaGenerateRequest.getTheme() + "这些主题来生成合适的内容，也要参考用户如下的描述内容：" + ideaGenerateRequest.getDescription() + "。原文如下，你应该在这一段文本上进行修改、润色、补充：" + newContent;
        ContentTypeEnum contentType = ContentTypeEnum.fromDescription(ideaGenerateRequest.getType());

        AIQuestionRequest aiQuestionRequest = new AIQuestionRequest();
        aiQuestionRequest.setQuestion(fullQuestion); // 设置完整的问题
        aiQuestionRequest.setType(contentType.getType());
        return aiChatService.getAnswer(aiQuestionRequest);
    }

    public String callModel(String question, String imagePath) throws Exception {
        log.info("正在调用大模型...");
        // 构建鉴权 URL
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);

        // 替换为 WebSocket URL
        String wsUrl = authUrl.replace("http://", "ws://").replace("https://", "wss://");

        // 创建 WebSocket 客户端
        OkHttpClient client = new OkHttpClient.Builder().build();
        final CountDownLatch latch = new CountDownLatch(1);
        final StringBuilder answer = new StringBuilder();

        WebSocket webSocket = client.newWebSocket(new Request.Builder().url(wsUrl).build(), new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                try {
                    JSONObject requestJson = new JSONObject();

                    JSONObject header = new JSONObject();
                    header.put("app_id", appid);
                    header.put("uid", UUID.randomUUID().toString().substring(0, 10));

                    JSONObject parameter = new JSONObject();
                    JSONObject chat = new JSONObject();
                    chat.put("domain", "imagev3");
                    chat.put("temperature", 0.5);
                    chat.put("max_tokens", 4096);
                    chat.put("auditing", "default");
                    parameter.put("chat", chat);

                    JSONObject payload = new JSONObject();
                    JSONObject message = new JSONObject();
                    JSONArray text = new JSONArray();

                    // 添加图片信息
                    JSONObject roleContent = new JSONObject();
                    roleContent.put("role", "user");
                    roleContent.put("content", Base64.getEncoder().encodeToString(ImageUtil.read(imagePath)));
                    roleContent.put("content_type", "image");
                    text.add(roleContent);

                    // 添加问题
                    JSONObject roleContentText = new JSONObject();
                    roleContentText.put("role", "user");
                    roleContentText.put("content", question);
                    roleContentText.put("content_type", "text");
                    text.add(roleContentText);

                    message.put("text", text);
                    payload.put("message", message);

                    requestJson.put("header", header);
                    requestJson.put("parameter", parameter);
                    requestJson.put("payload", payload);

                    webSocket.send(requestJson.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);
                if (myJsonParse.header.code != 0) {
                    System.out.println("发生错误，错误码为：" + myJsonParse.header.code);
                    System.out.println("本次请求的sid为：" + myJsonParse.header.sid);
                    webSocket.close(1000, "");
                    latch.countDown();
                    return;
                }
                List<Text> textList = myJsonParse.payload.choices.text;
                for (Text temp : textList) {
                    answer.append(temp.content);
                }
                if (myJsonParse.header.status == 2) {
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                latch.countDown();
            }
        });

        // 等待 WebSocket 交互完成
        latch.await();

        return answer.toString();
    }

    private String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());

        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";

        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);

        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();

        return httpUrl.toString();
    }

    class JsonParse {
        Header header;
        Payload payload;
    }

    class Header {
        int code;
        int status;
        String sid;
    }

    class Payload {
        Choices choices;
    }

    class Choices {
        List<Text> text;
    }

    class Text {
        String role;
        String content;
    }
}