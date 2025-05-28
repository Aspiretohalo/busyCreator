package com.busymango.busymangoBackend.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.core.mapper.FileInfoMapper;
import com.busymango.busymangoBackend.core.model.entity.FileInfo;
import com.busymango.busymangoBackend.core.service.FileService;
import com.busymango.busymangoBackend.core.strategy.context.PreviewContext;
//import com.busymango.busymangoBackend.core.strategy.strategies.preview.DocPreviewStrategy;
import com.busymango.busymangoBackend.core.strategy.strategies.preview.DocxPreviewStrategy;
import com.busymango.busymangoBackend.core.strategy.strategies.preview.OtherPreviewStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件转化服务实现
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo>
        implements FileService {
    @Resource
    private FileInfoMapper fileInfoMapper;

    @Override
    public String previewFile(Integer id) throws IOException {
        FileInfo fileInfo = fileInfoMapper.selectById(id);
        // 根据ID查询文件信息
        String filePath = "D:\\000app\\test\\";
        String fileNameWithoutExtension = fileInfo.getFileName().replaceFirst("[.][^.]+$", ""); // 替换文件扩展名
        String storagePath = "D:\\000app\\test\\out\\" + fileNameWithoutExtension + "\\";
        File directory = new File(storagePath);
        // 创建文件夹，如果文件夹不存在
        if (!directory.exists()) {
            directory.mkdirs(); // 创建文件夹，包括所有必需的父文件夹
        }
        File file = new File(filePath + fileInfo.getFileName());
        // 检查文件类型，如果是 Word 文件，则转换为 PDF
        String fileExtension = getFileExtension(fileInfo.getFileName());
        PreviewContext previewContext = new PreviewContext(null);
        switch (fileExtension) {
            case "docx":
                previewContext.setStrategy(new DocxPreviewStrategy());
                break;
//            case "doc":
//                previewContext.setStrategy(new DocPreviewStrategy());
//                break;
            default:
                previewContext.setStrategy(new OtherPreviewStrategy());
                break;
        }
        /**
         * todo 4.判断要传给策略的数据是什么
         */
        return previewContext.preview(file.getAbsolutePath());
    }
//    public ResponseEntity<String> previewFile(Integer id) throws Exception {
//        根据ID查询文件信息
//        FileInfo fileInfo = fileService.getById(id);
//        // 根据ID查询文件信息
//        String filePath = "D:\\000app\\test\\";
//        String fileNameWithoutExtension = fileInfo.getFileName().replaceFirst("[.][^.]+$", ""); // 替换文件扩展名
//        String storagePath = "D:\\000app\\test\\out\\" + fileNameWithoutExtension + "\\";
//        File directory = new File(storagePath);
//        // 创建文件夹，如果文件夹不存在
//        if (!directory.exists()) {
//            directory.mkdirs(); // 创建文件夹，包括所有必需的父文件夹
//        }
//        File file = new File(filePath + fileInfo.getFileName());
//
//        // 检查文件类型，如果是 Word 文件，则转换为 PDF
//        String fileExtension = getFileExtension(fileInfo.getFileName());
//
//        if ("docx".equalsIgnoreCase(fileExtension) || "doc".equalsIgnoreCase(fileExtension)) {
//            // 如果是 Word 文件，调用转换方法
//            String pdfFilePath = null;
//            pdfFilePath = fileService.convertWordToHtml(file.getAbsolutePath());
//            log.info(pdfFilePath);
//            return ResponseEntity.ok(pdfFilePath);  // 返回转换后的 PDF 文件路径
//        } else {
//            // 对于其他文件类型，直接返回文件路径
//            return ResponseEntity.ok("文件预览地址: " + file.getAbsolutePath());
//        }
//
//    }

    // 获取文件扩展名
    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return index > 0 ? fileName.substring(index + 1) : "";
    }
}




