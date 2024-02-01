package com.reiko.nail.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reiko.nail.dao.BunruiDao;
import com.reiko.nail.dao.ShiireDao;
import com.reiko.nail.dao.ShohinDao;
import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.SearchItemDto;
import com.reiko.nail.dto.SearchShiireDto;
import com.reiko.nail.dto.ShiireDto;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.dto.ShohinIdDto;
import com.reiko.nail.entity.ShiireEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.enums.DaiBunruiEnum;
import com.reiko.nail.enums.SeasonEnum;
import com.reiko.nail.enums.ThemeTypeEnum;
import com.reiko.nail.repository.ShiireRepository;
import com.reiko.nail.repository.ShohinRepository;
import com.reiko.nail.response.ResponseData;
import com.reiko.nail.util.Constants;

import lombok.RequiredArgsConstructor;
@Transactional
@Service
@RequiredArgsConstructor
public class ShohinService {
	
	private final ShohinDao shohinDao;
	
	private final ShohinRepository shohinRepository;
	
	private final BunruiDao bunruiDao;
	
	private final ShiireDao shiireDao;
	
	private final ShiireRepository shiireRepository;

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
		
	
		
		res.setData(searchItemDto);
		return res;
	}

	// 商品検索
	public List<ShohinEntity> getSearchItemList(SearchItemDto data) {
		return shohinDao.searchItemList(data);
	}

	// 新商品の登録
	public String registryNewItem(ShohinDto shohinDto) {
		// P-WI24-O1
		
		ThemeTypeEnum themeType = shohinDto.getThemeType();
		SeasonEnum season = shohinDto.getSeason();
		int nowYear = LocalDate.now().getYear();
		String seasonCd = season.getValue() + Integer.toString(nowYear).substring(2);
		
    	//商品コードの生成
		String newShohinCd = createNewShohinCd(themeType, seasonCd);
		// 税額、税込額計算
		int zeinukiGaku = shohinDto.getZeinukiGaku();
		int zeiGaku = calcZeigaku(zeinukiGaku);
		
		String shiireIdList = null;
		if(shohinDto.getIdList().size() > 0 ){
			shiireIdList = createIdList(shohinDto.getIdList());
		}
		
		// 登録商品の情報をセット
		ShohinEntity entity = new ShohinEntity();
		entity.setShohinCd(newShohinCd);
		entity.setThemeType(themeType.getKey());
		entity.setSeason(seasonCd);
		entity.setZeinukiGaku(zeinukiGaku);
		entity.setZeiGaku(zeiGaku);
		entity.setZeikomiGaku(zeiGaku + zeinukiGaku);
		entity.setShiireIdList(shiireIdList);
		entity.setMakeTime(StringUtils.isEmpty(shohinDto.getMakeTime()) ? null : shohinDto.getMakeTime());
		entity.setShohinMemo(StringUtils.isEmpty(shohinDto.getShohinMemo()) ? null : shohinDto.getShohinMemo());
		shohinDao.insertNewItem(entity);
		
		return  newShohinCd;
				
	}
	// 新しい商品コードの生成
	private String createNewShohinCd(ThemeTypeEnum themeType, String seasonCd) {

		int nowNumber = shohinRepository.getNextCdThemeAndSeason(themeType.getKey(), seasonCd);
		String nextNumber = String.format("%02d", nowNumber + 1);
		String newCd = themeType.getValue() + "-" + seasonCd + "-" + nextNumber;	
		return newCd;
	}
	// IDリストを文字列に変換
	private String createIdList(List<ShohinIdDto> idList) {
		StringBuilder ids = new StringBuilder();
	    for (int i = 0; i < idList.size(); i++) {
	    	String id = idList.get(i).getId();
	    	if(StringUtils.isNotEmpty(id)) {
	    		ids.append(id);
		    	if (i < idList.size() - 1) {
		    		ids.append("_");
		    	}
	    	}
	    }
		return ids.toString();
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
		
		List<BunruiNameDto> dtoList = bunruiDao.relationByDaiBunrui(daiBunruiName);
		BunruiNameDto dto = new BunruiNameDto();
		dto.setBunruiName(Constants.ALL);
		dtoList.add(0, dto);
		return dtoList;
	}
	
	// 仕入商品情報の保存
	public ResponseData<ShiireEntity> saveShiireItem(ShiireDto shiireDto) {
		
		ResponseData<ShiireEntity> response = new ResponseData<>();
		
		// 仕入IDがある場合は更新処理
		if(StringUtils.isNotEmpty(shiireDto.getShiireId())) {
			
			response = shiireRepository.registoryShiire(shiireDto);
			
			return response;
		} 
		ShiireEntity entity = shiireDao.findShiireJohoByJanCd(shiireDto.getJanCd());
		// 仕入IDがないが、登録済みのJAN_CDがある場合
		if(ObjectUtils.isNotEmpty(entity)) {
				
			response.setHasShiire(true);
			response.setData(entity);
			response.setMessage("品番が同じ仕入情報があります。");
			return response;
			
		// 仕入IDもJAN_CDでも同じものがない場合(新規仕入情報登録となる)
		} else {
			String newShiireId = String.format("%05d", shiireDao.countId() + 1);
			shiireDto.setShiireId(newShiireId);
			response = shiireRepository.registoryShiire(shiireDto);
			return response;
		}
		
	}
	
	private int calcZeikomiToZeigaku(int zeikomiGaku) {
		return (int) (zeikomiGaku / 1.1);
	}
	
	public boolean dayService(LocalDate startDate, LocalDate endDate) {
		boolean isAfter = false;
		
		if(startDate == null && endDate == null) {
			return isAfter;
		} else if(startDate != null && endDate != null) {
			isAfter = startDate.isAfter(endDate);
		} 
		
		return isAfter;
	}
	
	// 仕入情報検索
	public List<ShiireEntity> searchShiireList(SearchShiireDto searchShiireDto) {
		if(StringUtils.isEmpty(searchShiireDto.getDaiBunruiName())) {
			searchShiireDto.setDaiBunruiName(null);
		}
		
		if(StringUtils.isEmpty(searchShiireDto.getJanCd())) {
			searchShiireDto.setJanCd(null);
		}
		
		if(StringUtils.equals(searchShiireDto.getShoBunruiName(), Constants.ALL) 
				|| StringUtils.isEmpty(searchShiireDto.getShoBunruiName())){
			searchShiireDto.setShoBunruiName(null);
		}
		
		return shiireDao.searchShiireItemList(searchShiireDto);
	}
	
	// 仕入情報取得（大分類がカラーと、パーツのみを取得）
	public List<ShiireEntity> getColorAndPartsShiireList(){
		List<ShiireEntity> resultList = 
				shiireDao.selectByColorAndPartsShiireList(DaiBunruiEnum.COLOR.getKey(), DaiBunruiEnum.PARTS.getKey());
		
		return resultList;
	}
	
	// 仕入品名リストの取得
	public List<ShiireEntity> getItemNameList(String daiBunruiName, String shoBunruiName){
		
		if(StringUtils.equals(shoBunruiName, Constants.ALL)) {
			shoBunruiName = null;
		}
				
		List<ShiireEntity> itemNameList = shiireDao.selectByItemNameList(daiBunruiName, shoBunruiName);
		
		return itemNameList;
	}
}
