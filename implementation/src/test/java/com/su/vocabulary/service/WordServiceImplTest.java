package com.su.vocabulary.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;

import com.su.vocabulary.dao.GenericDao;
import com.su.vocabulary.dao.ContentDao;
import com.su.vocabulary.domain.User;
import com.su.vocabulary.domain.Word;
import com.su.vocabulary.service.exceptions.UserNotFoundException;
import com.su.vocabulary.service.exceptions.WordAlreadyExistsException;
import com.su.vocabulary.service.exceptions.WordNotFoundException;

public class WordServiceImplTest {
    private static WordServiceImpl service;
    private static ContentDao<Word> wordDao;
    private static GenericDao<User> userDao;

    @Before
    public void setUp() throws Exception {
        wordDao = mock(ContentDao.class);
        userDao = mock(GenericDao.class);
        service = new WordServiceImpl();
        service.setWordDao(wordDao);
        service.setUserDao(userDao);
    }
    
    
    @After
    public void tearDown() throws Exception {
        service = null;
        wordDao = null;
        userDao = null;
    }

    // getWord

    @Test(expected = WordNotFoundException.class)
    public void testGetWordInvalidId1() throws WordNotFoundException {
        service.getWord(0);
    }

    @Test(expected = WordNotFoundException.class)
    public void testGetWordInvalidId2() throws WordNotFoundException {
        service.getWord(-1);
    }

    @Test(expected = DataAccessException.class)
    public void testGetWordDbException() throws WordNotFoundException {
        int wordId = 1;
        when(wordDao.getById(wordId)).thenThrow(
                new DataRetrievalFailureException("DB exception"));
        service.getWord(wordId);
    }

    @Test
    public void testGetWordSuccessful() throws WordNotFoundException {
        int wordId = 2;
        String wordName = "Word";
        Word word = Word.newBuilder().setWord(wordName).build();
        when(wordDao.getById(wordId)).thenReturn(word);
        Word result = service.getWord(wordId);
        assertEquals(result, word);
    }

    // getAll

    @Test(expected = UserNotFoundException.class)
    public void testGetAllInvalidId1() throws UserNotFoundException {
        service.getAll(0);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetAllInvalidId2() throws UserNotFoundException {
        service.getAll(-1);
    }

    @Test(expected = DataAccessException.class)
    public void testGetAllDbException() throws UserNotFoundException {
        int userId = 1;
        when(wordDao.getAll(userId)).thenThrow(
                new DataRetrievalFailureException("DB exception"));
        service.getAll(userId);
    }

    @Test
    public void testGetAllSuccessful() throws UserNotFoundException {
        int userId = 2;
        List<Word> words = new ArrayList<Word>();
        when(wordDao.getAll(userId)).thenReturn(words);
        List<Word> result = service.getAll(userId);
        assertEquals(result, words);
    }

    // createWord

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWordInvalidUserId() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = -1;
        Word word = Word.newBuilder().setUserId(userId).build();
        service.createWord(word);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWordNullWord() throws UserNotFoundException,
            WordAlreadyExistsException {
        service.createWord(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWordNullWordValue() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 2;
        Word word = Word.newBuilder().setWord(null).setUserId(userId).build();
        when(userDao.getById(userId)).thenReturn(
                new User(userId, "email@com", "password"));        
        service.createWord(word);
    }

    @Test(expected = WordAlreadyExistsException.class)
    public void testCreateWordAlreadyExists() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 2;
        String wordName = "Word";
        Word word = Word.newBuilder().setWord(wordName).setUserId(userId).build();
        when(userDao.getById(userId)).thenReturn(
                new User(userId, "email@com", "password"));
        when(wordDao.isInstanceExists(word)).thenReturn(true);
        service.createWord(word);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateWordUserNotExist() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 2;
        String wordName = "Word";
        Word word = Word.newBuilder().setWord(wordName).setUserId(userId).build();
        when(userDao.getById(userId)).thenReturn(null);
        service.createWord(word);
    }

    @Test(expected = DataAccessException.class)
    public void testCreateWordDbException() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 2;
        int wordId = 1;
        String wordName = "Word";
        String translation = "translation";
        String description = "description";
        String examples = "examples";
        Word word = Word.newBuilder().setWord(wordName).setWordId(wordId)
                .setUserId(userId).setTranslation(translation)
                .setDescription(description).setExample(examples).build();
        when(userDao.getById(userId)).thenReturn(
                new User(userId, "email@com", "password"));
        when(wordDao.isInstanceExists(word)).thenReturn(false);
        when(wordDao.create(word)).thenThrow(
                new DataRetrievalFailureException("DB exception"));
        service.createWord(word);
    }

    @Test
    public void testCreateWordSuccessful() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 2;
        String wordName = "Word";
        String translation = "translation";
        String description = "description";
        String examples = "examples";
        Word word = Word.newBuilder().setWord(wordName).setUserId(userId)
                .setTranslation(translation).setDescription(description)
                .setExample(examples).build();
        Word returnedWord = Word.newBuilder().setWord("Returned word").build();
        
        when(userDao.getById(userId)).thenReturn(
                new User(userId, "email@com", "password"));
        when(wordDao.isInstanceExists(word)).thenReturn(false);
        when(wordDao.create(word)).thenReturn(returnedWord);
        
        Word result = service.createWord(word);
        assertEquals(result, returnedWord);
    }
    
    // updateWord

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWordNullWord() throws UserNotFoundException,
            WordAlreadyExistsException {
        service.updateWord(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWordNullWordValue() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 2;
        Word word = Word.newBuilder().setWord(null).setUserId(userId).build();
        when(userDao.getById(userId)).thenReturn(
                new User(userId, "email@com", "password")); 
        service.updateWord(word);
}

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWordUserNotFound() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = -1;
        Word word = Word.newBuilder().setWord("Word").setUserId(userId).build();
        service.updateWord(word);
    }

    @Test(expected = WordAlreadyExistsException.class)
    public void testUpdateWordAlreadyExists() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 1;
        int wordId = 1;
        int otherWordId = 2;
        String wordName = "Word";
        Word word = Word.newBuilder().setWord(wordName).setUserId(userId).setWordId(wordId).build();
        Word wordInDb = Word.newBuilder().setWord(wordName).setUserId(userId).setWordId(otherWordId).build();
        when(userDao.getById(userId)).thenReturn(
                new User(userId, "email@com", "password"));
        when(wordDao.getByValue(wordName, userId)).thenReturn(wordInDb);
        when(wordDao.isInstanceExists(word)).thenReturn(true);
        service.updateWord(word);
    }

    @Test(expected = DataAccessException.class)
    public void testUpdateWordDbException() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 1;
        int wordId = 1;
        String wordName = "Word";
        String translation = "translation";
        String description = "description";
        String examples = "examples";
        Word word = Word.newBuilder().setWord(wordName).setUserId(userId)
                .setWordId(wordId).setTranslation(translation)
                .setDescription(description).setExample(examples).build();
        when(userDao.getById(userId)).thenReturn(
                new User(userId, "email@com", "password"));
        when(wordDao.isInstanceExists(word)).thenReturn(false);
        doThrow(new DataRetrievalFailureException("DB exception"))
                .when(wordDao).update(word);
        service.updateWord(word);
    }

    @Test
    public void testUpdateWordSuccessful() throws UserNotFoundException,
            WordAlreadyExistsException {
        int userId = 2;
        String wordName = "Word";
        Word word = Word.newBuilder().setWord(wordName).setUserId(userId).build();
        when(userDao.getById(userId)).thenReturn(
                new User(userId, "email@com", "password"));
        when(wordDao.isInstanceExists(word)).thenReturn(true);
        when(wordDao.getByValue(wordName, userId)).thenReturn(word);
        doNothing().when(wordDao).update(word);
        service.updateWord(word);

    }

    // removeWord

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWordInvalidWordId1() throws WordNotFoundException {
        service.removeWord(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWordInvalidWordId2() throws WordNotFoundException {
        service.removeWord(0);
    }

    @Test(expected = DataAccessException.class)
    public void testRemoveWordDbException() throws WordNotFoundException {
        int wordId = 2;
        doThrow(new DataRetrievalFailureException("DB exception"))
                .when(wordDao).delete(wordId);
        service.removeWord(wordId);
    }

    @Test
    public void testRemoveWordSuccessful() throws WordNotFoundException {
        int wordId = 2;
        doNothing().when(wordDao).delete(wordId);
        service.removeWord(wordId);
    }

}
