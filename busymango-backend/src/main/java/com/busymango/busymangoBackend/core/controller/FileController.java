package com.busymango.busymangoBackend.core.controller;

import cn.hutool.core.io.FileUtil;
import com.busymango.busymangoBackend.AI.service.AiParseService;
import com.busymango.busymangoBackend.core.common.BaseResponse;
import com.busymango.busymangoBackend.core.common.ErrorCode;
import com.busymango.busymangoBackend.core.common.ResultUtils;
import com.busymango.busymangoBackend.core.constant.FileConstant;
import com.busymango.busymangoBackend.core.exception.BusinessException;
import com.busymango.busymangoBackend.core.manager.CosManager;
import com.busymango.busymangoBackend.core.model.dto.file.UploadFileRequest;
import com.busymango.busymangoBackend.core.model.enums.FileUploadBizEnum;

import java.io.File;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busymango.busymangoBackend.user.model.entity.User;
import com.busymango.busymangoBackend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Resource
    private AiParseService aiParseService;
    @Value("${cos.client.region}")
    private String cosClientRegion;
    @Value("${cos.client.bucket}")
    private String cosClientBucket;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<Object> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        String biz = uploadFileRequest.getBiz();
        /**
         * 当业务为 PARSE_IDEAS解析创意 或者 PARSE_FILES解析文件 时，直接跳转到解析业务代码，无需上传到cos
         */
        if (FileUploadBizEnum.PARSE_IDEAS.getValue().equals(biz) || FileUploadBizEnum.PARSE_FILES.getValue().equals(biz)) {
            return ResultUtils.success(aiParseService.parseIdeasByFile(multipartFile));
        }
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success("https://" + cosClientBucket + ".cos." + cosClientRegion + ".myqcloud.com" + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
