package com.su.vocabulary.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.su.vocabulary.dao.GenericDao;
import com.su.vocabulary.dao.ContentDao;
import com.su.vocabulary.domain.User;
import com.su.vocabulary.domain.Word;
import com.su.vocabulary.service.exceptions.UserNotFoundException;
import com.su.vocabulary.service.exceptions.WordAlreadyExistsException;
import com.su.vocabulary.service.exceptions.WordNotFoundException;

public class WordServiceImpl implements WordService {
    /** DAO object to work with words in storage. */
    private ContentDao<Word> wordDao;

    /** DAO object to work with users in storage. */
    private GenericDao<User> userDao;

    private static final Logger logger = Logger
            .getLogger(WordServiceImpl.class);

    /**
     * Sets ContentDao<Word> object to wordDao field through autowiring.
     *
     * @param dao
     *            ContentDao<Word> object
     */
    @Autowired
    @Qualifier("wordDao")
    public void setWordDao(ContentDao<Word> dao) {
        wordDao = dao;
    }

    /**
     * Sets GenericDao<User> object to userDao field through autowiring.
     *
     * @param dao
     */
    @Autowired
    @Qualifier("userDao")
    public void setUserDao(GenericDao<User> dao) {
        userDao = dao;
    }

    @Override
    public Word getWord(int wordId) throws WordNotFoundException {
        if (wordId < 1) {
            logger.error("getWord error: incorrect word id");
            throw new WordNotFoundException("Word id can't be less than 1!");
        }
        return wordDao.getById(wordId);
    }

    @Override
    public List<Word> getAll(int userId) throws UserNotFoundException {
        if (userId < 1) {
            logger.error("getAll error: incorrect user id");
            throw new UserNotFoundException("Word id can't be less than 1!");
        }
        return wordDao.getAll(userId);
    }

    @Override
    public Word createWord(Word word) throws UserNotFoundException,
            WordAlreadyExistsException {
        if (word == null) {
            logger.error("createWord error: null argument");
            throw new IllegalArgumentException("Null Word argument!");
        }
        int userId = word.getUserId();
        if (userId < 1) {
            logger.error("createWord error: incorrect user id");
            throw new IllegalArgumentException("User id can't be less then 1!");
        }
        User user = userDao.getById(userId);
        if (user == null) {
            logger.error("createWord error: user with userId=" + userId
                    + " doesn't exist");
            throw new UserNotFoundException("User doesn't exist!");
        }

        if (word.getWord() == null) {
            throw new IllegalArgumentException("Word value is null!");
        }

        if (wordDao.isInstanceExists(word)) {
            logger.error("createWord error: word " + word.getWord()
                    + " already exists");
            throw new WordAlreadyExistsException("Word already exists!");
        }
     
        return wordDao.create(word);
        
    }

    @Override
    public void updateWord(Word word) throws UserNotFoundException,
            WordAlreadyExistsException {
        if (word == null) {
            logger.error("updateWord error: null parameter");
            throw new IllegalArgumentException("Null Word argument!");
        }

        if (word.getWord() == null) {
            logger.error("updateWord error: null word value");
            throw new IllegalArgumentException("Word value is null!");
        }
        int userId = word.getUserId();
        if (userId < 1) {
            logger.error("updateWord error: incorrect user id");
            throw new IllegalArgumentException("User id can't be less then 1!");
        }
        User user = userDao.getById(userId);

        if (user == null) {
            logger.error("updateWord error: user with id=" + userId + " doesn't exist");
            throw new UserNotFoundException("User doesn't exist!");
        }
        if (wordDao.isInstanceExists(word)) {
            int wordIdForUpdatedName = wordDao.getByValue(word.getWord(), word.getUserId()).getWordId();
            if (wordIdForUpdatedName != word.getWordId()) {
                logger.error("updateWord error: word " + word.getWord() + " alreadyExists");
                throw new WordAlreadyExistsException("Word already exists!");
            }
        }

        wordDao.update(word);        
    }

    @Override
    public void removeWord(int wordId) throws WordNotFoundException {
        if (wordId < 1) {
            logger.error("removeWord error: incorrect word id");
            throw new IllegalArgumentException("Word id can't be less than 1!");
        }
        wordDao.delete(wordId);
    }

//    private boolean checkForNulls(Word word) {
//        if (word.getDescription() == null) {
//            return true;
//        }
//        if (word.getExample() == null) {
//            return true;
//        }
//        if (word.getTranslation() == null) {
//            return true;
//        }
//        return false;
//    }

//    private Word createNoNullsWord(Word word) {
//        Word.Builder builder = Word.newBuilder();
//        builder.setWordId(word.getWordId());
//        builder.setUserId(word.getUserId());
//        builder.setWord(word.getWord());
//        if (word.getSynonyms() != null) {
//            builder.setSynonyms(word.getSynonyms());
//        }
//
//        if (word.getDescription() == null) {
//            builder.setDescription("");
//        } else {
//            builder.setDescription(word.getDescription());
//        }
//
//        if (word.getExample() == null) {
//            builder.setExample("");
//        } else {
//            builder.setExample(word.getExample());
//        }
//
//        if (word.getTranslation() == null) {
//            builder.setTranslation("");
//        } else {
//            builder.setTranslation(word.getTranslation());
//        }
//        return builder.build();
//    }

}
