package com.reiko.nail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.dto.SearchShiireDto;
import com.reiko.nail.entity.ShiireEntity;

@Mapper
public interface ShiireDao {

	// 仕入登録
	void insertShiireItem(ShiireEntity shiireEntity);
	// 仕入登録時の検索
	List<ShiireEntity> searchShiireItemList(SearchShiireDto searchShiireDto);
	
	int countId();
	// 品番で一致する仕入情報
	ShiireEntity findShiireJohoByJanCd(@Param("janCd") String janCd);
	
	// 大分類が「カラー」と「パーツ」に絞った仕入情報リストの取得
	List<ShiireEntity> selectByColorAndPartsShiireList(@Param("color") String color, @Param("parts") String parts);
	
	List<ShiireEntity> selectByItemNameList(@Param("daiBunruiName") String daiBunruiName, @Param("shoBunruiName") String shoBunruiName);
}
