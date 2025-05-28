package com.busymango.busymangoBackend.core.strategy.context;


import com.busymango.busymangoBackend.core.strategy.PreviewStrategy;

import java.io.IOException;
import java.io.InputStream;

// 上下文环境类
public class PreviewContext {
    private PreviewStrategy strategy;

    public PreviewContext(PreviewStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PreviewStrategy strategy) {
        this.strategy = strategy;
    }

    public String preview(String inputFilePath) throws IOException {
        return strategy.preview(inputFilePath);
    }
}
