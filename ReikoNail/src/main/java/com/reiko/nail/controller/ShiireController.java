package com.reiko.nail.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.reiko.nail.dto.EditShiireDto;
import com.reiko.nail.response.ResponseData;
import com.reiko.nail.service.MessageService;
import com.reiko.nail.service.ValidationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShiireController {

	private final static Logger logger = LogManager.getLogger(ShiireController.class);
	
	private final MessageService messageService;
	
	private final ValidationService validationService;
	
	@RequestMapping(value = "/Valide", method = {RequestMethod.GET})
	public ResponseEntity<ResponseData<List<String>>> valide(@ModelAttribute EditShiireDto editShiireDto) {
		
		logger.info(messageService.getMessage("proccess.Start", new String[] {"仕入商品の入力チェック"}));
		ResponseData<List<String>> res = new ResponseData(); 
		
		List<String> errorMessageList = validationService.validate(editShiireDto);
		if (errorMessageList.size() > 0) {
			res.setData(errorMessageList);
			res.setHasError(true);
			res.setMessage(messageService.getMessage("check.data.info", null));
		}
		logger.info(messageService.getMessage("proccess.End", new String[] {"仕入商品の入力チェック"}));
		
		return new ResponseEntity<ResponseData<List<String>>>(res, HttpStatus.OK);

	}
}
