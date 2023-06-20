package com.cacloud.iam.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cacloud.common.utils.R;
import com.cacloud.iam.constant.UserTypeEnums;
import com.cacloud.iam.domain.CreateUserParam;
import com.cacloud.iam.domain.UserInfoParam;
import com.cacloud.iam.entity.AccountEntity;
import com.cacloud.iam.entity.ProjectEntity;
import com.cacloud.iam.entity.UserEntity;
import com.cacloud.iam.manager.UserManager;
import com.cacloud.iam.service.AccountService;
import com.cacloud.iam.service.ProjectService;
import com.cacloud.iam.service.UserService;
import java.time.LocalDateTime;
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
        // 查询用户信息
        UserEntity userResult = userService.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUserName, username).last("limit 1"));

        if (ObjectUtils.isEmpty(userResult)) {
            return R.error(400,"登录用户：" + username + " 不存在");
        }

        if (!password.equals(userResult.getPassword())) {
            return R.error(400,"登录用户：" + username + " 密码错误");
        }
        return R.ok();
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
