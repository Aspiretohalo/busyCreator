package com.busymango.busymangoBackend.core.service;

import com.busymango.busymangoBackend.core.model.dto.interfaceAccess.InterfaceAccessDTO;
import com.busymango.busymangoBackend.core.model.entity.InterfaceAccess;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 95788
* @description 针对表【bcmcreate_interface_access(用户接口访问情况表)】的数据库操作Service
* @createDate 2025-02-12 17:27:22
*/
public interface InterfaceAccessService extends IService<InterfaceAccess> {

    void saveRequest(InterfaceAccessDTO interfaceAccessDTO);
}
