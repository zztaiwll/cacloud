package com.cacloud.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cacloud.common.exception.RRException;
import com.cacloud.common.utils.StringUtils;
import com.cacloud.iam.dao.UserDao;
import com.cacloud.iam.domain.ChangeUserPasswordParams;
import com.cacloud.iam.entity.UserEntity;
import com.cacloud.iam.service.UserService;
import com.cacloud.ibatis.common.utils.PageUtils;
import com.cacloud.ibatis.common.utils.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;


@Service("UserService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Override
    public void disableUser(List<Long> ids, Integer state) {
        if (ids != null && !ids.isEmpty()) {
            List<UserEntity> userEntities = new ArrayList<>();
            ids.forEach(id -> {
                UserEntity userEntity = new UserEntity();
                userEntity.setStatus(state);
                userEntity.setId(id);
                userEntities.add(userEntity);
            });
            updateBatchById(userEntities, 30);
        }

    }

    @Override
    public void changePassword(ChangeUserPasswordParams changeUserPasswordParams) {
        if (changeUserPasswordParams == null
                || changeUserPasswordParams.getId() == null
                || StringUtils.isEmpty(changeUserPasswordParams.getOldPassword())
                || StringUtils.isEmpty(changeUserPasswordParams.getNewPassword()) ) {
            throw new RRException("请填写完整", 400);
        }

        UserEntity user = getById(changeUserPasswordParams.getId());
        if (user == null) {
            throw new RRException("用户没有找到", 400);
        }
        if(!changeUserPasswordParams.getOldPassword().equals(user.getPassword())){
            throw new RRException("密码错误", 400);
        }
        user.setPassword(changeUserPasswordParams.getNewPassword());
        updateById(user);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<>()
        );
        return new PageUtils(page);
    }

}