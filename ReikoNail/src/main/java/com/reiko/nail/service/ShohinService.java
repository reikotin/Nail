package com.reiko.nail.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.reiko.nail.dao.BunruiDao;
import com.reiko.nail.dao.ShohinDao;
import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.SearchItemDto;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.repository.ShohinRepository;
import com.reiko.nail.response.ResponseData;
import com.reiko.nail.util.Constants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShohinService {
	
	private final ShohinDao shohinDao;
	
	private final ShohinRepository shohinRepository;
	
	private final BunruiDao bunruiDao;

	// 商品の情報取得
	public ShohinDto getShohinKingaku(String shohinCd) {
		
		ShohinDto dto = shohinDao.selectByShohinKingaku(shohinCd);
		
		if(ObjectUtils.isNotEmpty(dto)) {
			return dto;
		}
		return null;
	}

	// 商品の検索
	public ResponseData<SearchItemDto> searchItemList(SearchItemDto searchItemDto) {
		ResponseData<SearchItemDto> res = new ResponseData<SearchItemDto>();
		
		if(StringUtils.equals(searchItemDto.getColor(), "0")) {
			searchItemDto.setColor(null);
		}

		if(StringUtils.equals(searchItemDto.getShohinCd(), "0")) {
			searchItemDto.setShohinCd(null);
		}
		
		res.setData(searchItemDto);
		return res;
	}

	// 商品検索
	public List<ShohinEntity> getSearchItemList(SearchItemDto data) {
		return shohinDao.searchItemList(data);
	}

	// 新商品の登録
	public String registryNewItem(ShohinDto shohinDto) {
		// 商品コードの番号生成
		int lastShohinCd = shohinRepository.findByLastShohinCd(jpColorHenkan(shohinDto.getColor()));
		String shohinNo = String.format("%03d", lastShohinCd + 1);
		
		// 税額、税込額計算
		int zeinukiGaku = shohinDto.getZeinukiGaku();
		int zeiGaku = calcZeigaku(zeinukiGaku);
		
		// カラーコードを漢字に変換
		String jpColor = jpColorHenkan(shohinDto.getColor());
		
		// 登録商品の情報をセット
		ShohinEntity entity = new ShohinEntity();
		entity.setShohinCd(shohinDto.getColor() + shohinNo);
		entity.setColor(jpColor);
		entity.setZeinukiGaku(shohinDto.getZeinukiGaku());
		entity.setZeiGaku(zeiGaku);
		entity.setZeikomiGaku(zeiGaku + zeinukiGaku);
		entity.setMakeTime(StringUtils.isEmpty(shohinDto.getMakeTime()) ? null : shohinDto.getMakeTime());
		entity.setZairyoMemo(StringUtils.isEmpty(shohinDto.getZairyoMemo()) ? null : shohinDto.getZairyoMemo());
		entity.setShohinMemo(StringUtils.isEmpty(shohinDto.getShohinMemo()) ? null : shohinDto.getShohinMemo());
		shohinDao.insertNewItem(entity);
		
		return  shohinDto.getColor() + shohinNo;
	}
	
	// カラーコードを漢字に変換
	private String jpColorHenkan(String color) {
		String jpColor = null;
		
		switch (color) {
		case Constants.PINK:
			jpColor = "桃";
			break;
		case Constants.BLUE:
			jpColor = "青";
			break;
		case Constants.DEEP_BLUES:
			jpColor = "紺";
			break;
		case Constants.RED:
			jpColor = "赤";
			break;
		case Constants.YELLOW:
			jpColor = "黄";
			break;
		case Constants.GREEN:
			jpColor = "緑";
			break;
		case Constants.VIOLET:
			jpColor = "紫";
			break;
		case Constants.BLACK:
			jpColor = "黒";
			break;
		case Constants.WHITE:
			jpColor = "白";
			break;
		case Constants.ORANGE:
			jpColor = "橙";
			break;
		case Constants.BROWN:
			jpColor = "茶";
			break;
		case Constants.GOLD:
			jpColor = "金";
			break;
		case Constants.SILVER:
			jpColor = "銀";
			break;
		case Constants.VARIOUS:
			jpColor = "その他";
			break;
		}
		return jpColor;
	}
	
	// 税額計算
	private int calcZeigaku(int zeinukiGaku) {
		return zeinukiGaku / 10;
	}

	// 商品論理削除
	public int deleteItem(String shohinCd) {
		return shohinDao.deleteShohin(shohinCd);
	}

	// 該当の商品情報取得
	public ShohinEntity getShohinInfo(String shohinCd) {
		
		return shohinDao.findByShohin(shohinCd);
	}

	// 商品情報の編集
	public int updateItem(ShohinEntity shohinEntity) {
		
		int zeinukiGaku = shohinEntity.getZeinukiGaku();
		int zeiGaku = calcZeigaku(zeinukiGaku);
		shohinEntity.setZeiGaku(zeiGaku);
		shohinEntity.setZeikomiGaku(zeiGaku + zeinukiGaku);
		return shohinDao.updateShohin(shohinEntity);
	}
	
	
	public List<BunruiNameDto> getShoBunruiList(String daiBunruiName) {
		return bunruiDao.relationByDaiBunrui(daiBunruiName);
	}
}
