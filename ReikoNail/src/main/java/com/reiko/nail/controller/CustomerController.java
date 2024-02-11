package com.reiko.nail.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.reiko.nail.dto.ApiResponse;
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
	
	@RequestMapping(value = "/CustomerNew", method = {RequestMethod.GET})
	public ModelAndView customerNew() {
		ModelAndView modelAndView = new ModelAndView("CustomerNew");
		 
		
		modelAndView.addObject("customer", new CustomerEntity());
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/GetAddress", method = {RequestMethod.GET})
	public ResponseEntity<ApiResponse> getAddress(@RequestParam String yubinNo){
	
		ApiResponse address = customerService.getAddress(yubinNo);
		
		
		return new ResponseEntity<ApiResponse>(address, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/Register", method = {RequestMethod.POST})
	public ModelAndView registry(@ModelAttribute CustomerEntity customerEntity) {
		ModelAndView modelAndView = new ModelAndView();
		
		
		System.out.println(customerEntity);
		
		
		return null;
		
	}

}
