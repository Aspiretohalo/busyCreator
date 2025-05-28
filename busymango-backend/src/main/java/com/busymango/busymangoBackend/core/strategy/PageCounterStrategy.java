package com.busymango.busymangoBackend.core.strategy;

import java.io.File;
import java.io.IOException;

// 策略接口
public interface PageCounterStrategy {
    int getCount(File file) throws IOException;
}
