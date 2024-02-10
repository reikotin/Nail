package com.reiko.nail.dto;

import java.time.LocalDate;
import java.util.List;

import com.reiko.nail.entity.ShohinEntity;

import lombok.Data;

@Data
public class EditDenpyoDto {

	/** 伝票番号 */
	private String denpyoNo;
	/** お客様CD */
	private String customerCd;
	/** お客様　姓 */
	private String customerSei;
	/** お客様 名 */
	private String customerMei;
	/** 姓（フリガナ） */
	private String seiFurigana;
	/** 名（フリガナ） */
	private String meiFurigana;
	/** 購入日 */
	private LocalDate kounyuDate;
	/** 伝票発行済みフラグ */
	private String denpyoHakkozumiFlag;
	/** 郵便番号 */
	private String yubinNo;
	/** 都道府県・市区町村 */
	private String prefectureCity;
	/** 番地・その他住所 */
	private String streetNo;
	/** 最終更新日(発送日) */
	private LocalDate hassoDate;
	/** 備考 */
	private String biko;
	/** 合計金額 */
	private Long gokeiKingaku;
	/** 発送方法 */
	private String hassoHoho;
	/** 追跡番号 */
	private String tsuisekiNo;
	/** 発送料 */
	private Integer hassoRyo;
	/** 商品情報 */
	List<ShohinEntity> shohinJoho;
	/** ステータス */
	private String status;
}
