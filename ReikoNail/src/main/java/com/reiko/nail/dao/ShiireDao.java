package com.reiko.nail.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.entity.ShiireEntity;

@Mapper
public interface ShiireDao {

	/**
	 *  仕入登録
	 * @param shiireEntity
	 */
	void insertShiireItem(ShiireEntity shiireEntity);
	
	/**
	 * 仕入一覧用 仕入検索
	 * @param janCd
	 * @param daiBunruiName
	 * @param shoBunruiName
	 * @param itemName
	 * @param startDate
	 * @param endDate
	 * @param pageNumber
	 * @return List<ShiireEntity>
	 */
	List<ShiireEntity> searchShiireItemList(@Param("janCd") String janCd, @Param("daiBunruiName") String daiBunruiName,
			@Param("shoBunruiName") String shoBunruiName, @Param("itemName") String itemName,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pageNumber") int pageNumber);
	/**
	 * 仕入登録時用 仕入検索
	 * @param janCd
	 * @param daiBunruiName
	 * @param shoBunruiName
	 * @param itemName
	 * @return List<ShiireEntity>
	 */
	List<ShiireEntity> searchShiireForNewShiire(
			@Param("janCd") String janCd, @Param("daiBunruiName") String daiBunruiName, 
			@Param("shoBunruiName") String shoBunruiName, @Param("itemName") String itemName);
	
	/**
	 * 仕入一覧ページネーション用
	 * @param janCd
	 * @param daiBunruiName
	 * @param shoBunruiName
	 * @param itemName
	 * @param startDate
	 * @param endDate
	 * @return 仕入件数
	 */
	int countSearcShiireItemList(@Param("janCd") String janCd, @Param("daiBunruiName") String daiBunruiName,
			@Param("shoBunruiName") String shoBunruiName, @Param("itemName") String itemName,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	/**
	 * 削除済み含む仕入総数
	 * @return
	 */
	int countId();
	
	/**
	 *  品番で一致する仕入情報
	 * @param janCd
	 * @return
	 */
	ShiireEntity findShiireJohoByJanCd(@Param("janCd") String janCd);
	
	/**
	 * 大分類が「カラー」と「パーツ」に絞った仕入情報リストの取得
	 * @param color
	 * @param parts
	 * @return
	 */
	List<ShiireEntity> selectByColorAndPartsShiireList(@Param("color") String color, @Param("parts") String parts);
	
	List<ShiireEntity> selectByItemNameList(@Param("daiBunruiName") String daiBunruiName, @Param("shoBunruiName") String shoBunruiName);
	
	/**
	 * 仕入IDに対する仕入情報を取得
	 * @param list
	 * @return List<ShiireEntity>
	 */
	List<ShiireEntity> selectByIdShiireList(List<String> list);
}
