package com.challenge.data.store;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Dao<T> {
	
	protected Map<String, T> entities;
	
	public Dao() {
		this.entities = new ConcurrentHashMap<String, T>();
	}
	
	protected Collection<T> findAll() {
		return entities.values();
	}
	
	protected abstract T findById(String id);

}
