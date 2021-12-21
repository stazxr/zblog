package com.github.stazxr.zblog.core.base;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * BaseEntity
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Getter
@Setter
public class BaseEntity extends Model<BaseEntity> {
	/**
	 * serialId
	 */
	private static final long serialVersionUID = 2313936582027156631L;

	/**
	 * 创建用户
	 */
	@TableField(fill=FieldFill.INSERT)
	private String createUser;

	/**
	 * 创建时间
	 */
	@TableField(fill=FieldFill.INSERT)
	private String createTime;

	/**
	 * 创建日期
	 */
	@TableField(fill=FieldFill.INSERT)
	private String createDate;

	/**
	 * 修改用户
	 */
	@TableField(fill=FieldFill.UPDATE)
	private String updateUser;

	/**
	 * 修改时间
	 */
	@TableField(fill=FieldFill.UPDATE)
	private String updateTime;

	/**
	 * 乐观锁配置
	 */
	@Version
	private Integer version;
}
