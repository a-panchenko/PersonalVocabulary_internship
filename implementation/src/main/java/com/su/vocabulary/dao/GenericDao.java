package com.su.vocabulary.dao;

import java.util.List;

/**
 * Describes an abstract interface to some
 *           type of database
 * @author alexandr.panchenko
 * @version 1.0
 * @since 2015-05-13
 */
public interface GenericDao<T> {
	/**
	 * Inserts object into database
	 * @param object
	 *           instance which should be written into database
	 * @return object which were written into database 
	 */
	T create(T object);
	
	/**
	 * Returns object from database by object's id
	 * @param id
	 *           unique identifier of an object in storage;
	 * @return object which were written into database
	 */
	T getById(int id);
	
	/**
	 * Returns object from database by object's String property
	 * @param value
	 *           String object's property
	 * @return object from database with specified String property
	 *          
	 */
	T getByValue(String value);
	
	/**
	 * Returns list of objects from database
	 * @return list of objects from database
	 */
	List<T> getAll();
	
	/**
	 * Updates specified object
	 * @param object which should be updated
	 */
	void update(T object);
	
	/**
	 * Deletes object with specified id from database
	 * @param id
	 *           unique identifier of an object in storage;
	 */
	void delete(int id);
	
	/**
	 * Checks whether instance already exist in database
	 * @param instance
	 *           object to checking; should be unique
	 * @return true if object exists and false if it doesn't
	 */
	boolean isInstanceExists(T instance);	
}
