package com.cacloud.iam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cacloud
 * @description:
 * @author: duanfei
 * @create: 2023-06-20
 **/
@Data
@ApiModel(value = "分页参数")
public class PageParam {
    @ApiModelProperty(value = "第几页", required = true)
    private Integer page;
    @ApiModelProperty(value = "每页限制条数", required = true)
    private Integer limit;
}
