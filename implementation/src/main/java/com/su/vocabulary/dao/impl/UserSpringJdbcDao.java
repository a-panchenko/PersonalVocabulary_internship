package com.su.vocabulary.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.su.vocabulary.dao.AbstractSpringJdbcDao;
import com.su.vocabulary.domain.User;

/**
 * Class provides methods for interacting with database using SpringJDBC
 * @author alexandr.panchenko
 * @version 1.0
 * @since 2015-05-13
 */
public class UserSpringJdbcDao extends AbstractSpringJdbcDao<User> {
	/**
	 * Object for encoding password
	 */
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	protected String getInsertSQL() {		
		return "INSERT INTO users(user_email, password) VALUES(?, ?)";
	}

	@Override
	protected String getSelectByIdSQL() {		
		return "SELECT * FROM users WHERE user_id = ?";
	}
	
	@Override
	protected String getSelectByValueSQL() {
		return "SELECT * FROM users WHERE user_email = ?";
	}

	@Override
	protected String getSelectAllSQL() {		
		return "SELECT * FROM users";
	}

	@Override
	protected String getUpdateSQL() {		
		return "UPDATE users SET user_email = ?, password = ? WHERE user_id = ?";
	}

	@Override
	protected String getDeleteSQL() {		
		return "DELETE FROM users WHERE user_id = ?";
	}

	@Override
	protected String getCountOfExistSQL() {
		return "SELECT COUNT(*) FROM users WHERE user_email =?";
	}

	@Override
	protected RowMapper<User> getRowMapper() {
		return new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				int userId = rs.getInt("user_id");
				String email = rs.getString("user_email");
				String password = rs.getString("password");				
				return new User(userId, email, password);
			}
			
		};
	}

    @Override
    protected Object[] getIsInstanceExistsArgs(User instance) {
        return new Object[] {instance.getEmail()};
    }

    @Override
    protected Object[] getCreateArgs(User object) {
        String userEmail = object.getEmail();
        String encodedPassword = encoder.encode(object.getPassword());
        return new Object[] {userEmail, encodedPassword};
    }

    @Override
    protected Object[] getUpdateArgs(User object) {
        String email = object.getEmail();
        String password = object.getPassword();
        Integer userId = object.getId();
        return new Object[] { email, password, userId };
    }	
}
