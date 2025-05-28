package com.busymango.busymangoBackend.core.strategy.strategies.preview;


import com.busymango.busymangoBackend.core.strategy.PreviewStrategy;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class OtherPreviewStrategy implements PreviewStrategy {
    /**
     * 将文件转换为Base64编码的字符串。
     *
     * @param inputFilePath 文件路径
     * @return 文件内容的Base64编码字符串
     * @throws IOException 如果读取文件时发生错误
     */
    public String preview(String inputFilePath) throws IOException {
        File file = new File(inputFilePath);
        if (!file.exists()) {
            throw new IOException("File does not exist: " + inputFilePath);
        }

        // 使用Files.readAllBytes读取文件为字节数组
        byte[] fileContent = Files.readAllBytes(file.toPath());

        // 使用Base64编码字节数组
        String base64Encoded = Base64.getEncoder().encodeToString(fileContent);
        // 返回Base64编码字符串
        return "data:application/vnd.openxmlformats-officedocument.presentationml.presentation;base64," + base64Encoded;
    }
}
