package com.reiko.nail.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class SalesEntity {

	/** 販売ID */
	private BigDecimal salesId;
	/** 伝票番号 */
	private String denpyoNo;
	/** 商品CD */
	private String shohinCd;
	/** 購入日 */
	private LocalDate kounyuDate;
	/** お客様CD */
	private String customerCd;
    /** 登録日 */
	private LocalDate createDate;
}
