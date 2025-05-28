package com.busymango.busymangoBackend.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.core.model.entity.FileInfo;

import java.io.IOException;

/**
 * 帖子服务
 *
 * @author caoyanghalo@qq.com
 */
public interface FileService extends IService<FileInfo> {

    String previewFile(Integer id) throws IOException;

}
