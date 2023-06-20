package com.cacloud.iam.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cacloud.redis.common.config.RedisService;
import com.cacloud.common.constant.CacheConstants;
import com.cacloud.common.constant.SecurityConstants;
import com.cacloud.common.utils.IdUtils;
import com.cacloud.common.utils.JwtUtils;
import com.cacloud.common.utils.R;
import com.cacloud.common.utils.StringUtils;
import com.cacloud.iam.constant.UserTypeEnums;
import com.cacloud.iam.domain.CreateUserParam;
import com.cacloud.iam.entity.AccountEntity;
import com.cacloud.iam.entity.ProjectEntity;
import com.cacloud.iam.entity.UserEntity;
import com.cacloud.iam.manager.UserManager;
import com.cacloud.iam.service.AccountService;
import com.cacloud.iam.service.ProjectService;
import com.cacloud.iam.service.UserService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: cacloud
 * @description: 用户管理类
 * @author: duanfei
 * @create: 2023-06-18
 **/
@Slf4j
@Service
public class UserManagerImpl implements UserManager {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    /**
     * 1. 创建账号
     * 2. 创建默认根项目
     * 3. 创建根用户
     * 4. 创建账户，关联账户到当前项目
     *
     * @param userInfoParam
     */
    @Override
    public void createAnonymousUser(CreateUserParam userInfoParam) {
        log.info("iam user createAnonymousUser userInfoParam is {}", userInfoParam);
        // 1. 创建账号
        AccountEntity defaultAccount = createDefaultAccount(userInfoParam);
        //1. 创建默认项目并关联账号
        createDefaultProject(defaultAccount);
        // 3. 创建根用户
        Long userId = IdWorker.getId();
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userInfoParam, userEntity);
        userEntity.setId(userId);
        userEntity.setUsertype(UserTypeEnums.Root.getValue());
        userService.save(userEntity);
    }

    @Override
    /**
     * 登录
     */
    public R login(String username, String password) {
        log.info("iam user login username is {} password is {}", username, password);
        //验证信息是否为空
        if(StringUtils.isEmpty(username)){
            return R.error(400,"用户名不能为空");
        }
        if(StringUtils.isEmpty(password)){
            return R.error(400,"密码不能为空");
        }
        // 查询用户信息
        UserEntity userResult = userService.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUserName, username).last("limit 1"));
        if (ObjectUtils.isEmpty(userResult)) {
            return R.error(400,"登录用户：" + username + " 不存在");
        }
        if (!password.equals(userResult.getPassword())) {
            return R.error(400,"登录用户：" + username + " 密码错误");
        }
        // 接口返回信息
        Map<String, Object> rspMap = createToken(userResult);
        return R.ok(rspMap);
    }
    //使用jwt创建token
    private Map<String, Object> createToken(UserEntity loginUser)
    {
        String token = IdUtils.fastUUID();
        Long userId = loginUser.getId();
        String userName = loginUser.getUserName();
        loginUser.setId(userId);
        loginUser.setUserName(userName);
        refreshToken(loginUser,token);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }
    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;
    //更新token的redis时长和数据库里面的信息
    public void refreshToken(UserEntity loginUser,String token)
    {
        loginUser.setUpdatedTime(LocalDateTime.now());
//        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        String userKey = getTokenKey(token);
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token)
    {
        return ACCESS_TOKEN + token;
    }


    private AccountEntity createDefaultAccount(CreateUserParam userEntity) {
        Long accountId = IdWorker.getId();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountId);
        accountEntity.setName(userEntity.getUserName());
        accountEntity.setEmail(userEntity.getEmail());
        accountEntity.setPhone(userEntity.getPhone());
        accountEntity.setStatus(1);
        accountEntity.setAccessStatus(1);
        accountEntity.setPasswordChangedTime(LocalDateTime.now());
        accountEntity.setDeleted(0);
        accountEntity.setCreatedTime(LocalDateTime.now());
        accountEntity.setUpdatedTime(LocalDateTime.now());
        accountEntity.setCreatedBy("");
        accountEntity.setUpdatedBy("");
        accountService.save(accountEntity);
        return accountEntity;
    }

    private ProjectEntity createDefaultProject(AccountEntity accountEntity) {
        Long projectId = IdWorker.getId();
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);
        projectEntity.setAccountId(accountEntity.getId());
        projectEntity.setName(accountEntity.getName());
        projectEntity.setParentId(0L);
        projectEntity.setCreatedTime(LocalDateTime.now());
        projectEntity.setUpdatedTime(LocalDateTime.now());
        projectEntity.setCreatedBy("");
        projectEntity.setUpdatedBy("");
        projectEntity.setDeleted(0);
        projectService.save(projectEntity);
        return projectEntity;
    }
}
