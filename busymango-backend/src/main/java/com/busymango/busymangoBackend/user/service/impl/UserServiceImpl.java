package com.busymango.busymangoBackend.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.core.common.ErrorCode;
import com.busymango.busymangoBackend.core.constant.CommonConstant;
import com.busymango.busymangoBackend.core.exception.BusinessException;
import com.busymango.busymangoBackend.core.utils.DateUtil;
import com.busymango.busymangoBackend.core.utils.SqlUtils;
import com.busymango.busymangoBackend.project.service.ProjectService;
import com.busymango.busymangoBackend.user.mapper.UserMapper;
import com.busymango.busymangoBackend.user.mapper.UserVipMapper;
import com.busymango.busymangoBackend.user.model.dto.UserQueryRequest;
import com.busymango.busymangoBackend.user.model.entity.User;
import com.busymango.busymangoBackend.user.model.entity.UserVip;
import com.busymango.busymangoBackend.user.model.enums.UserRoleEnum;
import com.busymango.busymangoBackend.user.model.vo.LoginUserVO;
import com.busymango.busymangoBackend.user.model.vo.UserProjectCountVO;
import com.busymango.busymangoBackend.user.model.vo.UserVO;
import com.busymango.busymangoBackend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.busymango.busymangoBackend.user.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 *
 * @author caoyanghalo@qq.com
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserVipMapper userVipMapper;
    @Resource
    @Lazy
    private ProjectService projectService;

//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "bcMango";

    @Override
    public long userRegister(String userAccount, String userPassword, String confirmPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, confirmPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || confirmPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(confirmPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_account", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserAvatar("https://files-1317662942.cos.ap-nanjing.myqcloud.com/fruit/watermelon.png");
            user.setRole("user");
            user.setSex("0");
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);

        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 3. 记录用户的登录态(生成jwt)
        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("userAccount", userAccount);
                put("expireTime", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
            }
        };

        user.setJwt(JWTUtil.createToken(map, "xlxlxlxlxlxlxlxlxlxlxlxlxlxlxlxlxlxl".getBytes()));
//        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return user;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        String token = request.getHeader("Authorization").split(" ")[1];
        try {
            // 验证token的有效性
            JWTUtil.verify(token, SALT.getBytes());
        } catch (Exception e) {
            // 如果验证失败，可以在这里处理，例如返回null或者抛出一个自定义异常
            System.out.println("Token验证失败: " + e.getMessage());
            return null;
        }

        JWT jwt = JWTUtil.parseToken(token);
        jwt.getHeader(JWTHeader.TYPE);
        String userAccount = (String) jwt.getPayload("userAccount");

        if (userAccount == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        User currentUser;
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
//        if (redisTemplate.opsForValue().get(userAccount) != null) {
//            currentUser = (User) redisTemplate.opsForValue().get(userAccount);
//        } else {
        currentUser = userMapper.selectUserByAccount(userAccount);
//            redisTemplate.opsForValue().set(userAccount, currentUser, 1, TimeUnit.HOURS);
//        }

        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);

        // 把会员信息一同查询返回
        UserVip userVip = userVipMapper.selectVipByUserId(user.getId());
        // 是vip，且会员未到期
        if (userVip != null &&
                (userVip.getExpirationdate() == null // 无限期
                        || userVip.getExpirationdate().after(new Date()))) {
            Long vipId = userVip.getId();
            loginUserVO.setVipId(vipId);
        }

        UserProjectCountVO userProjectCountVO = projectService.getCount();
        loginUserVO.setUserProjectCountVO(userProjectCountVO);

        long daysSinceJoin = this.calculateDaysSinceJoin(user.getCreatedAt());
        loginUserVO.setJoinDays(daysSinceJoin);
        return loginUserVO;
    }

    public long calculateDaysSinceJoin(Date createdAt) {
        // 假设 user.getCreatedAt() 返回的是 Date 类型
        return DateUtil.calculateDaysSinceJoin(createdAt);
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public List<User> listUsersByIds(List<Long> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userMapper.listUsersByIds(userList);
    }

    @Override
    public UserVO getUsersById(Long userId) {
        return userMapper.getUsersById(userId);
    }
}
