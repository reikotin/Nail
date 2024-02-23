package com.reiko.nail.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.dto.EditShiireDto;
import com.reiko.nail.dto.ShiireRirekiDto;
import com.reiko.nail.entity.ShiireEntity;

@Mapper
public interface ShiireDao {

	/**
	 * 仕入新規登録
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
	 * @return {@code List<ShiireRirekiDto>}
	 */
	List<ShiireRirekiDto> searchShiireItemList(@Param("janCd") String janCd, @Param("daiBunruiName") String daiBunruiName,
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
	 * @return 仕入総数(削除含む)
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
	 * @param color カラー名
	 * @param parts パーツ名
	 * @return {@code List<ShiireEntity>}<br>カラーとパーツに絞った仕入情報リスト
	 */
	List<ShiireEntity> selectByColorAndPartsShiireList(@Param("color") String color, @Param("parts") String parts);
	
	List<ShiireEntity> selectByItemNameList(@Param("daiBunruiName") String daiBunruiName, @Param("shoBunruiName") String shoBunruiName);
	
	/**
	 * 仕入IDに対する仕入情報を取得
	 * @param shiireIdList 仕入IDリスト
	 * @return {@code List<ShiireEntity>}
	 */
	List<ShiireEntity> selectByIdShiireList(List<String> shiireIdList);
	
	/**
	 * 仕入編集時の仕入情報取得
	 * @param shiireId 仕入ID
	 * @return {@code EditShiireDto}
	 */
	EditShiireDto selectByShiire(@Param("rirekiId") String rirekiId);
	
	/**
	 * 仕入情報更新
	 * @param daiBunruiName 大分類
	 * @param shoBunruiName 小分類
	 * @param itemName 品名
	 * @param zeinukiGaku 税抜額
	 * @param zeiGaku 税額
	 * @param zeikomiGaku 税込額
	 * @param itemSize サイズ
	 * @param janCd 品番/JAN
	 * @param shiireId 仕入ID
	 * @param shiireSaki 仕入先
	 * @param shiireMaker 仕入メーカー
	 * @return
	 */
	int updateShiire(@Param("daiBunruiName") String daiBunruiName, @Param("shoBunruiName") String shoBunruiName,
			@Param("itemName") String itemName, @Param("zeinukiGaku") Integer zeinukiGaku, @Param("zeiGaku") Integer zeiGaku,
			@Param("zeikomiGaku") Integer zeikomiGaku, @Param("itemSize") String itemSize, @Param("janCd") String janCd,
			@Param("shiireId") String shiireId, @Param("shiireSaki") String shiireSaki, @Param("shiireMaker") String shiireMaker);
}
