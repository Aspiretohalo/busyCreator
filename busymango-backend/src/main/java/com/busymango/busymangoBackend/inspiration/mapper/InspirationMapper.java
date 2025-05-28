package com.busymango.busymangoBackend.inspiration.mapper;

import com.busymango.busymangoBackend.inspiration.model.entity.Inspiration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.inspiration.model.vo.InspirationVO;
import com.busymango.busymangoBackend.task.model.vo.TaskVO;

import java.util.List;

/**
* @author Admin
* @description 针对表【bcmcreate_inspiration(灵感表)】的数据库操作Mapper
* @createDate 2025-03-23 13:57:54
* @Entity com.busymango.busymangoBackend.inspiration.model.entity.Inspiration
*/
public interface InspirationMapper extends BaseMapper<Inspiration> {

    List<InspirationVO> listInspirationByType(String type);
    List<InspirationVO> listInspiration();
}




