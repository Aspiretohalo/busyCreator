package com.busymango.busymangoBackend.core.mapper;

import com.busymango.busymangoBackend.core.model.dto.interfaceAccess.InterfaceAccessDTO;
import com.busymango.busymangoBackend.core.model.entity.InterfaceAccess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 95788
* @description 针对表【bcmcreate_interface_access(用户接口访问情况表)】的数据库操作Mapper
* @createDate 2025-02-12 17:27:22
* @Entity com.busymango.busymangoBackend.core.model.entity.InterfaceAccess
*/
public interface InterfaceAccessMapper extends BaseMapper<InterfaceAccess> {

    void saveRequest(InterfaceAccessDTO interfaceAccessDTO);
}




