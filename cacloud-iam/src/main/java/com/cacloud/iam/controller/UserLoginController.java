package com.cacloud.iam.controller;

import com.cacloud.common.utils.R;
import com.cacloud.iam.domain.UserLoginParams;
import com.cacloud.iam.manager.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"内部用户登录"})
@RestController
@RequestMapping("iam/userlogin")
public class UserLoginController {

    @Autowired
    private UserManager userManager;

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public R userLogin(@RequestBody UserLoginParams userLoginParams) {

        return userManager.login(userLoginParams.getUserName(), userLoginParams.getPassword());
    }
}
