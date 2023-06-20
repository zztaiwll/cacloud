package com.cacloud.iam.entity;

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

/**
 * 用户表
 */
@Data
@ApiModel(value = "用户详情信息")
@TableName("iam_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;
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
    @ApiModelProperty(value = "删除状态(1:已删除，0:未删除)")
    private Integer deleted;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedTime;
    @ApiModelProperty(value = "创建人")
    private String createdBy;
    @ApiModelProperty(value = "更新人")
    private String updatedBy;
}