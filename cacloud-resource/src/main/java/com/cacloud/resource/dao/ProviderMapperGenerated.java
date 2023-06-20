package com.cacloud.resource.dao;

import com.cacloud.resource.entity.Provider;

/**
* @program: cacloud
*
* @description: ${description}
*
* @author: duanfei
*
* @create: 2023-06-20
**/
public interface ProviderMapperGenerated {
    int deleteByPrimaryKey(Long id);

    int insert(Provider record);

    int insertSelective(Provider record);

    Provider selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Provider record);

    int updateByPrimaryKey(Provider record);
}