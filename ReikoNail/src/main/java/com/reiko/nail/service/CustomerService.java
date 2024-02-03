package com.reiko.nail.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reiko.nail.dao.CustomerDao;
import com.reiko.nail.dto.DenpyoDto;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.entity.CustomerEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	
	private final CustomerDao customerDao;
	
	// 既存のお客様検索
	public CustomerEntity findByCustomer(String customerCd) {
		return customerDao.findByCustomer(customerCd);
	}

	// 新規お客様登録
	public void insertNewCustomer(DenpyoDto denpyoDto) {
		
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setCustomerCd(denpyoDto.getCustomerCd());
		customerEntity.setCustomerSei(denpyoDto.getCustomerSei());
		customerEntity.setCustomerMei(denpyoDto.getCustomerMei());
		customerEntity.setYubinNo(denpyoDto.getYubinNo());
		customerEntity.setPrefectureCity(denpyoDto.getPrefectureCity());
		customerEntity.setStreetNo(denpyoDto.getStreetNo());
		customerEntity.setRuikeiKounyuKingaku(sumPrice(denpyoDto.getShohinDto()));
		
		customerDao.insertCustomer(customerEntity);
	}
	// お客様情報更新
	public void updateCustomerJoho(DenpyoDto denpyoDto) {
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setCustomerCd(denpyoDto.getCustomerCd());
		customerEntity.setCustomerSei(denpyoDto.getCustomerSei());
		customerEntity.setCustomerMei(denpyoDto.getCustomerMei());
		customerEntity.setSeiFurigana(denpyoDto.getSeiFurigana());
		customerEntity.setMeiFurigana(denpyoDto.getMeiFurigana());
		customerEntity.setYubinNo(denpyoDto.getYubinNo());
		customerEntity.setPrefectureCity(denpyoDto.getPrefectureCity());
		customerEntity.setStreetNo(denpyoDto.getStreetNo());
		customerEntity.setRuikeiKounyuKingaku(sumPrice(denpyoDto.getShohinDto()));
		
		customerDao.updateCustomer(customerEntity);
	}
	
	// 購入金額の計算
	private Long sumPrice(List<ShohinDto> shohinDto) {
		Long total = 0L;
		
		for(ShohinDto shohin : shohinDto) {
			total += shohin.getZeinukiGaku();
		}
		
		return total;
	}
	
	// 全てのお客様情報を取得
	public List<CustomerEntity> getAllCustomer(){
		return customerDao.selectByAllCustomer();
	}

}
