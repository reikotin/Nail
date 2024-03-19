package com.reiko.nail.enums;

public enum DaiBunruiEnum {

	PARTS("パーツ","PA"),
	TIP("チップ", "TI"),
	KONPOUZAI("梱包材", "KO"),
	COLOR("カラー", "CO"),
	BRUSH("ブラシ", "BR"),
	YASURI("ヤスリ", "YA"),
	LED("LED", "LE"),
	CLEANER("クリーナー", "CL"),
	KAKOUDOUGU("加工道具","KA")
	;
	

	private final String key;
	private final String value;
	
	private DaiBunruiEnum(final String key, final String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getValue() {
		return this.value;
	}

	
	
}
