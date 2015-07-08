package com.su.vocabulary.dao;

import java.util.List;

/**
 * Extends generic DAO interface; used for access to user content in database.
 *
 * @author julia.denysova
 * @version 1.0
 * @since 2015-06-03
 * @param <T>
 *            application domain class
 */
public interface ContentDao<T> extends GenericDao<T> {
    /**
     * Gets data of a certain user from database.
     *
     * @param userId
     *            user id in a database
     * @return list of objects of <T> type
     */
    List<T> getAll(int userId);

    /**
     * Returns object of type T from database by specified value and userId.
     *
     * @param value
     *          String value uniquely defining object in database for a
     *            certain user
     * @param userId
     *            user id in database
     * @return object of T type
     */
    T getByValue(String value, int userId);
    
}
