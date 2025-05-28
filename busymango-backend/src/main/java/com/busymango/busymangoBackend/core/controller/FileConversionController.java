package com.busymango.busymangoBackend.core.controller;

import com.busymango.busymangoBackend.core.common.BaseResponse;
import com.busymango.busymangoBackend.core.common.ResultUtils;
import com.busymango.busymangoBackend.core.model.entity.FileInfo;
import com.busymango.busymangoBackend.core.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@Slf4j
public class FileConversionController {

    private final FileService fileService;

    @Autowired
    public FileConversionController(FileService fileService) {
        this.fileService = fileService;
    }

    // 文件上传接口
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 存储上传的文件到D:\000app文件夹
            String storagePath = "D:\\000app\\test\\";
            String originalFilename = file.getOriginalFilename();
            File targetFile = new File(storagePath + originalFilename);
            file.transferTo(targetFile);

            // 获取文件的额外信息
            long fileSize = file.getSize();
            String fileType = file.getContentType();

            // 将文件信息存储到数据库
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(originalFilename);
            fileInfo.setFilePath(targetFile.getAbsolutePath());
            fileInfo.setUploadTime(new java.util.Date());
            fileInfo.setFileSize(fileSize);
            fileInfo.setFileType(fileType);
            // 假设有一个用户ID
            fileInfo.setUserId(1); // 这里需要根据实际情况设置用户ID

            fileService.save(fileInfo);

            // 返回文件的存储路径
            return ResponseEntity.ok("文件上传成功: " + targetFile.getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public BaseResponse<List<FileInfo>> listFileInfo() {
        return ResultUtils.success(fileService.list());
    }

    // 文件预览接口
    @GetMapping("/preview")
    public BaseResponse<String> previewFile(Integer id) throws Exception {
        return ResultUtils.success(fileService.previewFile(id));

    }

    // 获取文件扩展名
    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return index > 0 ? fileName.substring(index + 1) : "";
    }
}
