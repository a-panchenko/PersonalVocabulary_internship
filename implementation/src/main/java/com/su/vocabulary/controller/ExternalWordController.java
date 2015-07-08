package com.su.vocabulary.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.su.vocabulary.domain.Word;
import com.su.vocabulary.service.CustomUserDetails;
import com.su.vocabulary.service.WordService;
import com.su.vocabulary.service.exceptions.EmptyWordException;
import com.su.vocabulary.service.exceptions.SessionRecognizingException;
import com.su.vocabulary.service.exceptions.UserNotFoundException;
import com.su.vocabulary.service.exceptions.WordAlreadyExistsException;
import com.su.vocabulary.service.exceptions.WordNotFoundException;

@Controller
@RequestMapping(value = "/client")
public class ExternalWordController {
	/** Bean instance of WordService */
	@Autowired
	private WordService wordService;
	private static final Logger logger = Logger.getLogger(WordController.class);
	
	public ExternalWordController() {
		
	}
	
	//is used for ExternalWordControllerTest
	/*package*/ ExternalWordController(WordService service) {
		this.wordService = service;
	}

	/**
	 * Creates word from word details received from HTTP request.
	 *
	 * @param word
	 *            String value of word which should be created
	 * @param translation
	 *            String value of word's translation
	 * @param description
	 *            String value of word's description
	 * @param example
	 *            String value of examples of using word
	 * @param synonyms
	 *            List of word's synonyms
	 * @return String message with result of method invocation - 'Ok' or
	 *            error message
	 * @throws SessionRecognizingException
	 *            if user is not authorized
	 */
	@RequestMapping(value="/save_word", method = RequestMethod.POST)
	public @ResponseBody Object createWord(@RequestParam("word") String word,
								   @RequestParam(value = "translation", required = false) String translation,
								   @RequestParam(value = "description", required = false) String description,
								   @RequestParam(value = "example", required = false) String example,
								   @RequestParam(value = "synonyms", required = false) List<String> synonyms 
								   ) throws SessionRecognizingException {
		int userId = getUserId();
		if (word == null || word.isEmpty()) {
			logger.error("Empty word was given!");
			return "Empty word was given!";
		}

		if (userId < 1) {
			logger.error("User with id = " + userId + "doesn't exist!");
			return "User with id = " + userId +  "doesn't exist!";
			
		}
		Word.Builder wordBuilder = Word.newBuilder();
		wordBuilder
				.setUserId(userId)
				.setWord(word)
				.setTranslation(translation)
				.setDescription(description)
				.setExample(example);
		if (synonyms != null) {
		    wordBuilder.setSynonyms(synonyms);
		}
		Word builtWord = wordBuilder.build();
		Word createdWord = null;
		try {
		    createdWord = wordService.createWord(builtWord);
		} catch(DataAccessException e) {
		    logger.error("Create word error .", e);
		    return "Word saving procedure failed.";
		} catch(Exception e) {
		    logger.error("Create word error .", e);
		    return e.getMessage();
		}
		
		return createdWord.getWordId();
	}

	/**
	 * Returns all user's words
	 *
	 * @return List of words
	 * @throws SessionRecognizingException
	 *            if user is not authorized
	 */
	@RequestMapping(value="/words", method = RequestMethod.POST)
	public @ResponseBody Object getAllWords() throws SessionRecognizingException {
		int userId = getUserId();
		List<Word> words = null;
		try {
		    words = wordService.getAll(userId);
		} catch(DataAccessException e) {
		    logger.error("Get all words error. ", e);
		    return "Procedure failed.";
		} catch(Exception e) {
		    logger.error("Get all words error. ", e);
		    return e.getMessage();		    
		}
		return words;
	}

	/**
	 * Deletes word with id from HTTP request.
	 *
	 * @param wordId
	 *            Integer value of word which should be deleted
	 * @return id of word which was deleted
	 * @throws SessionRecognizingException
	 *            if user is not authorized
	 */
	@RequestMapping(value = "/delete_word", method = RequestMethod.POST)
	public @ResponseBody Object deleteWord(@RequestParam("wordId") int wordId) throws
	                              SessionRecognizingException {
		getUserId();
		try {
		    wordService.removeWord(wordId);
		} catch(DataAccessException e) {
		    logger.error("Delete word error. ", e);
		    return "Delete word procedure failed!";
		} catch (Exception e) {
		    logger.error("Delete word error. ", e);
		    return e.getMessage();
		}
		return (Integer)wordId;
	}

	/**
	 * Updates word with word details from HTTP request.
	 *
	 * @param wordId
	 *            Integer value of word which should be updated
	 * @param word
	 *            String value of word which should be created
	 * @param translation
	 *            String value of word's translation
	 * @param description
	 *            String value of word's description
	 * @param example
	 *            String value of examples of using word
	 * @param synonyms
	 *            List of word's synonyms
	 * @return String message with result of method invocation - 'Ok' or
	 *            error message
	 * @throws SessionRecognizingException
	 */
	@RequestMapping(value = "/update_word", method = RequestMethod.POST)
	public @ResponseBody String updateWord(@RequestParam("wordId") Integer wordId,
							 @RequestParam("word") String word,
							 @RequestParam(value = "translation", required = false) String translation,
							 @RequestParam(value = "description", required = false) String description,
							 @RequestParam(value = "example", required = false) String example,
							 @RequestParam(value = "synonyms", required = false) List<String> synonyms
							 ) throws SessionRecognizingException {
		int userId = getUserId();
		if (word == null || word.isEmpty()) {
			logger.error("Empty word was given!");
			return "Empty word was given!";
		}
		Word.Builder wordBuilder = Word.newBuilder();
		wordBuilder
				.setUserId(userId)
				.setWordId(wordId)
				.setWord(word)
				.setTranslation(translation)
				.setDescription(description)
				.setExample(example);
		if (synonyms != null) {
		    wordBuilder.setSynonyms(synonyms);
		}
		Word builtWord = wordBuilder.build();
        try {
            wordService.updateWord(builtWord);
        } catch(DataAccessException e) {
            logger.error("Update word error. ", e);
            return "Update word procedure failed.";
        } catch (Exception e) {
            logger.error("Update word error. ", e);
            return e.getMessage();
        }
		return "Ok";
	}

	/**
	 * Returns information about specified word
	 *
	 * @param wordId
	 *            Integer value of word which information should be returned
	 * @return Word instance in JSON format
	 * @throws SessionRecognizingException
	 *            if user is not authorized
	 */
	@RequestMapping(value = "/word", method = RequestMethod.POST)
	public @ResponseBody Object getWord(@RequestParam("wordId") int wordId) throws
	                              SessionRecognizingException {
		getUserId();
		Word word = null;
		try {
		    word = wordService.getWord(wordId);		
		} catch (DataAccessException e) {
		    logger.error("Get word error. ", e);
		    return "Procedure failed.";
		} catch (Exception e) {
		    logger.error("Get word error. ", e);
            return e.getMessage();
		}
		
		return word;
	}
	
	/*package*/ int getUserId() throws SessionRecognizingException {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
												.getContext()
												.getAuthentication()
												.getPrincipal();
		if (userDetails == null) {
			logger.error("UserDetails is null. JSESSIONID was not found");
			throw new SessionRecognizingException("Some problems with session occurred!");
		}
		return userDetails.getId();
	}
}
