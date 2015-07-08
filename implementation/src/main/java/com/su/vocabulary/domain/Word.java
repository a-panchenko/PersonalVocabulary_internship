package com.su.vocabulary.domain;

import java.util.Collections;
import java.util.List;

public class Word {
	private int userId;
	private int wordId;
	public String word;
	private String translation = "";
	private String description = "";
	private String example = "";
	private List<String> synonyms;	
	
	private Word(){
		
	}
	
	public int getUserId() {
		return userId;
	}
	
	public int getWordId() {
		return wordId;
	}
	
	public String getWord() {
		return word;
	}
	
	public String getTranslation() {
		return translation;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getExample() {
		return example;
	}
	
	public List<String> getSynonyms() {
		return synonyms;
	}
	
	public static Builder newBuilder() {		
		return new Word().new Builder();
	}
	
	public class Builder {			
		private Builder() {
			
		}
		
		public Builder setUserId(int userId) {
			Word.this.userId = userId;
			return this;
		}
		
		public Builder setWordId(int wordId) {
			Word.this.wordId = wordId;
			return this;
		}
		
		public Builder setWord(String word) {
			Word.this.word = word;
			return this;
		}
		
		public Builder setTranslation(String t) {
			Word.this.translation = t; 
			return this;
		}
		
		public Builder setDescription(String description) {
			Word.this.description = description;
			return this;
		}
		
		public Builder setExample(String example) {
			Word.this.example = example;
			return this;
		}
		
		public Builder setSynonyms(List<String> synonyms) {
			Word.this.synonyms = Collections.unmodifiableList(synonyms);
			return this;
		}
				
		
		public Word build() {
			return Word.this;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((example == null) ? 0 : example.hashCode());
		result = prime * result
				+ ((synonyms == null) ? 0 : synonyms.hashCode());
		result = prime * result
				+ ((translation == null) ? 0 : translation.hashCode());
		result = prime * result + userId;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		result = prime * result + wordId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (example == null) {
			if (other.example != null)
				return false;
		} else if (!example.equals(other.example))
			return false;
		if (synonyms == null) {
			if (other.synonyms != null)
				return false;
		} else if (!synonyms.equals(other.synonyms))
			return false;
		if (translation == null) {
			if (other.translation != null)
				return false;
		} else if (!translation.equals(other.translation))
			return false;
		if (userId != other.userId)
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		if (wordId != other.wordId)
			return false;
		return true;
	}
	
	
}
