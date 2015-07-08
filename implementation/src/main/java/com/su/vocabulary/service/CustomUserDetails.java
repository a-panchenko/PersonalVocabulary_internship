package com.su.vocabulary.service;

import org.springframework.security.core.userdetails.UserDetails;
/**
 * Provides core user information, including userId.
 * @author alexandr.panchenko
 * @since 2015-05-20
 */
public interface CustomUserDetails extends UserDetails{
	/**
	 * Returns user's id
	 * @return integer number - user's id
	 */
	int getId();
}
