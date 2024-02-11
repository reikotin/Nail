package com.reiko.nail.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DenpyoDto {
//
//	/** 伝票番号 */
//	private String denpyoNo;
//	/** お客様CD */
//	private String customerCd;
//	/** お客様　姓 */
//	private String customerSei;
//	/** お客様 名 */
//	private String customerMei;
//	/** 姓（フリガナ） */
//	private String seiFurigana;
//	/** 名（フリガナ） */
//	private String meiFurigana;
//	/** 商品情報 */
//	private List<ShohinDto> shohinDto;
//	/** 購入日 */
//	private LocalDate kounyuDate;
//	/** 郵便番号 */
//	private String yubinNo;
//	/** 都道府県・市区町村 */
//	private String prefectureCity;
//	/** 番地・その他住所 */
//	private String streetNo;
//	/** 備考 */
//	private String biko;
//	/** 発送方法 */
//	private String hassoHoho;
//	/** 追跡番号 */
//	private String tsuisekiNo;
//	/** 発送料 */
//	private Integer hassoRyo;
//	/** お客様情報修正区分 */
//	private boolean customerJohoHenshu;
	private String denpyoNo;
	private String denpyoHakkozumiFlag;
	private LocalDate kounyuDate;
	private LocalDate hassoDate;
	private String hassoHoho;
	private LocalDate updateDate;
	private Integer zeikomiGaku;
	private String customerSei;
	private String customerMei;
}
