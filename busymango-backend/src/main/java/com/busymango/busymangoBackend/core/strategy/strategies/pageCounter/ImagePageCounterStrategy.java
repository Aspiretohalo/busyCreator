package com.busymango.busymangoBackend.core.strategy.strategies.pageCounter;


import com.busymango.busymangoBackend.core.strategy.PageCounterStrategy;

import java.io.File;

// 图片策略
public class ImagePageCounterStrategy implements PageCounterStrategy {
    @Override
    public int getCount(File file) {
        // 假设每个图像文件只有一页
        return 1;
    }
}
