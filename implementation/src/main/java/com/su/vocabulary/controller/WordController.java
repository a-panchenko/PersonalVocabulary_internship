package com.su.vocabulary.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.su.vocabulary.domain.Word;
import com.su.vocabulary.service.CustomUserDetails;
import com.su.vocabulary.service.UserService;
import com.su.vocabulary.service.WordService;
import com.su.vocabulary.service.exceptions.EmptyWordException;
import com.su.vocabulary.service.exceptions.SessionRecognizingException;
import com.su.vocabulary.service.exceptions.UserNotFoundException;
import com.su.vocabulary.service.exceptions.WordAlreadyExistsException;
import com.su.vocabulary.service.exceptions.WordNotFoundException;

/**
 * Controller class to process requests for adding/editing/deleting words
 * @author alexandr.panchenko
 * @version 1.0
 * @since 2015-05-22
 *
 */
@Controller
public class WordController {
	/** Bean instance of WordService */
	@Autowired
	private WordService wordService;
	private static final Logger logger = Logger.getLogger(WordController.class);
	
	public WordController(){
		
	}
	// is used for UserControllerMockitoTest.java
    /* package */WordController(WordService wordService) {
        this.wordService = wordService;
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
	@RequestMapping(value="/createWord", method = RequestMethod.POST)
	public @ResponseBody String createWord(@RequestParam("word") String word,
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
        wordBuilder.setUserId(userId).setWord(word).setTranslation(translation)
                .setDescription(description).setExample(example);
        if (synonyms != null) {
            wordBuilder.setSynonyms(synonyms);
        }
        Word builtWord = wordBuilder.build();
        try {
            wordService.createWord(builtWord);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Ok";
    }

	/**
	 * Returns all user's words
	 *
	 * @return ModelAndView object with the name of returned page and list of
	 *         user's words as a model attribute;
	 * @throws SessionRecognizingException
	 *            if user is not authorized
	 * @throws UserNotFoundException
	 *            if user with such id was not found in database
	 */
	@RequestMapping(value="/words", method = RequestMethod.GET)
	public ModelAndView getAllWords() throws SessionRecognizingException, 
											 UserNotFoundException {
		ModelAndView model = new ModelAndView("words");
		int userId = getUserId();
		List<Word> words = wordService.getAll(userId);
		model.addObject("words", words);
		return model;
	}

	/**
	 * Deletes word with id from HTTP request.
	 *
	 * @param wordId
	 *            Integer value of word which should be deleted
	 * @return String value of page to which user should be redirected
	 * @throws SessionRecognizingException
	 *            if user is not authorized
	 */
	@RequestMapping(value = "/delete_word", method = RequestMethod.GET)
	public String deleteWord(@RequestParam("wordId") int wordId) throws 
	                              SessionRecognizingException {
		getUserId();
		try {
		    wordService.removeWord(wordId);		
		} catch(Exception e) {
		    return e.getMessage();
		}
		return "redirect:/words";
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
							 @RequestParam("translation") String translation,
							 @RequestParam("description") String description,
							 @RequestParam("example") String example,
							 @RequestParam("synonyms") List<String> synonyms
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
        } catch (Exception e) {
            return e.getMessage();
        }

		return "Ok";		
	}

	/**
	 * Provides ability to update details about a word
	 *
	 * @param wordId
	 *            Integer value of word which should be updated
	 * @return ModelAndView object with the name of returned page and word
	 *         which should be updated
	 * @throws SessionRecognizingException
	 *            if user is not authorized
	 * @throws WordNotFoundException
	 *            if word with such id was not found
	 */
	@RequestMapping(value = "/edit_word", method = RequestMethod.GET)
	public ModelAndView editWord(@RequestParam("wordId") Integer wordId)
			throws SessionRecognizingException, WordNotFoundException {
		getUserId();
		ModelAndView modelAndView = new ModelAndView("editWord");
		Word word = null;
		word = wordService.getWord(wordId);
		modelAndView.addObject("word", word);
		return modelAndView;
	}

	/**
	 * Provides ability to view information about a word
	 *
	 * @param wordId
	 *            Integer value of word which should be shown
	 * @return ModelAndView object with the name of returned page and word
	 *         which should be shown
	 * @throws SessionRecognizingException
	 *            if user is not authorized
	 * @throws WordNotFoundException
	 *            if word with such id doesn't exist
	 */
	@RequestMapping(value = "/get_word", method = RequestMethod.GET)
	public ModelAndView getWord(@RequestParam("wordId") int wordId) throws 
	                              SessionRecognizingException, WordNotFoundException {
		getUserId();
		Word word = wordService.getWord(wordId);
		ModelAndView model = new ModelAndView("viewWord");
		model.addObject("word", word);
		
		return model;
	}
	
	int getUserId() throws SessionRecognizingException {
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

	/**
	 * Handles all exceptions inherited from DataAccessException class.
	 *
	 * @param ex
	 *            exception thrown in one of methods in WordController
	 * @return ModelAndView object with view name and attributes identifying
	 *         displayed exception message
	 */
	@ExceptionHandler(DataAccessException.class)
    public ModelAndView handleDbException(Exception ex) {
        logger.error("registration or account deletion failed", ex);
        ModelAndView model = new ModelAndView("signup_error_page");
        model.addObject("DataAccessException", "true");
        model.addObject("errMsg", "Database exception");
        return model;
    }

	/**
	 * Handles all exceptions other than DataAccessException.
	 *
	 * @param e
	 *            exception thrown in one of methods in WordController
	 * @return ModelAndView object with view name and attributes identifying
	 *         displayed exception message
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception e) {
		String exceptionName = e.getClass().getSimpleName();
		ModelAndView model = new ModelAndView("word_error");
		model.addObject(exceptionName, "true");
		model.addObject("errMsg", e.getMessage());
		return model;
	}
}
