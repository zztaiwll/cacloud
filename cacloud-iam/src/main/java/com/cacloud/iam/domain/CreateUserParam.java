package com.cacloud.iam.domain;

import com.baomidou.mybatisplus.annotation.TableName;
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

@Data
@ApiModel(value = "创建用户请求")
public class CreateUserParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "密码 （需加密）")
    private String password;
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
}