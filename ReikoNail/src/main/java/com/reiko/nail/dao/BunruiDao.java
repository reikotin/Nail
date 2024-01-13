package com.reiko.nail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.dto.BunruiDto;
import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.entity.BunruiEntity;

@Mapper
public interface BunruiDao {

	/**
	 * 全ての分類を取得
	 * @return
	 */
	List<BunruiDto> selectAllBunrui();

	/**
	 * 引数の大分類登録数を取得
	 * @param daiBunruiName
	 * @return 件数
	 */
	int selectCountBunrui(@Param("daiBunruiName") String daiBunruiName);
	
	void insertBunrui(BunruiEntity bunruiEntity);
	
	List<BunruiNameDto> relationByDaiBunrui(@Param("daiBunruiName") String daiBunruiName);
	
}
