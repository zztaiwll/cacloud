package com.cacloud.iam.manager;

import com.cacloud.common.utils.R;
import com.cacloud.iam.domain.CreateUserParam;
import com.cacloud.iam.domain.UserInfoParam;
import com.cacloud.iam.entity.UserEntity;

/**
 * @program: cacloud
 * @description: 用户管理类
 * @author: duanfei
 * @create: 2023-06-18
 **/
public interface UserManager {

    /**
     * 创建免实名认证用户接口
     *
     * @param userInfoParam
     */
    void createAnonymousUser(CreateUserParam userInfoParam);


    R login(String userName, String password);

}
