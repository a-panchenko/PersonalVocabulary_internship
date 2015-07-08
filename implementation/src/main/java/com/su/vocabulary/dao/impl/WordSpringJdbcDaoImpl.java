package com.su.vocabulary.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.su.vocabulary.dao.AbstractSpringJdbcDao;
import com.su.vocabulary.dao.ContentDao;
import com.su.vocabulary.domain.Word;

/**
 * Provides methods for interacting with database.
 * @author julia.denysova
 * @version 1.0
 * @since 2015-06-03
 *
 */
public class WordSpringJdbcDaoImpl extends AbstractSpringJdbcDao<Word>
        implements ContentDao<Word> {

    private static final Logger logger = Logger.getLogger(WordSpringJdbcDaoImpl.class);

    /**
     * Returns SQL statement for retrieving synonyms of a certain word from
     * database.
     * @return SQL statement
     */
    private String getAllSynonymsSQL() {
        return "SELECT synonym_word_id FROM synonyms WHERE word_id=? UNION SELECT word_id FROM synonyms where synonym_word_id=?";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO words(user_id, word, word_translation, word_description, examples) VALUES(?,?,?,?,?)";                
    }
    
    private String getSelectWordNameByIdSQL() {
        return "SELECT word FROM words where word_id=?";
    }

    @Override
    protected String getSelectByIdSQL() {
        return "SELECT * FROM words where word_id=?";
    }

    @Override
    protected String getSelectByValueSQL() {
        return "SELECT * FROM words WHERE word =? and user_id =?";
    }

    @Override
    protected String getSelectAllSQL() {
        return "SELECT * FROM words WHERE user_id=?";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE words SET word=?, word_translation=?, word_description=?, examples=? WHERE word_id=?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE from words WHERE word_id=?";
    }

    @Override
    protected String getCountOfExistSQL() {
        return "SELECT COUNT(*) FROM words WHERE word=? AND user_id=?";
    }

    @Override
    protected RowMapper<Word> getRowMapper() {
        return new RowMapper<Word>() {
            public Word mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                int wordId = rs.getInt("word_id");
                int userId = rs.getInt("user_id");
                String word = rs.getString("word");
                String translation = rs.getString("word_translation");
                String description = rs.getString("word_description");
                String examples = rs.getString("examples");

                logger.debug("MAPPER: " + wordId + ", " + userId + ", " + word
                        + ", " + translation + ", " + description + ", "
                        + examples);

                Word.Builder builder = Word.newBuilder();
                builder = builder.setWordId(wordId).setUserId(userId).setWord(word)
                        .setTranslation(translation)
                        .setDescription(description).setExample(examples);

                List<String> synonyms = getSynonyms(wordId);
                if (synonyms != null) {
                    builder.setSynonyms(synonyms);
                }

                return builder.build();
            }
        };
    }

    @Override
    public Word create(Word object) {
        jdbcTemplate.update(getInsertSQL(), getCreateArgs(object));
        addSynonyms(object);
        Word createdWord = getByValue(object.getWord(), object.getUserId());
        return createdWord;
    }

    @Override
    public void update(Word object) {
        jdbcTemplate.update(getUpdateSQL(), getUpdateArgs(object));
        addSynonyms(object);

        logger.debug("WORD UPDATED wordId=" + object.getWordId());
    }

    @Override
    public Word getById(int id) {
        Word word = jdbcTemplate.queryForObject(getSelectByIdSQL(),
                new Object[] { id }, getRowMapper());
        logger.debug("GET BY ID for wordId=" + id);

        return word;
    }

    @Override
    public Word getByValue(String value, int userId) {
        Word word = jdbcTemplate.queryForObject(
                getSelectByValueSQL(), new Object[] { value, userId },
                getRowMapper());

        logger.debug("GET BY VALUE for word=" + value + ", userId=" + userId);

        return word;
    }

    @Override
    public Word getByValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Word> getAll(int userId) {
        List<Word> words = jdbcTemplate.query(getSelectAllSQL(),
                getRowMapper(), userId);
        logger.debug("GET ALL WORDS for userId=" + userId + ": " + words) ;

        return words;
    }

    @Override
    public List<Word> getAll() {
        throw new UnsupportedOperationException();
    }

    private List<String> addSynonyms(Word object) {
        List<Object[]> params = new ArrayList<Object[]>();
        List<String> synonyms = object.getSynonyms();
        if (synonyms == null || synonyms.isEmpty()) {
            return synonyms;
        }
        int wordId = getByValue(object.getWord(), object.getUserId()).getWordId();

        for (String synonym : synonyms) {
            int synonymId = 0;

            Word s = Word.newBuilder().setWord(synonym)
                    .setUserId(object.getUserId()).setDescription("")
                    .setExample("").setTranslation("").build();
            if (isInstanceExists(s)) {
                synonymId = getByValue(synonym, object.getUserId())
                        .getWordId();
            } else {
                create(s);
                synonymId = getByValue(synonym, object.getUserId())
                        .getWordId();
            }
            params.add(new Object[] { wordId, synonymId });
        }

        for (Object[] o : params) {
            logger.debug("params: " + Arrays.toString(o));
        }

        jdbcTemplate.batchUpdate("call add_synonym(?,?)", params, new int[] {
                java.sql.Types.INTEGER, java.sql.Types.INTEGER });

        logger.debug("SYNONYMS ADDED for wordId=" + object.getWordId());

        return synonyms;
    }

    private List<String> getSynonyms(int wordId) {

        List<Integer> synonymIds = jdbcTemplate.queryForList(getAllSynonymsSQL(),
                new Object[] { wordId, wordId }, Integer.class);

        List<String> synonyms = new ArrayList<String>();
        for (Integer id : synonymIds) {
            String synonym = getWordNameById(id);
            synonyms.add(synonym);
        }

        logger.debug("GET SYNONYMS for wordId=" + wordId + ": " + synonyms);

        return synonyms;
    }

    private String getWordNameById(int wordId) {
        String word = jdbcTemplate.queryForObject(
                getSelectWordNameByIdSQL(), new Object[] { wordId },
                String.class);
        return word;
    }

    @Override
    protected Object[] getIsInstanceExistsArgs(Word instance) {
        String word=instance.getWord();
        Integer userId = instance.getUserId();
        return new Object[] {word, userId};
    }

    @Override
    protected Object[] getCreateArgs(Word object) {
        Integer userId = object.getUserId();
        String word = object.getWord();
        String translation = object.getTranslation();
        String description = object.getDescription();
        String example = object.getExample();
        return new Object[] {userId, word, translation, description, example};
    }

    @Override
    protected Object[] getUpdateArgs(Word object) {
        String word = object.getWord();
        String translation = object.getTranslation();
        String description = object.getDescription();
        String examples = object.getExample();
        Integer wordId = object.getWordId();
        return new Object[] {word, translation, description, examples, wordId};
    }  
    
}
