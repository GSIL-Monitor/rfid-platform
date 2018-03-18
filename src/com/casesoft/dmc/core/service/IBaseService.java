package com.casesoft.dmc.core.service;

import java.io.Serializable;
import java.util.List;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;

/**
 * Service层的父接口，提供基本的分页，增删查改的模板函数
 * @author Administrator
 *
 * @param <T>
 * @param <PK>
 */
public interface IBaseService<T,PK extends Serializable> {

	public Page<T> findPage(Page<T> page, List<PropertyFilter> filters);
	public void save(T entity);//保存实体
	public T load(PK id);//通过id加载实体
	public T get(String propertyName, Object value);//根据某个属性值获取
	public List<T> find(List<PropertyFilter> filters);
	public List<T> getAll();
	public <X> List<X> findAll();
	public void update(T entity);
	public void delete(T entity);
	public void delete(PK id);
}
