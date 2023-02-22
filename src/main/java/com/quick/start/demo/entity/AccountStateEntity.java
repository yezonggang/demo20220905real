package com.quick.start.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yzg
 * @since 2023-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account_state")
@ApiModel(value="AccountStateEntity对象", description="")
public class AccountStateEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("userid")
    private String userid;

    @TableField("account_non_expired")
    private Integer accountNonExpired;

    @TableField("account_non_locked")
    private Integer accountNonLocked;

    @TableField("credentials_non_expired")
    private Integer credentialsNonExpired;

    @TableField("enabled")
    private Integer enabled;


}
