package com.reiko.nail.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CustomerEntity {

	/** 顧客CD */
	private String customerCd;
	/** 姓 */
	private String customerSei;
	/** 名 */
	private String customerMei;
	/** 姓（フリガナ） */
	private String seiFurigana;
	/** 名（フリガナ） */
	private String meiFurigana;
	/** 郵便番号 */
	private String yubinNo;
	/** 都道府県・市区町村 */
	private String prefectureCity;
	/** 番地・その他住所 */
	private String streetNo;
	/** 購入回数 */
	private int kounyuKaisu;
	/** 累計購入金額 */
	private Long ruikeiKounyuKingaku;
	/** 登録日 */
	private LocalDate createDate;
	/** 更新日 */
	private LocalDate updateDate;

}
