package com.reiko.nail.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.entity.DenpyoEntity;

@Mapper
public interface DenpyoDao {

	List<DenpyoEntity> selectByAllDenpyoList();

	int countDenpyoMaisu(@Param("kounyuDate") LocalDate kounyuDate);

	int insertDenpyo(@Param("zeinukiGaku") int zeinukiGaku, 
			@Param("zeiGaku") int zeiGaku, @Param("zeikomiGaku") int zeikomiGaku, 
			@Param("customerCd") String customerCd, @Param("denpyoNo") String denpyoNo, 
			@Param("kounyuDate") LocalDate kounyuDate, @Param("biko") String biko);
	
}
