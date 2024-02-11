package com.reiko.nail.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.reiko.nail.dto.DenpyoDto;
import com.reiko.nail.dto.EditDenpyoDto;
import com.reiko.nail.dto.ExportDenpyo;
import com.reiko.nail.entity.DenpyoEntity;
import com.reiko.nail.entity.ShohinEntity;

@Mapper
public interface DenpyoDao {

	List<DenpyoDto> selectByAllDenpyoList();

	int countDenpyoMaisu(@Param("kounyuDate") LocalDate kounyuDate);

	int insertDenpyo(@Param("zeinukiGaku") int zeinukiGaku, 
			@Param("zeiGaku") int zeiGaku, @Param("zeikomiGaku") int zeikomiGaku, 
			@Param("customerCd") String customerCd, @Param("denpyoNo") String denpyoNo, 
			@Param("kounyuDate") LocalDate kounyuDate, @Param("biko") String biko);
	
	/**
	 * 領収書兼納品書用伝票情報の取得
	 * @param denpyoNo
	 * @return
	 */
	List<ExportDenpyo> exportDenpyo(@Param("denpyoNo") String denpyoNo);
	
	/**
	 * 編集用伝票情報の取得(お客様情報の取得)
	 * @param denpyoNo
	 * @return
	 */
	EditDenpyoDto selectByEditDenpyoForCustomerJoho(@Param("denpyoNo") String denpyoNo);
	
	/**
	 * 編集用伝票情報の取得(商品情報の取得)
	 * @param denpyoNo
	 * @return
	 */
	List<ShohinEntity> selectByEditDenpyoForShohinJoho(@Param("denpyoNo") String denpyoNo);
	
	/**
	 * 伝票の論理削除
	 * @param denpyoNo
	 * @return 更新件数
	 */
	int deleteDenpyoByPrimary(@Param("denpyoNo") String denpyoNo);
	
	/**
	 * 伝票の金額情報変更あり
	 * @param denpyoEntity
	 * @return
	 */
	int updateDenpyoByKingakuHenko(DenpyoEntity denpyoEntity);
	
	/**
	 * 伝票の金額情報変更なし
	 * @param denpyoEntity
	 * @return
	 */
	int updateDenpyoByNotKingakuHenko(DenpyoEntity denpyoEntity);
}
