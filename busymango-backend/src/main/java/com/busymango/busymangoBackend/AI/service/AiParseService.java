package com.busymango.busymangoBackend.AI.service;


import com.busymango.busymangoBackend.AI.model.dto.IdeaGenerateRequest;
import com.busymango.busymangoBackend.AI.model.vo.AIParseVO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AiParseService {
    AIParseVO parseIdeasByFile(MultipartFile file);

    SseEmitter generateIdea(IdeaGenerateRequest ideaGenerateRequest);
}
