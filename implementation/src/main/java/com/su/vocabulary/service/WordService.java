package com.su.vocabulary.service;

import java.util.List;

import com.su.vocabulary.domain.Word;
import com.su.vocabulary.service.exceptions.UserNotFoundException;
import com.su.vocabulary.service.exceptions.WordAlreadyExistsException;
import com.su.vocabulary.service.exceptions.WordNotFoundException;

/**
 * An interface is used to process requests related with user's words.
 * @author julia.denysova
 * @version 1.0
 * @since 2015-06-03
 */
public interface WordService {
    /**
     * Retrieves word from storage.
     *
     * @param wordId
     *            unique word id in storage
     * @return word object
     * @throws WordNotFoundException
     *             when word id doesn't meet id requirements
     * @throws DataAccessException
     *             in case if issues occur while retrieving word from storage
     */
    Word getWord(int wordId) throws WordNotFoundException;

    /**
     * Retrieves all words of a certain user from storage.
     *
     * @param userId
     *            unique user id in storage
     * @return list of word objects
     * @throws UserNotFoundException
     *             when user with specified id doesn't exist in storage
     * @throws DataAccessException
     *             in case issues occur while retrieving word from storage
     */
    List<Word> getAll(int userId) throws UserNotFoundException;

    /**
     * Inserts word into storage.
     *
     * @param word
     *             word object
     * @return added word
     * @throws UserNotFoundException
     *             when user with specified id doesn't exist in storage
     * @throws WordAlreadyExistsException
     *             if word name already exists in storage for the specified userId
     * @throws DataAccessException
     *             in case issues occur while retrieving word from storage
     * @throws IllegalArgumentException
     *             if word object is null or word value is null or user id doesn't meet id requirements (<1)
     */
    Word createWord(Word word) throws UserNotFoundException,
            WordAlreadyExistsException;

    /**
     * Updates existing word with new information.
     *
     * @param word
     *            word object
     * @throws UserNotFoundException
     *             when user_id doesn't meet id requirements (<1) or user with
     *             specified id doesn't exist in storage
     * @throws WordAlreadyExistsException
     *             if word name already exists in for the userId specified in
     *             word
     * @throws DataAccessException
     *             in case issues occur while accessing storage
     * @throws IllegalArgumentException
     *             if word object is null or word value is null or user id
     *             doesn't meet id requirements (<1)
     */
    void updateWord(Word word) throws UserNotFoundException,
            WordAlreadyExistsException;

    /**
     * Removes word with a specified word id from storage
     *
     * @param wordId
     * @throws IllegalArgumentException if word id doesn't meet id requirements (<1)
     * @throws DataAccessException
     *             in case issues occur while accessing storage
     */
    void removeWord(int wordId) throws WordNotFoundException;

}
