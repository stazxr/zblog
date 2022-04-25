package com.github.stazxr.zblog.core.base;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.Field;

/**
 * BaseEntity
 *
 * @author SunTao
 * @since 2020-11-15
 */
@Slf4j
@Getter
@Setter
public class BaseEntity extends Model<BaseEntity> {
	/**
	 * 创建用户
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createUser;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createTime;

	/**
	 * 创建日期
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createDate;

	/**
	 * 修改用户
	 */
	@TableField(fill = FieldFill.UPDATE)
	private String updateUser;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private String updateTime;

	/**
	 * 乐观锁配置
	 */
	@Version
	private Integer version;

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		Field[] fields = this.getClass().getDeclaredFields();
		try {
			for (Field f : fields) {
				f.setAccessible(true);
				builder.append(f.getName(), f.get(this)).append("\n");
			}
		} catch (Exception e) {
			log.error("entity toString builder catch an error", e);
			return "toString builder catch an error";
		}
		return builder.toString();
	}
}
