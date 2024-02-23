package com.reiko.nail.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EditShiireDto {

	/** 仕入ID */
	private String shiireId;
	/** 大分類名称 */
	@NotBlank(message = "大分類が未入力です。")
	private String daiBunruiName;
	/** 小分類名称 */
	@NotBlank(message = "小分類が未入力です。")
	private String shoBunruiName;
	/** 税抜額 */
	@NotNull(message = "税抜額を入力して下さい。")
	@Min(value = 1, message = "税抜額は1以上で入力して下さい。")
	@Max(value = 99999, message = "税抜額は99999以下で入力してください。")
	private Integer zeinukiGaku;
	/** 税額 */
	@NotNull(message = "税額を入力して下さい。")
	@Min(value = 1, message = "税額は1以上で入力して下さい。")
	@Max(value = 99999, message = "税額は99999以下で入力してください。")
	private Integer zeiGaku;
	/** 税込額 */
	@NotNull(message = "税込額を入力して下さい。")
	@Min(value = 1, message = "税込額は1以上で入力して下さい。")
	@Max(value = 99999, message = "税込額は99999以下で入力してください。")
	private Integer zeikomiGaku;
	/** 仕入先 */
	private String shiireSaki;
	/** 仕入メーカー */
	private String shiireMaker;
	/** サイズ */
	private String itemSize;
	/** 品名 */
	@NotBlank(message = "品名が未入力です。")
	@Pattern(regexp = "^[^!@#$%^&*()_+={}\\[\\]|\\\\:;\"'<>,.?/~]*$", message = "品名に「`!@#$%^&*()_+={}[]|:;\"'<>,.?/~」の記号は使用できません。")
	private String itemName;
	/** 品番/JANCD */
	private String janCd;
	/** 履歴ID */
	private String rirekiId;
	/** 数量 */
	@NotNull(message = "数量を入力して下さい。")
	@Min(value = 1, message = "数量は1以上で入力して下さい。")
	@Max(value = 99999, message = "数量は99999以下で入力してください。")
	private Integer suryo;
	/** 仕入日 */
	@NotNull(message = "仕入日が未入力です。")
	private LocalDate shiireDate;
}
