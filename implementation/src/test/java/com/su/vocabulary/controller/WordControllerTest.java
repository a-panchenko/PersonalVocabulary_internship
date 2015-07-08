package com.su.vocabulary.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import com.su.vocabulary.domain.Word;
import com.su.vocabulary.service.CustomUserDetails;
import com.su.vocabulary.service.WordService;
import com.su.vocabulary.service.exceptions.EmptyWordException;
import com.su.vocabulary.service.exceptions.SessionRecognizingException;
import com.su.vocabulary.service.exceptions.UserNotFoundException;
import com.su.vocabulary.service.exceptions.WordAlreadyExistsException;
import com.su.vocabulary.service.exceptions.WordNotFoundException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class) 
public class WordControllerTest {
	private WordController controller;
	private WordService service;
	private ModelAndView model;
	
	@Before
	public void setUP() throws SessionRecognizingException {
		service = Mockito.mock(WordService.class);
		controller = Mockito.spy(new WordController(service));
	}	
	
	@Test
	public void testCreatEmptyeWord() throws UserNotFoundException, EmptyWordException, 
							  SessionRecognizingException, WordAlreadyExistsException {
		Word word = Word.newBuilder().setWord("").build();
		when(service.createWord(word)).thenReturn(word);
		doReturn(1).when(controller).getUserId();		
		String result = controller.createWord("", null, null, null, null);
		assertEquals("Empty word was given!", result);
	}
		
	@Test
	public void testCreateValidWordCreate() throws EmptyWordException, 
					UserNotFoundException, SessionRecognizingException, 
					WordAlreadyExistsException {
		Word word = Word.newBuilder().setUserId(1).setWord("Word").build();
		when(service.createWord(word)).thenReturn(word);
		doReturn(1).when(controller).getUserId();
		String controllerView = controller.createWord("word", null, null, null, null);		
		assertEquals("Ok", controllerView);
	}
	
	@Test
	public void getWordsTest() throws UserNotFoundException, SessionRecognizingException, EmptyWordException, WordNotFoundException {
		List<Word> list = new ArrayList<Word>();
		ModelAndView model = new ModelAndView();
		model.addObject("words", list);
		model.setViewName("words");
		when(service.getAll(1)).thenReturn(list);
		doReturn(1).when(controller).getUserId();
		ModelAndView createdModel = controller.getAllWords();
		assertEquals(model.getModelMap(), createdModel.getModelMap());
		assertEquals(model.getViewName(), createdModel.getViewName());
	}
	
	@Test
	public void getWord() throws WordNotFoundException, SessionRecognizingException {
		Word word = Word.newBuilder().setWord("word").build();
		ModelAndView model = new ModelAndView();
		model.addObject("word", word);
		model.setViewName("viewWord");
		when(service.getWord(anyInt())).thenReturn(word);
		doReturn(1).when(controller).getUserId();
		ModelAndView createdModel = controller.getWord(1);
		assertEquals(model.getModelMap(), createdModel.getModelMap());
		assertEquals(model.getViewName(), createdModel.getViewName());		
	}
	
	@Test
	public void deleteWordTest() throws UserNotFoundException, SessionRecognizingException, WordNotFoundException {
		doReturn(1).when(controller).getUserId();
		controller.deleteWord(1);
		verify(service).removeWord(1);
	}
	
	@Test
	public void updateWordTest() throws SessionRecognizingException, 
								WordNotFoundException, EmptyWordException, 
								UserNotFoundException, WordAlreadyExistsException {		
		doReturn(1).when(controller).getUserId();
		String redirectPage = controller.updateWord(1, "word2", null, null, null, null);
		assertEquals("Ok", redirectPage);		
	}
	
}
