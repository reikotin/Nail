package com.reiko.nail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.entity.CustomerEntity;

@Mapper
public interface CustomerDao {

	int findByCustomer(@Param("customerSei") String customerSei, @Param("customerMei") String customerMei);

	void insertCustomer(CustomerEntity customerEntity);
	
	void updateCustomer(CustomerEntity customerEntity);
	
	List<CustomerEntity> selectByAllCustomer();

}
