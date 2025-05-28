package com.busymango.busymangoBackend.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.core.model.dto.interfaceAccess.InterfaceAccessDTO;
import com.busymango.busymangoBackend.core.model.entity.InterfaceAccess;
import com.busymango.busymangoBackend.core.service.InterfaceAccessService;
import com.busymango.busymangoBackend.core.mapper.InterfaceAccessMapper;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 95788
 * @description 针对表【bcmcreate_interface_access(用户接口访问情况表)】的数据库操作Service实现
 * @createDate 2025-02-12 17:27:22
 */
@Service
public class InterfaceAccessServiceImpl extends ServiceImpl<InterfaceAccessMapper, InterfaceAccess>
        implements InterfaceAccessService {
    @Resource
    private InterfaceAccessMapper interfaceAccessMapper;

    @Override
    public void saveRequest(InterfaceAccessDTO interfaceAccessDTO) {
        UserDTO userDTO = UserContext.getUserDTO();
        if (userDTO == null) return;
        interfaceAccessDTO.setUserId(userDTO.getUserId());
        interfaceAccessMapper.saveRequest(interfaceAccessDTO);
    }
}




