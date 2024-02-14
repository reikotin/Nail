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

	/**
	 * 全てのお客様情報を取得
	 * @return List<CustomerEntity>
	 */
	List<CustomerEntity> selectByAllCustomer();
	
	/**
	 * 新規お客様情報登録
	 * @param customerEntity
	 * @return
	 */
	int insertCustomer(CustomerEntity customerEntity);
	
	/**
	 * お客様情報更新(累計購入回数,累計購入金額含む)
	 * @param customerEntity
	 */
	void updateCustomerJohoAndKounyu(CustomerEntity customerEntity);
	
	/**
	 * お客様情報更新(累計購入回数,累計購入金額以外)
	 * @param customerEntity
	 * @return
	 */
	int updateCustomerJoho(CustomerEntity customerEntity);
	
	/**
	 * お客様情報更新(累計購入回数,累計購入金額のみ)
	 * @param customerCd
	 * @param kounyuKingaku
	 */
	void updateCustomerKingakuJoho(@Param("customerCd") String customerCd, @Param("kounyuKngaku") Long kounyuKingaku);
	
	/**
	 * お客様コード生成用
	 * @return お客様登録数
	 */
	int countCustomer();
	
	/**
	 * お客様情報の物理削除
	 * @param customerCd
	 * @return
	 */
	int deleteCustomerByPrimary(@Param("customerCd")String customerCd);

}
