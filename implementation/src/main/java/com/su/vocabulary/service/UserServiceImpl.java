package com.su.vocabulary.service;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.su.vocabulary.dao.GenericDao;
import com.su.vocabulary.domain.User;
import com.su.vocabulary.service.exceptions.UserAlreadyExistsException;
import com.su.vocabulary.service.exceptions.UserNotFoundException;

/**
 * Provides methods to manipulate with users's data
 * @author alexandr.panchenko
 * @version 1.0
 * @since 2015-05-13
 */
public class UserServiceImpl implements UserService {
	/**
	 * {@link GenericDao} which provides an abstract interface to some 
	 *           type of database 
	 */
	private GenericDao<User> userDao;
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	/**
	 * Sets implementation of {@link GenericDao}. Is wired by Spring IoC
	 * @param userDao
	 */
	@Autowired
	@Qualifier("userDao")
	public void setUserDao(GenericDao<User> userDao) {
		this.userDao = userDao;
	}

	/**
	 * @see {@link org.springframework.security.core.userdetails.UserDetails}
	 */
	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {
		try {
			User user = userDao.getByValue(email);
			return new UserDetailsImpl(user);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(email);
		}
	}

	@Override
	public User createUser(String email, String password) throws UserAlreadyExistsException {
		User user = new User(null, email, password);
		if (userDao.isInstanceExists(user)) {
			logger.error("Error while creating user. User with email " + email + " already exist.");
			throw new UserAlreadyExistsException("User with such email already exist!");
		}		
		userDao.create(user);
		logger.debug("Added new User with email " + email);
		User userWithId = userDao.getByValue(user.getEmail());
		return userWithId;
	}

	@Override
	public User getUser(int userId) throws UserNotFoundException {
		checkId(userId);
		User user = userDao.getById(userId);
		if (user == null) {
			throw new UserNotFoundException("User with such id was not found!");
		}
		logger.debug("Selected user with id = " + user.getId() + ", email = " + user.getEmail());
		return user;
	}

	@Override
	public void deleteUser(int userId) throws UserNotFoundException {
		checkId(userId);
		try {
			userDao.getById(userId);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException("User with id = " + userId + " was not found!");
		}
		userDao.delete(userId);
		logger.debug("User with id = " + userId + " was deleted");
	}
	
	private void checkId(int id) throws UserNotFoundException {
		if (id < 1) {
			throw new UserNotFoundException("User with id = " + id + " was not found");
		}
	}
	
	private static class UserDetailsImpl implements CustomUserDetails {
        
        private static final long serialVersionUID = 1L;
		
		private transient User user;
				
		public UserDetailsImpl(User user) {
			this.user = user;
		}
		
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			GrantedAuthority grantedAuthority = new GrantedAuthority() {					
				public String getAuthority() {
					return UserRole.ROLE_USER.name();
				}
			};				
			return Arrays.asList(grantedAuthority);
		}

		@Override
		public String getPassword() {
			return user.getPassword();
		}

		@Override
		public String getUsername() {
			return user.getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public int getId() {			
			return user.getId();
		}
		
	}
	
}
