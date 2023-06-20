package com.cacloud.resource.dao;

import com.cacloud.resource.entity.Edge;

/**
* @program: cacloud
*
* @description: ${description}
*
* @author: duanfei
*
* @create: 2023-06-20
**/
public interface EdgeMapperGenerated {
    int deleteByPrimaryKey(Integer id);

    int insert(Edge record);

    int insertSelective(Edge record);

    Edge selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Edge record);

    int updateByPrimaryKey(Edge record);
}