package com.quizzbattle.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quizzbattle.ws.exception.NotFoundException;
import com.quizzbattle.ws.model.User;
import com.quizzbattle.ws.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageSource messageSource;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(messageSource.getMessage("error.UserService.user.not.found",
						new String[] { username }, LocaleContextHolder.getLocale())));

		return new UserDetailsImpl(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(
				() -> new NotFoundException(messageSource.getMessage("error.UserService.user.not.found.by.id",
						new Object[] { id }, LocaleContextHolder.getLocale())));

		return new UserDetailsImpl(user);
	}
}
