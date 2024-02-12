package com.reiko.nail.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	
	@Autowired
	MessageSource message;
	
	public String getMessage(String props, String[] arg) {
		return message.getMessage(props, arg, Locale.JAPAN);
	}
}
