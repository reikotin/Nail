package com.reiko.nail.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.reiko.nail.dto.ApiResponse;
import com.reiko.nail.entity.CustomerEntity;
import com.reiko.nail.response.ResponseData;
import com.reiko.nail.service.CustomerService;
import com.reiko.nail.service.MessageService;
import com.reiko.nail.service.ValidationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CustomerController {
	
	private final static Logger logger = LogManager.getLogger(CustomerController.class);
	private final CustomerService customerService;
	private final ValidationService validationService;
	private final MessageService messageService;
	
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
	
		logger.info(messageService.getMessage("proccess.Start", new String[] {"住所検索"}));
		ApiResponse address = customerService.getAddress(yubinNo);
		
		logger.info(messageService.getMessage("proccess.End", new String[] {"住所検索"}));
		return new ResponseEntity<ApiResponse>(address, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/Register", method = {RequestMethod.POST})
	public ResponseEntity<ResponseData<List<String>>> registry(
			@ModelAttribute CustomerEntity customerEntity, HttpSession session) {
		
		ResponseData<List<String>> response = new ResponseData<List<String>>();
		// バリデーションチェック
		List<String> errorMessageList = validationService.validateCustomer(customerEntity);
		if(errorMessageList.size() != 0) {
			response.setData(errorMessageList);
			response.setHasError(true);
			response.setMessage("以下の項目を修正してください。");
			return new ResponseEntity<ResponseData<List<String>>>(response, HttpStatus.OK);
		}
		
		ResponseData<String> resultData = customerService.insertNewCustomer(customerEntity);
		if(resultData.isHasError()) {
			response.setMessage("異常が発生しました。");
			response.setData(Arrays.asList("管理者へ連絡してください。"));
			response.setHasError(true);
		}
		String customerName = "お客様名: " + customerEntity.getCustomerSei() + customerEntity.getCustomerMei();
		session.setAttribute("headMessage", "正常に登録完了しました。");
		session.setAttribute("itemMessage1", resultData.getData());
		session.setAttribute("itemMessage2", customerName);
		session.setAttribute("next", "続けて登録する");
		session.setAttribute("url", "/CustomerNew");
		return new ResponseEntity<ResponseData<List<String>>>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/CustomerIndex", method = {RequestMethod.GET})
	public ModelAndView indexCustomer() {
		
		ModelAndView modelAndView = new ModelAndView("CustomerIndex");
		List<CustomerEntity> customerList = customerService.getAllCustomer();
		
		modelAndView.addObject("customerList", customerList);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/CustomerEdit/{customerCd}", method = {RequestMethod.GET})
	public ModelAndView editCustomer(@PathVariable String customerCd) {
		logger.info(messageService.getMessage("proccess.Start", new String[] {"お客様情報取得"}));
		CustomerEntity customer = customerService.findByCustomer(customerCd);
		logger.info(messageService.getMessage("get.customer.info", new String[] {customerCd}));
		logger.info(messageService.getMessage("proccess.End", new String[] {"お客様情報取得"}));
		
		ModelAndView modelAndView = new	ModelAndView("CustomerEdit");
		modelAndView.addObject("customer", customer);
		
		return modelAndView;
	}

}
