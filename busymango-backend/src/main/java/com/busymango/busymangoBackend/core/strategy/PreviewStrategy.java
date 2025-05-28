package com.busymango.busymangoBackend.core.strategy;

import java.io.IOException;
import java.io.InputStream;

// 策略接口
public interface PreviewStrategy {
    String preview(String inputFilePath) throws IOException;

}
