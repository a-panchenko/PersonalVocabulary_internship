package com.su.vocabulary.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.servlet.ModelAndView;

import com.su.vocabulary.domain.Word;
import com.su.vocabulary.service.WordService;
import com.su.vocabulary.service.exceptions.EmptyWordException;
import com.su.vocabulary.service.exceptions.SessionRecognizingException;
import com.su.vocabulary.service.exceptions.UserNotFoundException;
import com.su.vocabulary.service.exceptions.WordAlreadyExistsException;
import com.su.vocabulary.service.exceptions.WordNotFoundException;

import static org.mockito.Mockito.*;

public class ExternalWordControllerTest {
	private ExternalWordController controller;
	private WordService service;
		
	@Before
	public void setUP() throws SessionRecognizingException {
		service = Mockito.mock(WordService.class);
		controller = Mockito.spy(new ExternalWordController(service));
	}	
	
	@Test
	public void testCreatEmptyWord() throws UserNotFoundException, EmptyWordException, 
							  SessionRecognizingException, WordAlreadyExistsException {
		Word word = Word.newBuilder().setWord("").build();
		when(service.createWord(word)).thenReturn(word);
		doReturn(1).when(controller).getUserId();		
		Object result = controller.createWord("", null, null, null, null);
		assertEquals("Empty word was given!", (String)result);
	}
	
	
	@Test
	public void testCreateValidWordCreate() throws EmptyWordException, 
					UserNotFoundException, SessionRecognizingException, 
					WordAlreadyExistsException {
		
		Integer wordId = 1;
		Integer userId = 1;
		doReturn(userId).when(controller).getUserId();
		Word word = Word.newBuilder().setUserId(1).setWord("Word").setWordId(wordId).build();
		when(service.createWord(any(Word.class))).thenReturn(word);
		Object result = controller.createWord("word", null, null, null, null);		
		assertEquals(wordId, (Integer)result);
	}
	
	@Test
    public void testCreateWordDbException() throws EmptyWordException, 
                    UserNotFoundException, SessionRecognizingException, 
                    WordAlreadyExistsException {
        
        Integer userId = 1;
        doReturn(userId).when(controller).getUserId();
        when(service.createWord(any(Word.class))).thenThrow(new BadSqlGrammarException("", "", new SQLException()));
        Object result = controller.createWord("word", null, null, null, null);      
        assertEquals("Word saving procedure failed.", result);
    }
	
	@Test
	public void getWordsTest() throws UserNotFoundException, SessionRecognizingException, EmptyWordException, WordNotFoundException {
		List<Word> list = new ArrayList<Word>();
		ModelAndView model = new ModelAndView();
		model.addObject("words", list);
		model.setViewName("words");
		when(service.getAll(1)).thenReturn(list);
		doReturn(1).when(controller).getUserId();
		controller.getAllWords();
		verify(service).getAll(1);
	}
	
	@Test
    public void getWordsDbException() throws UserNotFoundException, SessionRecognizingException, EmptyWordException, WordNotFoundException {
        List<Word> list = new ArrayList<Word>();
        ModelAndView model = new ModelAndView();
        model.addObject("words", list);
        model.setViewName("words");
        when(service.getAll(1)).thenThrow(new BadSqlGrammarException("", "", new SQLException()));
        doReturn(1).when(controller).getUserId();
        Object result = controller.getAllWords();
        assertEquals("Procedure failed.", result);
    }
	
	@Test
	public void getWord() throws WordNotFoundException, SessionRecognizingException {
		Word word = Word.newBuilder().setWord("word").build();
		ModelAndView model = new ModelAndView();
		model.addObject("word", word);
		model.setViewName("word");
		when(service.getWord(anyInt())).thenReturn(word);
		doReturn(1).when(controller).getUserId();
		controller.getWord(1);
		verify(service).getWord(1);
	}
	
	@Test
    public void getWordDbException() throws WordNotFoundException, SessionRecognizingException {
        Word word = Word.newBuilder().setWord("word").build();
        ModelAndView model = new ModelAndView();
        model.addObject("word", word);
        model.setViewName("word");
        when(service.getWord(anyInt())).thenThrow(new BadSqlGrammarException("", "", new SQLException()));
        doReturn(1).when(controller).getUserId();
        Object result = controller.getWord(1);
        assertEquals("Procedure failed.", result);
    }
	
	@Test
	public void deleteWordTest() throws UserNotFoundException, SessionRecognizingException, WordNotFoundException {
		doReturn(1).when(controller).getUserId();
		controller.deleteWord(1);
		verify(service).removeWord(1);
	}
	
	@Test
    public void deleteWordDbException() throws UserNotFoundException, SessionRecognizingException, WordNotFoundException {
        doReturn(1).when(controller).getUserId();
        doThrow(new BadSqlGrammarException("", "", new SQLException())).when(service).removeWord(anyInt());
        Object result = controller.deleteWord(1);
        assertEquals("Delete word procedure failed!", result);
    }
	
	@Test
	public void updateWordTest() throws SessionRecognizingException, 
								WordNotFoundException, EmptyWordException, 
								UserNotFoundException, WordAlreadyExistsException {		
		doReturn(1).when(controller).getUserId();
		doNothing().when(service).updateWord(any(Word.class));
		String result = controller.updateWord(1, "word2", null, null, null, null);		
		assertEquals("Ok", result);		
	}
	
	@Test
    public void updateWordDbException() throws SessionRecognizingException, 
                                WordNotFoundException, EmptyWordException, 
                                UserNotFoundException, WordAlreadyExistsException {     
        doReturn(1).when(controller).getUserId();
        doThrow(new BadSqlGrammarException("", "", new SQLException())).when(service).updateWord(any(Word.class));
        String result = controller.updateWord(1, "word2", null, null, null, null);      
        assertEquals("Update word procedure failed.", result);     
    }
}
