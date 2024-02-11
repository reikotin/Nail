package com.reiko.nail.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reiko.nail.dao.CustomerDao;
import com.reiko.nail.dto.ApiResponse;
import com.reiko.nail.dto.EditDenpyoDto;
import com.reiko.nail.entity.CustomerEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.util.Constants;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomerService {
	
	private final CustomerDao customerDao;
	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;
	
	
	public ApiResponse getAddress(String yubinNo) {
		String apiUrl = Constants.ZIPCLOUD + yubinNo;
	
		ApiResponse result = null;
		
		try {
			HttpRequest request = (HttpRequest) HttpRequest.newBuilder(URI.create(apiUrl)).build();
			HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
			if(ObjectUtils.isEmpty(apiResponse.getResults())) {
				apiResponse.setError(true);
				apiResponse.setMessage("に一致する住所がありません。");
			}
			result = apiResponse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<String> valid(Object obj){
		List<String> message = new ArrayList<>();
		
		
		return message;
	}
	
	// 既存のお客様検索
	public CustomerEntity findByCustomer(String customerCd) {
		return customerDao.findByCustomer(customerCd);
	}

//	/**
//	 * TODO 2/11 未使用メソッド
//	 * @param denpyoDto
//	 */
//	public void insertNewCustomer(DenpyoDto denpyoDto) {
//		CustomerEntity customerEntity = new CustomerEntity();
//		customerEntity.setCustomerCd(denpyoDto.getCustomerCd());
//		customerEntity.setCustomerSei(denpyoDto.getCustomerSei());
//		customerEntity.setCustomerMei(denpyoDto.getCustomerMei());
//		customerEntity.setYubinNo(denpyoDto.getYubinNo());
//		customerEntity.setPrefectureCity(denpyoDto.getPrefectureCity());
//		customerEntity.setStreetNo(denpyoDto.getStreetNo());
//		customerDao.insertCustomer(customerEntity);
//	}
	
	// お客様情報更新
	public void updateCustomerJoho(EditDenpyoDto denpyoDto) {
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
		
		customerDao.updateCustomer(customerEntity);
	}
	
	public void updateRuikeiKounyuKingaku(EditDenpyoDto denpyoDto) {
		Long kounyuKingaku = sumPrice(denpyoDto.getShohinJoho());
		
		String customerCd = denpyoDto.getCustomerCd();
		
		customerDao.updateCustomerKingakuJoho(customerCd, kounyuKingaku);
		
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

}
