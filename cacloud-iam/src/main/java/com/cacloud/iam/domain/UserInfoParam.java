package com.cacloud.iam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @program: cacloud
 * @description: ${description}
 * @author: duanfei
 * @create: 2023-06-18
 **/

/**
 * 用户表
 */
@Data
@ApiModel(value = "用户详情")
public class UserInfoParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属租户id")
    private Long accountId;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "密码 （需加密）")
    private String password;
    @ApiModelProperty(value = "用户类型 0 根用户 1 用户 2 内部运营账户")
    private Integer usertype;
    @ApiModelProperty(value = "ak")
    private String ak;
    @ApiModelProperty(value = "sk （需加密）")
    private String sk;
    @ApiModelProperty(value = "企业名称")
    private String compnayName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "手机号码")
    private String phone;
    @ApiModelProperty(value = "是否启用状态1:启用0:禁用")
    private Integer status;
    @ApiModelProperty(value = "上一次修改密码的时间")
    private LocalDateTime passwordChangedTime;
}