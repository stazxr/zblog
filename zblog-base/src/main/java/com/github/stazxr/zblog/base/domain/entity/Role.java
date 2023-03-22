package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import com.github.stazxr.zblog.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * 系统角色
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
@Setter
@TableName("role")
@ApiModel("角色实体")
public class Role extends BaseEntity implements GrantedAuthority {
    /**
     * 角色id
     */
    @TableId
    @ApiModelProperty("角色id")
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField(value = "`DESC`")
    @ApiModelProperty("角色描述")
    private String desc;

    /**
     * 角色状态
     */
    @ApiModelProperty(value = "角色状态", example = "true")
    private Boolean enabled;

    /**
     * 是否删除
     */
    @TableLogic
    @ApiModelProperty("是否删除")
    private Boolean deleted;

    /**
     * 返回角色编码
     *
     * @return roleCode
     */
    @Override
    public String getAuthority() {
        return roleCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Role other = (Role) obj;
        return !StringUtils.isEmpty(roleCode) && roleCode.equalsIgnoreCase(other.getRoleCode());
    }

    @Override
    public String toString() {
        return getAuthority();
    }
}
