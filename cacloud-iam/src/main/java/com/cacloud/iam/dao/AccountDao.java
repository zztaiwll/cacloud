package com.cacloud.iam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cacloud.iam.entity.AccountEntity;

/**
* @program: cacloud
*
* @description: ${description}
*
* @author: duanfei
*
* @create: 2023-06-18
**/
public interface AccountDao extends BaseMapper<AccountEntity> {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountEntity record);

    int insertSelective(AccountEntity record);

    AccountEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountEntity record);

    int updateByPrimaryKey(AccountEntity record);
}