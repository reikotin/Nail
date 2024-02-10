package com.reiko.nail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.entity.SalesEntity;

@Mapper
public interface SalesDao {

	int registryMeisai(List<SalesEntity> salesList);
	
	int deleteMeisaiByPrimary(@Param("denpyoNo") String denpyoNo);

}
