package com.reiko.nail.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reiko.nail.dao.CustomerDao;
import com.reiko.nail.dto.ApiResponse;
import com.reiko.nail.dto.EditDenpyoDto;
import com.reiko.nail.entity.CustomerEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.response.ResponseData;
import com.reiko.nail.util.Constants;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CustomerService {
	private final static Logger logger = LogManager.getLogger(CustomerService.class);
	private final CustomerDao customerDao;
	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;
	private final MessageService messageService;
	
	
	public ApiResponse getAddress(String yubinNo) {
		logger.info(messageService.getMessage("get.address.Start", new String[] {yubinNo}));
		String apiUrl = Constants.ZIPCLOUD + yubinNo;
		ApiResponse result = null;
		
		try {
			HttpRequest request = (HttpRequest) HttpRequest.newBuilder(URI.create(apiUrl)).build();
			HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
			if(ObjectUtils.isEmpty(apiResponse.getResults())) {
				apiResponse.setError(true);
				apiResponse.setMessage(messageService.getMessage("not.address.info", new String[] {yubinNo}));
			}
			result = apiResponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 既存のお客様検索
	public CustomerEntity findByCustomer(String customerCd) {
		
		return customerDao.findByCustomer(customerCd);
	}

	/**
	 * お客様登録
	 * @param CustomerEntity
	 */
	public ResponseData<String> insertNewCustomer(CustomerEntity customerEntity) {
		ResponseData<String> customerData = new ResponseData<>();
		int count = customerDao.countCustomer();
		String newCode = String.format(Constants.FORTH, count + 1);
		customerEntity.setCustomerCd(newCode);
		int result = customerDao.insertCustomer(customerEntity);
		if(result != 1) {
			customerData.setHasError(true);
		}
		customerData.setData("お客様コード: " + newCode);
		logger.info(messageService.getMessage("insert.data.count", new String[] {"お客様情報", Integer.toString(result)}));
		
		return customerData;
	}
	
	// お客様情報更新
	public void updateCustomerJohoAll(EditDenpyoDto denpyoDto) {
		logger.info(messageService.getMessage("customer.update.Start", new String[] {denpyoDto.getCustomerCd()}));
		
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setCustomerCd(denpyoDto.getCustomerCd());
		customerEntity.setCustomerSei(denpyoDto.getCustomerSei());
		customerEntity.setCustomerMei(denpyoDto.getCustomerMei());
		customerEntity.setSeiFurigana(denpyoDto.getSeiFurigana());
		customerEntity.setMeiFurigana(denpyoDto.getMeiFurigana());
		customerEntity.setYubinNo(denpyoDto.getYubinNo());
		customerEntity.setPrefectureCity(denpyoDto.getPrefectureCity());
		customerEntity.setStreetNo(denpyoDto.getStreetNo());
		customerEntity.setRuikeiKounyuKingaku(sumPrice(denpyoDto.getShohinJoho()));
		
		customerDao.updateCustomerJohoAndKounyu(customerEntity);
		logger.info(messageService.getMessage("customer.update.End", new String[] {denpyoDto.getCustomerCd()}));
	}
	
	public void updateRuikeiKounyuKingaku(EditDenpyoDto denpyoDto) {
		logger.info(messageService.getMessage("customer.update.Start", new String[] {denpyoDto.getCustomerCd()}));
		Long kounyuKingaku = sumPrice(denpyoDto.getShohinJoho());
		
		String customerCd = denpyoDto.getCustomerCd();
		
		customerDao.updateCustomerKingakuJoho(customerCd, kounyuKingaku);
		logger.info(messageService.getMessage("customer.update.End", new String[] {denpyoDto.getCustomerCd()}));
	}
	
	/**
	 * お客様情報更新(金額以外)
	 * @param customerEntity
	 * @return
	 */
	public ResponseData<String> updateCustomerJoho(CustomerEntity customerEntity) {
		ResponseData<String> customerData = new ResponseData<>();
		int count = customerDao.updateCustomerJoho(customerEntity);
		if(count != 1) {
			customerData.setHasError(true);
		}		
		customerData.setData("お客様コード: " + customerEntity.getCustomerCd());
		logger.info(messageService.getMessage("update.data.count", new String[] {"お客様情報",Integer.toString(count)}));
		return customerData;
	}
	
	// 購入金額の計算
	private Long sumPrice(List<ShohinEntity> list) {
		Long total = 0L;
		for(ShohinEntity shohin : list) {
			total += shohin.getZeinukiGaku();
		}
		return total;
	}
	
	// 全てのお客様情報を取得
	public List<CustomerEntity> getAllCustomer(){
		return customerDao.selectByAllCustomer();
	}
	
	// お客様情報の論理削除
	public int deleteCustomer(String customerCd) {
		int count = customerDao.deleteCustomerByPrimary(customerCd);
		if(count == 1) { 
			logger.info(messageService.getMessage("customer.delete.info", new String[]{customerCd}));
		} else {
			logger.info(messageService.getMessage("customer.delete.warn", new String[]{customerCd}));
		}
		return count;	
	}

}
