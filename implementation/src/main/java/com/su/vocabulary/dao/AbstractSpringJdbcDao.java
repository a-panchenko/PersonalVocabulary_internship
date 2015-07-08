package com.su.vocabulary.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Provides methods for interacting with database  
 * @author alexandr.panchenko
 * @version 1.0
 * @since 2015-05-13
 */
public abstract class AbstractSpringJdbcDao<T> implements GenericDao<T> {
	/**
	 * Object which executes SQL queries or updates, initiating iteration over
	 *           ResultSets and catching JDBC exceptions
	 */
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	
	/**
	 * Returns SQL String for inserting object into database
	 * @return String SQL query for inserting object
	 */
	protected abstract String getInsertSQL();
	
	/**
	 * Returns SQL String for selecting object from database by id
	 * @return String SQL query for selecting object
	 */
	protected abstract String getSelectByIdSQL();
	
	/**
	 * Returns SQL String for selecting object from database by
	 *           some String property
	 * @return String SQL query for selecting object
	 */
	protected abstract String getSelectByValueSQL();
	
	/**
	 * Returns SQL String for selecting list of objects from database
	 * @return String SQL query for selecting objects
	 */
	protected abstract String getSelectAllSQL();
	
	/**
	 * Returns SQL String for updating object
	 * @return String SQL query for updating object
	 */
	protected abstract String getUpdateSQL();
	
	/**
	 * Returns SQL String for deleting object
	 * @return String SQL query for deleting
	 */
	protected abstract String getDeleteSQL();
	
	/**
	 * Returns SQL String for detecting count of the same objects
	 * @return
	 */
	protected abstract String getCountOfExistSQL();
	
	/**
	 * Returns arguments for prepared statement used in a query for isInstanceExists method.
	 * @param instance instance to be verified
	 * @return array of arguments of Object type
	 */
	protected abstract Object[] getIsInstanceExistsArgs(T instance);
	
	/**
	 * Returns arguments for prepared statement used in a query for create method.
	 * @param object instance to be created in database
	 * @return array of arguments of Object type
	 */
	protected abstract Object[] getCreateArgs(T object);
	
	/**
	 * Returns arguments for prepared statement used in a query for update method
	 * @param object
	 * @return
	 */
	protected abstract Object[] getUpdateArgs(T object);

	/**
	 * Returns RowMapper for specified class
	 * @return RowMapper instance
	 */	
	protected abstract RowMapper<T> getRowMapper();
	
	@Override
	public T create(T object) {
		jdbcTemplate.update(getInsertSQL(), getCreateArgs(object));
		return object;
	}
	
	@Override
	public T getById(int id) {
		T object = jdbcTemplate.queryForObject(getSelectByIdSQL(), new Object[]{id}, getRowMapper());
		return object;
	}
	
	@Override
	public T getByValue(String value) {
		T object = jdbcTemplate.queryForObject(getSelectByValueSQL(), new Object[]{value}, getRowMapper());
		return object;
	}
	
	@Override
	public List<T> getAll() {
		List<T> list = jdbcTemplate.query(getSelectAllSQL(), getRowMapper());
		return list;
	}
	
	@Override
	public void update(T object) {
		jdbcTemplate.update(getUpdateSQL(), getUpdateArgs(object));
	}
	
	@Override
	public void delete(int id) {
		jdbcTemplate.update(getDeleteSQL(), id);
	}
		
	@Override
	public boolean isInstanceExists(T instance) {
		int count = jdbcTemplate.queryForObject(getCountOfExistSQL(), getIsInstanceExistsArgs(instance), Integer.class);
		return count != 0;
	}
}
