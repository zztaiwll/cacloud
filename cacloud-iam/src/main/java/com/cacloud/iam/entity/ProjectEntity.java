package com.cacloud.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
* @program: cacloud
*
* @description: ${description}
*
* @author: duanfei
*
* @create: 2023-06-18
**/
/**
    * 项目表
    */
@Data
@TableName("iam_project")
public class ProjectEntity implements Serializable {
    private Long id;

    /**
    * 所属租户id
    */
    private Long accountId;

    /**
    * 项目名称
    */
    private String name;

    /**
    * 继承的项目id，不存在上级则为0
    */
    private Long parentId;

    /**
    * 创建时间
    */
    private LocalDateTime createdTime;

    /**
    * 更新时间
    */
    private LocalDateTime updatedTime;

    /**
    * 创建人
    */
    private String createdBy;

    /**
    * 更新人
    */
    private String updatedBy;

    /**
    * 删除状态(1:已删除，0:未删除)
    */
    private int deleted;

    private static final long serialVersionUID = 1L;
}