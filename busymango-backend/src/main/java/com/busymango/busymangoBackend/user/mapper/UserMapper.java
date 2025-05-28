package com.busymango.busymangoBackend.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.busymango.busymangoBackend.user.model.entity.User;
import com.busymango.busymangoBackend.user.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据库操作
 *
 * @author caoyanghalo@qq.com
 */
public interface UserMapper extends BaseMapper<User> {
    User selectUserByAccount(@Param("userAccount") String userAccount);
    List<User> listUsersByIds(List<Long> userIds);

    UserVO getUsersById(Long userId);
}




