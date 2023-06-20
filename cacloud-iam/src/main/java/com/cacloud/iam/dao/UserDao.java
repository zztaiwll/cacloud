package com.cacloud.iam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cacloud.iam.entity.AccountEntity;
import com.cacloud.iam.entity.UserEntity;

/**
* @program: cacloud
*
* @description: ${description}
*
* @author: duanfei
*
* @create: 2023-06-18
**/

public interface UserDao extends BaseMapper<UserEntity> {
    /**
     * delete by primary key
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(UserEntity record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(UserEntity record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    UserEntity selectByPrimaryKey(Integer id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(UserEntity record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(UserEntity record);
}