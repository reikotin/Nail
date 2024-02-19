package com.reiko.nail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.dto.SearchItemDto;
import com.reiko.nail.dto.ShohinRequest;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.entity.ShohinEntity;

@Mapper
public interface ShohinDao {

	/**
	 * 商品リスト取得
	 * @return List<ShohinEntity>
	 */
	List<ShohinEntity> selectAllShohinList();

	/**
	 * 商品の金額情報の取得
	 * @param shohinCd
	 * @return ShohinDto
	 */
	ShohinDto selectByShohinKingaku(@Param("shohinCd") String shohinCd);

	/**
	 * 商品の検索結果取得
	 * @param data
	 * @return List<ShohinEntity>
	 */
	List<ShohinEntity> searchItemList(SearchItemDto data);

	/**
	 * パラメーターと同色の最新の商品コードを取得
	 * @param jpColorHenkan
	 * @return
	 */
	String findByLastShohinCd(@Param("color") String color);

	/**
	 * 新商品登録
	 * @param shohinEntity
	 */
	void insertNewItem(ShohinEntity shohinEntity);

	/**
	 * 商品論理削除
	 * @param shohinCd
	 * @return
	 */
	int deleteShohin(@Param("shohinCd") String shohinCd);
	
	/**
	 * 商品一括論理削除
	 * @param requests
	 * @return 削除件数
	 */
	int ikkatsuDeleteShohin(List<ShohinRequest> requests);

	/**
	 * 商品情報の取得
	 * @param shohinCd
	 * @return
	 */
	ShohinEntity findByShohin(@Param("shohinCd") String shohinCd);

	/**
	 * 商品情報の更新
	 * @param shohinEntity
	 * @return
	 */
	int updateShohin(ShohinEntity shohinEntity);

	/**
	 * 次の商品コードの末尾を生成するため、最新の末尾取得
	 * @param themType
	 * @param seasonCd
	 * @return
	 */
	String findThemeAndSeason(@Param("themeType") String themeType, @Param("seasonCd") String seasonCd);

	/**
	 * テーマに紐づく季節
	 * @param themeType
	 * @return
	 */
	List<String> selectRelationToTheme(@Param("themeType") String themeType);
	
}
