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
    * 资源提供商节点表
    */
@ApiModel(value="com-cacloud-resource-entity-Edge")
@Data
@AllArgsConstructor
public class Edge implements Serializable {
    @ApiModelProperty(value="")
    private Integer id;

    /**
    * 资源提供商id
    */
    @ApiModelProperty(value="资源提供商id")
    private Integer resourceProviderId;

    /**
    * 区域
    */
    @ApiModelProperty(value="区域")
    private String region;

    /**
    * URI
    */
    @ApiModelProperty(value="URI")
    private String uri;

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