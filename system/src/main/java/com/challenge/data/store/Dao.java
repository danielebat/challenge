package com.challenge.data.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.challenge.data.model.IdentityObject;

/**
 * SuperClass to handle common requests and store data
 */
public class Dao<T extends IdentityObject> {
	
	protected Map<Integer, T> entities;
	private Integer id;
	
	public Dao() {
		this.entities = new ConcurrentHashMap<Integer, T>();
		this.id = 0;
	}
	
	public synchronized Integer add(T entity) {
		id++;
		entity.setId(id);
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
