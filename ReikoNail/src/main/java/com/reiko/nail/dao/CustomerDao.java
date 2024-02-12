package com.reiko.nail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.entity.CustomerEntity;

@Mapper
public interface CustomerDao {

	/**
	 * お客様情報の取得
	 * @param customerCd
	 * @return CustomerEntity
	 */
	CustomerEntity findByCustomer(@Param("customerCd") String customerCd);

	int insertCustomer(CustomerEntity customerEntity);
	
	void updateCustomer(CustomerEntity customerEntity);
	
	/**
	 * 全てのお客様情報を取得
	 * @return List<CustomerEntity>
	 */
	List<CustomerEntity> selectByAllCustomer();
	
	void updateCustomerKingakuJoho(@Param("customerCd") String customerCd, @Param("kounyuKngaku") Long kounyuKingaku);
	
	/**
	 * お客様コード生成用
	 * @return お客様登録数
	 */
	int countCustomer();

}
