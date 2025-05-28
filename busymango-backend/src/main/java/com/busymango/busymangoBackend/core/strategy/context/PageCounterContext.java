package com.busymango.busymangoBackend.core.strategy.context;


import com.busymango.busymangoBackend.core.strategy.PageCounterStrategy;

import java.io.File;
import java.io.IOException;

// 上下文环境类
public class PageCounterContext {
    private PageCounterStrategy strategy;

    public PageCounterContext(PageCounterStrategy strategy) {
        this.strategy = strategy;
    }

    public int getPageCount(File file) throws IOException {
        return strategy.getCount(file);
    }

    public void setStrategy(PageCounterStrategy strategy) {
        this.strategy = strategy;
    }
}
