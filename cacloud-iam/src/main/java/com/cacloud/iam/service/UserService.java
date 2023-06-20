package com.cacloud.iam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cacloud.iam.domain.ChangeUserPasswordParams;
import com.cacloud.iam.entity.UserEntity;
import com.cacloud.ibatis.common.utils.PageUtils;
import java.util.List;
import java.util.Map;

/**
 * 账户
 *
 * @author 段飞
 */
public interface UserService extends IService<UserEntity> {


    void disableUser(List<Long> ids, Integer state);

    void changePassword(ChangeUserPasswordParams userEntity);

    PageUtils queryPage(Map<String, Object> params);
}

