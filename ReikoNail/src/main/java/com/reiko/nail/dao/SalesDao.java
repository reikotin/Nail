package com.reiko.nail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.reiko.nail.entity.SalesEntity;

@Mapper
public interface SalesDao {

	void registryMeisai(List<SalesEntity> salesList);

}
