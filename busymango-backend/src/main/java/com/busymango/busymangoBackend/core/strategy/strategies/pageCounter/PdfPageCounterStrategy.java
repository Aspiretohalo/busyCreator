package com.busymango.busymangoBackend.core.strategy.strategies.pageCounter;

import com.busymango.busymangoBackend.core.strategy.PageCounterStrategy;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PdfPageCounterStrategy implements PageCounterStrategy {
    @Override
    public int getCount(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            return document.getNumberOfPages();
        }
    }
}
