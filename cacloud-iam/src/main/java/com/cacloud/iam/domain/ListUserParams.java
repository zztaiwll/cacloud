package com.cacloud.iam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@Data
@ApiModel(value = "用户列表请求")
public class ListUserParams implements Serializable {

    @ApiModelProperty(value = "分页参数", required = true)
    private PageParam pageParam;


}
