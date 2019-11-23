package com.challenge.data.store;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Dao<T> {
	
	protected Map<Integer, T> entities;
	private Integer id;
	
	public Dao() {
		this.entities = new ConcurrentHashMap<Integer, T>();
		this.id = 0;
	}
	
	public synchronized Collection<T> findAll() {
		return entities.values();
	}
	
	public synchronized Integer add(T entity) {
		id++;
		entities.put(id, entity);
		return id;
	}
	
	public synchronized T findById(Integer id) {
		return entities.get(id);
	}
	
	public synchronized T remove(Integer id) {
		return entities.remove(id);
	}

}
