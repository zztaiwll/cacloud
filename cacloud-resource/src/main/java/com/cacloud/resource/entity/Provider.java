package com.cacloud.resource.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
* @program: cacloud
*
* @description: ${description}
*
* @author: duanfei
*
* @create: 2023-06-20
**/
/**
    * 资源提供商表
    */
@ApiModel(value="com-cacloud-resource-entity-Provider")
@Data
@AllArgsConstructor
public class Provider implements Serializable {
    @ApiModelProperty(value="")
    private Long id;

    /**
    * 资源提供商名称
    */
    @ApiModelProperty(value="资源提供商名称")
    private String name;

    /**
    * 资源提供商类型 0 启朔云 1 火山云 2 华为云
    */
    @ApiModelProperty(value="资源提供商类型 0 启朔云 1 火山云 2 华为云")
    private Boolean type;

    /**
    * 云机所属业务 ID(火山云)
    */
    @ApiModelProperty(value="云机所属业务 ID(火山云)")
    private String productId;

    /**
    * ak
    */
    @ApiModelProperty(value="ak")
    private String ak;

    /**
    * sk （需加密）
    */
    @ApiModelProperty(value="sk （需加密）")
    private String sk;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createdTime;

    /**
    * 更新时间
    */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updatedTime;

    /**
    * 创建人
    */
    @ApiModelProperty(value="创建人")
    private String createdBy;

    /**
    * 更新人
    */
    @ApiModelProperty(value="更新人")
    private String updatedBy;

    /**
    * 删除状态(1:已删除，0:未删除)
    */
    @ApiModelProperty(value="删除状态(1:已删除，0:未删除)")
    private Boolean deleted;

    private static final long serialVersionUID = 1L;
}