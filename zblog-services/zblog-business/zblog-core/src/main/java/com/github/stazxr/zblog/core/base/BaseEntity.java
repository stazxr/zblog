package com.github.stazxr.zblog.core.base;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.github.stazxr.zblog.core.util.ToStringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 公共实体类，包含基本的创建和修改信息。
 * 该类可以作为其他实体类的基类，提供通用字段。
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@Getter
@Setter
@ApiModel("公共实体")
public class BaseEntity extends Model<BaseEntity> {
	private static final long serialVersionUID = 5802079762879978466L;

	/**
	 * 创建用户，存储该实体的创建用户标识。
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(hidden = true)
	private Long createUser;

	/**
	 * 创建时间，存储该实体的创建时间。
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(hidden = true)
	private String createTime;

	/**
	 * 修改用户，存储最后修改该实体的用户标识。
	 */
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(hidden = true)
	private Long updateUser;

	/**
	 * 修改时间，存储该实体的最后修改时间。
	 */
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(hidden = true)
	private String updateTime;

	/**
	 * 乐观锁版本号，用于处理并发修改。
	 */
	@Version
	@ApiModelProperty(hidden = true)
	private Integer version;

	@Override
	public String toString() {
		return ToStringUtils.buildString(this);
	}
}
