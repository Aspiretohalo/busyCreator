package com.busymango.busymangoBackend.core.strategy.strategies.pageCounter;

import com.busymango.busymangoBackend.core.strategy.PageCounterStrategy;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// PPT和PPTX策略
public class PptPageCounterStrategy implements PageCounterStrategy {
    @Override
    public int getCount(File file) throws IOException {
        try (XMLSlideShow presentation = new XMLSlideShow(Files.newInputStream(file.toPath()))) {
            return presentation.getSlides().size();
        }
    }
}