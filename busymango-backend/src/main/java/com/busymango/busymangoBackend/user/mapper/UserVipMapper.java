package com.busymango.busymangoBackend.user.mapper;

import com.busymango.busymangoBackend.user.model.entity.UserVip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 95788
 * @description 针对表【user_vip(会员信息表)】的数据库操作Mapper
 * @createDate 2024-10-28 01:47:36
 * @Entity com.busymango.busymangoBackend.user.model.entity.UserVip
 */
public interface UserVipMapper extends BaseMapper<UserVip> {
    UserVip selectVipByUserId(@Param("userId") Long userId);

}




