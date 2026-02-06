package com.github.stazxr.zblog.bas.cache;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 缓存接口
 *
 * @param <K> 键类型
 * @param <V> 值类型
 *
 * @author SunTao
 * @since 2024-08-21
 */
public interface Cache<K, V> {
	/**
	 * 获取缓存值
	 *
	 * @param key 缓存键
	 * @return 缓存值，不存在返回 null
	 */
	V get(K key);

	/**
	 * 放入缓存
	 *
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 过期时间
	 * @param unit 时间单位
	 */
	void put(K key, V value, long timeout, TimeUnit unit);

	/**
	 * 删除缓存
	 *
	 * @param key 缓存键
	 */
	void remove(K key);

	/**
	 * 仅当缓存中不存在指定 key 时，才写入缓存值（原子语义）。
	 *
	 * <p><b>语义说明：</b></p>
	 * <ul>
	 * <li>当 key 不存在或已过期时，写入缓存并返回 true</li>
	 * <li>当 key 已存在时，不进行覆盖并返回 false</li>
	 * </ul>
	 *
	 * <p><b>并发语义：</b></p>
	 * <ul>
	 * <li>该操作必须保证原子性，避免并发情况下重复写入</li>
	 * <li>分布式缓存通常通过 Redis SET NX 等原子指令实现</li>
	 * <li>本地缓存通常通过 key 级锁或 CAS 机制实现</li>
	 * </ul>
	 *
	 * <p><b>典型使用场景：</b></p>
	 * <ul>
	 * <li>接口幂等控制</li>
	 * <li>分布式锁实现</li>
	 * <li>防止缓存击穿</li>
	 * <li>任务或消息去重处理</li>
	 * </ul>
	 *
	 * <p><b>过期策略：</b></p>
	 * <ul>
	 * <li>timeout &gt; 0 时设置过期时间</li>
	 * <li>timeout &lt;= 0 时表示不过期（具体行为由实现决定）</li>
	 * </ul>
	 *
	 * @param key 缓存键
	 * @param value 要写入的缓存值
	 * @param timeout 过期时间
	 * @param unit 时间单位
	 * @return
	 * <ul>
	 * <li>true：写入成功（key 原本不存在）</li>
	 * <li>false：key 已存在，未写入</li>
	 * </ul>
	 */
	Boolean setIfAbsent(K key, V value, long timeout, TimeUnit unit);

	/**
	 * 获取缓存中的值；当缓存不存在或已过期时，通过加载器加载数据并写入缓存。
	 *
	 * <p><b>语义说明：</b></p>
	 * <ul>
	 *   <li>优先从缓存中读取数据</li>
	 *   <li>若缓存未命中，则调用 {@code loader} 加载数据</li>
	 *   <li>加载成功后，将结果写入缓存，并设置指定的过期时间</li>
	 * </ul>
	 *
	 * <p><b>并发语义：</b></p>
	 * <ul>
	 *   <li>在高并发场景下，缓存实现应尽量避免多个线程同时触发 {@code loader}</li>
	 *   <li>推荐通过本地锁、分布式锁或原子操作等方式防止缓存击穿</li>
	 * </ul>
	 *
	 * <p><b>空值处理：</b></p>
	 * <ul>
	 *   <li>当 {@code loader} 返回 {@code null} 时，具体行为由实现决定：</li>
	 *   <li>可选择不写入缓存，或进行空值缓存（以防缓存穿透）</li>
	 * </ul>
	 *
	 * <p><b>异常处理：</b></p>
	 * <ul>
	 *   <li>{@code loader} 抛出的异常应直接向上抛出</li>
	 *   <li>异常情况下不应写入缓存</li>
	 * </ul>
	 *
	 * <p><b>典型使用场景：</b></p>
	 * <pre>{@code
	 * User user = cache.getOrLoad(
	 *     userId,
	 *     () -> userService.findById(userId),
	 *     10,
	 *     TimeUnit.MINUTES
	 * );
	 * }</pre>
	 *
	 * @param key 缓存键
	 * @param loader 数据加载器，用于在缓存未命中时获取数据
	 * @param timeout 缓存过期时间
	 * @param unit 过期时间单位
	 * @return 缓存中的值或加载得到的值；若加载结果为空则返回 {@code null}
	 */
	V getOrLoad(K key, Supplier<V> loader, long timeout, TimeUnit unit);
}
