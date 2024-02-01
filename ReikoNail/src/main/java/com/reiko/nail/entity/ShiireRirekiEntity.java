package com.reiko.nail.entity;


import java.time.LocalDate;

import lombok.Data;

@Data
public class ShiireRirekiEntity {

	/** 仕入ID */
	private String shiireId;
	/** 仕入日 */
	private LocalDate shiireDate;
	/** 数量 */
	private int suryo;
	/** 登録日 */
	private LocalDate createDate;
	
}
