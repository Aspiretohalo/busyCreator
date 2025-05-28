package com.busymango.busymangoBackend.core.strategy.strategies.pageCounter;

import com.busymango.busymangoBackend.core.strategy.PageCounterStrategy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// DOC和DOCX策略
public class DocPageCounterStrategy implements PageCounterStrategy {
    @Override
    public int getCount(File file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(Files.newInputStream(file.toPath()))) {
            int count = 0;
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                if (!paragraph.getText().trim().isEmpty()) {
                    count++;
                }
            }
            return count;
        }
    }
}