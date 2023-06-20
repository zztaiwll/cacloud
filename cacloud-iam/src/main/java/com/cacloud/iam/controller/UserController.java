package com.cacloud.iam.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cacloud.common.utils.R;
import com.cacloud.common.utils.StringUtils;
import com.cacloud.iam.domain.ChangeUserPasswordParams;
import com.cacloud.iam.domain.CreateUserParam;
import com.cacloud.iam.domain.ListUserParams;
import com.cacloud.iam.domain.UpdateUserParam;
import com.cacloud.iam.domain.UserInfoParam;
import com.cacloud.iam.entity.UserEntity;
import com.cacloud.iam.manager.UserManager;
import com.cacloud.iam.service.UserService;
import com.cacloud.ibatis.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: cacloud
 * @description: 用户
 * @author: duanfei
 * @create: 2023-06-18
 **/

@Slf4j
@Api(tags = {"用户管理"})
@RestController
@RequestMapping("/iam/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserManager userManager;

    /**
     * 创建匿名（免实名认证）用户
     */
    @ApiOperation(value = "创建用户")
    @PostMapping("/createAnonymousUser")
    public R createAnonymousUser(@RequestBody CreateUserParam createUserParam) {
        log.info("iam user createAnonymousUser userInfoParam is {}", createUserParam);
        if (createUserParam == null
                || StringUtils.isEmpty(createUserParam.getCompnayName())
                || StringUtils.isEmpty(createUserParam.getEmail())
                || StringUtils.isEmpty(createUserParam.getPassword())
                || StringUtils.isEmpty(createUserParam.getUserName())
                || StringUtils.isEmpty(createUserParam.getPhone())
        ) {

            return R.error(400, "请填写完整！");
        }
        //TODO 需校验用户权限

        //创建创建匿名（免实名认证）用户
        userManager.createAnonymousUser(createUserParam);
        return R.ok();
    }

    //禁用用户
    @ApiOperation(value = "禁用用户")
    @PutMapping("/disableUser")
    public R disableUser(@RequestBody List<Long> ids) {
        //TODO 需校验用户权限
        log.info("iam user disableUser ids is {}", JSON.toJSON(ids));
        if (CollectionUtils.isEmpty(ids)) {
            return R.error(400, "请填写完整！");

        }
        userService.disableUser(ids, 0);
        return R.ok();
    }

    //禁用用户
    @ApiOperation(value = "启用用户")
    @PutMapping("/ableUser")
    public R ableUser(@RequestBody List<Long> ids) {
        //TODO 需校验用户权限
        log.info("iam user ableUser ids is {}", JSON.toJSON(ids));
        if (CollectionUtils.isEmpty(ids)) {
            return R.error(400, "请填写完整！");

        }
        userService.disableUser(ids, 1);
        return R.ok();
    }

    @ApiOperation(value = "重置密码")
    @PutMapping("/changePassword")
    public R changePassword(@RequestBody ChangeUserPasswordParams changeUserPasswordParams) {
        //TODO 需校验用户权限
        log.info("iam user changePassword changeUserPasswordParams is {}", JSON.toJSON(changeUserPasswordParams));
        if (changeUserPasswordParams == null
                || StringUtils.isEmpty(changeUserPasswordParams.getNewPassword())
                || StringUtils.isEmpty(changeUserPasswordParams.getOldPassword())) {
            return R.error(400, "请填写完整！");
        }
        userService.changePassword(changeUserPasswordParams);
        return R.ok();
    }

    /**
     * 用户列表
     */
    @ApiOperation(value = "用户列表")
    @GetMapping("/list")
    public R list(@RequestParam ListUserParams listUserParams) {
        if (listUserParams == null
                || listUserParams.getPageParam() == null
                || listUserParams.getPageParam().getPage() == null
                || listUserParams.getPageParam().getLimit() == null
        ) {
            return R.error(400, "请填写完整!");
        }
        Map<String, Object> params = new HashMap();
        params.put("page", listUserParams.getPageParam().getPage());
        params.put("limit", listUserParams.getPageParam().getLimit());
        log.info("iam user list params is {}", JSON.toJSON(params));
        //TODO 需校验用户权限 获取当前用户权限的用户列表

        PageUtils page = userService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 用户详细信息
     */
    @ApiOperation(value = "用户详细")
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        log.info("iam user info id is {}", id);
        if (id == null) {
            return R.error(400, "请填写完整!");
        }
        UserEntity userEntity = userService.getById(id);
        return R.ok().put("user", userEntity);
    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    // @RequiresPermissions("user:user:save")
    public R save(@RequestBody UserInfoParam userInfoParam) {
        if (userInfoParam == null
                || StringUtils.isEmpty(userInfoParam.getUserName())
                || StringUtils.isEmpty(userInfoParam.getPassword())
                || StringUtils.isEmpty(userInfoParam.getEmail())
                || StringUtils.isEmpty(userInfoParam.getCompnayName())
                || StringUtils.isEmpty(userInfoParam.getPhone())
        ) {
            return R.error(400, "请填写完整!");
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userInfoParam, userEntity);
        userService.save(userEntity);
        return R.ok();
    }

    /**
     * 修改用户
     */
    @ApiOperation(value = "修改用户")
    @PutMapping("/update")
    // @RequiresPermissions("user:user:update")
    public R update(@RequestBody UpdateUserParam userInfoParam) {
        if (userInfoParam == null || userInfoParam.getId() == null
        ) {
            return R.error(400, "请填写完整!");
        }
        log.info("iam user update userInfoParam is {}", JSON.toJSON(userInfoParam));
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userInfoParam, userEntity);
        userService.updateById(userEntity);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @PutMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
