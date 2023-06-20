package com.cacloud.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * 账户表
 */
@Data
@TableName("iam_account")
public class AccountEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 账户名称
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 是否启用状态1:启用0:禁用
     */
    private int status;
    /**
     * 是否认证 状态1:认证0:未认证
     */
    private int accessStatus;
    /**
     * 上一次修改密码的时间
     */
    private LocalDateTime passwordChangedTime;
    /**
     * 删除状态(1:已删除，0:未删除)
     */
    private int deleted;
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
}