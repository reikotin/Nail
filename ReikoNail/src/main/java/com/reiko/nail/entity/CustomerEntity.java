package com.reiko.nail.entity;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerEntity {

	/** お客様コード */
	private String customerCd;
	/** 姓 */
	@NotBlank(message = "姓を入力して下さい。")
	private String customerSei;
	/** 名 */
	@NotBlank(message = "名を入力して下さい。")
	private String customerMei;
	/** 姓（フリガナ） */
	@NotBlank(message = "姓（フリガナ）を入力して下さい")
	private String seiFurigana;
	/** 名（フリガナ） */
	@NotBlank(message = "名（フリガナ）を入力して下さい")
	private String meiFurigana;
	/** 郵便番号 */
	@NotBlank(message = "郵便番号を入力して下さい")
	@Pattern(regexp = "^(\\d{7})$", message = "「1234567」の形式で入力してください。")
	private String yubinNo;
	/** 都道府県・市区町村 */
	@NotBlank(message = "都道府県・市区町村を入力して下さい")
	private String prefectureCity;
	/** 番地・その他住所 */
	@NotBlank(message = "番地を入力して下さい")
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
