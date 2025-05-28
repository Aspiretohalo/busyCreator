package com.busymango.busymangoBackend.inspiration.service;

import com.busymango.busymangoBackend.inspiration.model.entity.Inspiration;
import com.baomidou.mybatisplus.extension.service.IService;
import com.busymango.busymangoBackend.inspiration.model.vo.InspirationVO;
import com.busymango.busymangoBackend.task.model.vo.TaskVO;

import java.util.List;

/**
* @author Admin
* @description 针对表【bcmcreate_inspiration(灵感表)】的数据库操作Service
* @createDate 2025-03-23 13:57:55
*/
public interface InspirationService extends IService<Inspiration> {

    List<InspirationVO> listInspiration(String inspiration);
}
