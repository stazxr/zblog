package com.github.stazxr.zblog.bas.cache;

import java.io.Serializable;

/**
 * 缓存接口
 *
 * @param <K> 键类型
 * @param <V> 值类型
 *
 * @author SunTao
 * @since 2024-08-21
 */
public interface Cache<K, V> extends Serializable {
	/**
	 * 返回缓存对象的默认失效时长，单位为毫秒。
	 * 值为 {@code 0} 表示未设置失效时长。
	 *
	 * @return 默认失效时长，单位为毫秒
	 */
	long timeout();

	/**
	 * 将对象加入缓存，使用默认失效时长。
	 *
	 * @param key 键
	 * @param value 要缓存的对象
	 * @see #put(Object, Object, long)
	 */
	void put(K key, V value);

	/**
	 * 将对象加入缓存，使用指定的失效时长。
	 *
	 * @param key 键
	 * @param value 要缓存的对象
	 * @param timeout 失效时长，单位为毫秒
	 */
	void put(K key, V value, long timeout);

	/**
	 * 从缓存中获取对象。如果对象不存在或已过期，则返回 {@code null}。
	 *
	 * @param key 键
	 * @return 与键关联的对象，若不存在或已过期则返回 {@code null}
	 */
	V get(K key);

	/**
	 * 从缓存中移除对象。
	 *
	 * @param key 键
	 */
	void remove(K key);

	/**
	 * 清空缓存中的所有对象。
	 */
	void clear();

	/**
	 * 检查缓存中是否包含指定的键。
	 *
	 * @param key 要检查的键
	 * @return 如果缓存中包含指定的键，则返回 {@code true}，否则返回 {@code false}
	 */
	boolean containsKey(K key);
}

