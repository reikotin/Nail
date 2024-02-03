package com.reiko.nail.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reiko.nail.entity.CustomerEntity;
import com.reiko.nail.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CustomerController {
	
	private final CustomerService customerService;
	
	@RequestMapping(value = "/GetCustomerJoho", method = {RequestMethod.GET})
	public ResponseEntity<CustomerEntity> getCustomerJoho(@RequestParam String customerCd){
		
		if(StringUtils.isEmpty(customerCd)) {
			return ResponseEntity.ok(null);
		}
		
		CustomerEntity customer = customerService.findByCustomer(customerCd);
		
		return new ResponseEntity<CustomerEntity>(customer, HttpStatus.OK);
	}

}
