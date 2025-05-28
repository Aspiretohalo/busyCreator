package com.busymango.busymangoBackend.core.strategy.strategies.pageCounter;

import com.busymango.busymangoBackend.core.strategy.PageCounterStrategy;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// XLSX策略
public class XlsxPageCounterStrategy implements PageCounterStrategy {
    @Override
    public int getCount(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            try (Workbook workbook = WorkbookFactory.create(fis)) {
                // 假设每个工作表只有一页
                return workbook.getNumberOfSheets();
            }
        }
    }
}