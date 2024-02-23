package com.reiko.nail.dao;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.entity.ShiireRirekiEntity;

@Mapper
public interface RirekiDao {

	/**
	 * 仕入履歴情報の登録
	 * @param shiireRirekiEntity
	 */
	void insertShiireRireki(ShiireRirekiEntity shiireRirekiEntity);

	/**
	 * 仕入履歴情報の更新
	 * @param rirekiId 履歴ID
	 * @param shiireDate 仕入日
	 * @param suryo 数量
	 * @return
	 */
	int updateRireki(@Param("rirekiId") String rirekiId, @Param("shiireDate") LocalDate shiireDate, @Param("suryo") Integer suryo);

	/**
	 * 仕入履歴の削除
	 * @param rirekiId 履歴ID
	 */
	int deleteRireki(@Param("rirekiId") String rirekiId);
}
