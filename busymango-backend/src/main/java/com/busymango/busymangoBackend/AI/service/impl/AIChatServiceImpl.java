package com.busymango.busymangoBackend.AI.service.impl;

import com.busymango.busymangoBackend.AI.model.dto.AIQuestionRequest;
import com.busymango.busymangoBackend.AI.service.AIChatService;
import com.busymango.busymangoBackend.AI.service.AiAnswerService;
import com.busymango.busymangoBackend.AI.service.AiQuestionService;
import com.busymango.busymangoBackend.AI.service.AiReasoningService;
import com.busymango.busymangoBackend.inspiration.model.enums.ContentTypeEnum;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AIChatServiceImpl implements AIChatService {

    private final AiQuestionService aiQuestionService;
    private final AiAnswerService aiAnswerService;
    private final AiReasoningService aiReasoningService;
    private final ThreadPoolTaskExecutor aiTaskExecutor;
    // 用于存储最终回答
    private StringBuilder finalAnswer = new StringBuilder();
    // 用于存储最终思维链
    private StringBuilder finalReasoning = new StringBuilder();
    @Value("${deepseek.api.key}")
    private String APIKEY;
    @Value("${deepseek.api.base-url}")
    private String BASEURL;
    @Value("${deepseek.model}")
    private String MODEL;

    @Autowired
    public AIChatServiceImpl(AiQuestionService aiQuestionService, AiAnswerService aiAnswerService, AiReasoningService aiReasoningService, ThreadPoolTaskExecutor aiTaskExecutor) {
        this.aiQuestionService = aiQuestionService;
        this.aiAnswerService = aiAnswerService;
        this.aiReasoningService = aiReasoningService;
        this.aiTaskExecutor = aiTaskExecutor;
    }

    @Override
    public SseEmitter getAnswer(AIQuestionRequest aiQuestionRequest) {
        UserDTO userDTO = UserContext.getUserDTO();

        // 存储问题
        Long questionId = aiQuestionService.saveQuestion(aiQuestionRequest);
        log.info("AI提问存储情况：questionId = {}", questionId);
        String fullQuestion;
        // 使用基本提示词，让模型按要求格式回复
//        String fullQuestion = "请使用markdown的语法格式回复，但是不要加```markdown的标签。" + aiQuestionRequest.getQuestion();
        if (aiQuestionRequest.getType() == null) {
            fullQuestion = "请根据以下内容与要求，生成相应的内容：" + aiQuestionRequest.getQuestion();
        } else {
            switch (ContentTypeEnum.fromType(aiQuestionRequest.getType())) {
                case NOVEL:
                    log.info("小说");
                    fullQuestion = "请根据以下的案例、风格等要求生成精彩的小说：" + aiQuestionRequest.getQuestion();
                    break;
                case AD:
                    log.info("广告");
                    fullQuestion = "请根据以下的要求生成吸引人的广告文案：" + aiQuestionRequest.getQuestion();
                    break;
                case NEWS:
                    log.info("新闻");
                    fullQuestion = "请根据以下的要点生成新闻报道，要求真实可靠，并且吸引眼球：" + aiQuestionRequest.getQuestion();
                    break;
                case OUTLINE:
                    log.info("评论");
                    fullQuestion = "请根据以下的内容生成一些用户真实评论，预期要像真人：" + aiQuestionRequest.getQuestion();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported content type: " + aiQuestionRequest.getType());
            }
        }

        // 创建 SseEmitter 对象
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // 使用自定义的 TaskExecutor 执行异步任务
        CompletableFuture.runAsync(() -> {
            try {
                // 调用 Python 脚本并处理流式响应
                processScript(fullQuestion, aiQuestionRequest.getContext(), emitter);

                // 在流式响应完成后，存储回答
                Long answerId = aiAnswerService.saveAnswer(questionId, finalAnswer.toString(), userDTO);
                // 在流式响应完成后，存储思维链
                Long reasoningId = aiReasoningService.saveReasoning(questionId, finalAnswer.toString(), userDTO);
                log.info("AI回答存储情况：answerId = {}", answerId);
                log.info("AI思维链存储情况：reasoningId = {}", reasoningId);
                // 清空 finalAnswer
                finalAnswer.setLength(0);
                finalReasoning.setLength(0);
                // 完成事件
                emitter.complete();
            } catch (Exception e) {
                log.error("Error in SSE processing", e);
                emitter.completeWithError(e);
            }
        }, aiTaskExecutor);
        return emitter;
    }

    private void processScript(String question, String context, SseEmitter emitter) throws Exception {
        // 移除换行符
        if (question != null && question.length() > 0) {
            question = question.replace("\n", "");
        }
        if (context != null && context.length() > 0) {
            context = context.replace("\n", "");
        }
        // 构建请求 URL 和参数
        String apiUrl = BASEURL;
        String apiKey = APIKEY; // 替换为你的 API Key
        String model = MODEL; // 替换为你的模型名称

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setDoOutput(true);

        // 构建请求体
        String requestBody = String.format(
                "{\"model\": \"%s\", \"messages\": [{\"role\": \"system\", \"content\": \"%s\"},{\"role\": \"user\", \"content\": \"%s\"}], \"stream\": true}",
                model, context, question
        );

        log.info(requestBody);
        // 发送请求体
        connection.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));

        // 检查响应状态码
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            String errorLine;
            StringBuilder errorResponse = new StringBuilder();
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine).append("\n");
            }
            errorReader.close();
            throw new IOException("Unexpected response code: " + responseCode + "\n" + errorResponse);
        }

        // 读取响应流
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line;

        while ((line = reader.readLine()) != null) {
//            log.info("line:{}", line);
            log.info("line:{}", line);

            // 去掉 "data: " 前缀
            if (line.startsWith("data: ")) {
                line = line.substring(6);
            }
//            log.info("line:{}", line);

            // 检查是否是 JSON 格式
            if (line.trim().isEmpty() || line.equals("[DONE]")) {
                if (line.equals("[DONE]")) {
                    log.info("Stream completed.");
                }
                continue; // 跳过空行或 [DONE]
            }

            // 解析 JSON 数据
            try {
                JSONObject jsonObject = new JSONObject(line);
                JSONArray choices = jsonObject.getJSONArray("choices");
                if (choices.length() > 0) {
                    JSONObject choice = choices.getJSONObject(0);
                    JSONObject delta = choice.getJSONObject("delta");

                    // 提取 content 或 reasoning_content
                    if (delta.has("reasoning_content") && !delta.isNull("reasoning_content")) {
                        String reasoningContent = delta.getString("reasoning_content").replace("\\n", "\n");
                        finalReasoning.append(reasoningContent);
                        emitter.send(reasoningContent, MediaType.TEXT_PLAIN);
                    } else if (delta.has("content") && !delta.isNull("content")) {
                        String content = delta.getString("content").replace("\\n", "\n");
                        finalAnswer.append(content);
                        emitter.send(content, MediaType.TEXT_PLAIN);
                    }
                }
            } catch (JSONException e) {
                log.error("Error parsing JSON: " + line, e);
            }
        }

        reader.close();
        connection.disconnect();
    }

    private void processPythonScript(String question, String context, SseEmitter emitter) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "D:\\000app\\busyCreator\\bcmAI\\main.py", question, context);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

        String line;
        while ((line = reader.readLine()) != null) {
            log.info("line:{}", line);
            if (line != null && !line.isEmpty()) {
                // 将内容发送给前端
                emitter.send(SseEmitter.event().data(line));
                // 同时追加到最终回答
                finalAnswer.append(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.error("Python script exited with code: {}", exitCode);
        }
    }

    private void processSparkScript(String question, String context, SseEmitter emitter) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "D:\\000app\\busyCreator\\bcmAI\\main.py", question, context);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

        String line;
        while ((line = reader.readLine()) != null) {
            if (isJson(line)) {
                // 解析 JSON 数据并提取 content 字段
                String content = extractContentFromJson(line);
                log.info("line:{}", line);
                if (content != null && !content.isEmpty()) {
                    // 将内容发送给前端
                    emitter.send(SseEmitter.event().data(content));
                    // 同时追加到最终回答
                    finalAnswer.append(content);
                }
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.error("Python script exited with code: {}", exitCode);
        }
    }

    /**
     * 判断字符串是否是有效的 JSON 格式
     */
    private boolean isJson(String input) {
        try {
            JsonParser.parseString(input).getAsJsonObject();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String extractContentFromJson(String json) {
        try {
            // 使用 JSON 解析库（如 Gson）提取 content 字段
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonObject data = jsonObject.getAsJsonObject("data");
            return data.has("content") ? data.get("content").getAsString() : null;
        } catch (Exception e) {
            log.error("Error parsing JSON: {}", json, e);
            return null;
        }
    }

}