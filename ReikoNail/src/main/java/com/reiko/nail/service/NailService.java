package com.reiko.nail.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.reiko.nail.dao.BunruiDao;
import com.reiko.nail.dao.DenpyoDao;
import com.reiko.nail.dao.SalesDao;
import com.reiko.nail.dao.ShohinDao;
import com.reiko.nail.dto.BunruiDto;
import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.DenpyoDto;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.entity.BunruiEntity;
import com.reiko.nail.entity.DenpyoEntity;
import com.reiko.nail.entity.SalesEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.enums.DaiBunruiEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NailService {
	
	private final DenpyoDao denpyoDao;
	
	private final ShohinDao shohinDao;
	
	private final SalesDao salesDao;
	
	private final BunruiDao bunruiDao;

	public List<DenpyoEntity> selectAllDenpyoList() {
		
		return denpyoDao.selectByAllDenpyoList();
		
	}
	
	public List<ShohinEntity> getShohinList(){
		List<ShohinEntity> shohinList = shohinDao.selectAllShohinList();
		
		return shohinList;
	}


	// 伝票登録
	public void saveDenpyo(DenpyoDto denpyoDto) {
		/** 購入日の伝票枚数を取得し、伝票番号に変換 */
		int denpyoMaisu = denpyoDao.countDenpyoMaisu(denpyoDto.getKounyuDate());
		String denpyoDate = denpyoDto.getKounyuDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
		String denpyoNo = denpyoDate + String.format("%04d", denpyoMaisu);
		
		saveMeisai(denpyoDto, denpyoNo);
		
		Integer zeinukiGaku = 0;
		Integer zeiGaku = 0;
		Integer zeikomiGaku = 0;

		for(ShohinDto shohinDto : denpyoDto.getShohinDto()) {
			zeinukiGaku += shohinDto.getZeinukiGaku();
			zeiGaku += shohinDto.getZeiGaku();
			zeikomiGaku += shohinDto.getZeikomiGaku();
		}
		
		denpyoDao.insertDenpyo(zeinukiGaku, zeiGaku, zeikomiGaku, denpyoDto.getCustomerCd(), denpyoNo, denpyoDto.getKounyuDate(), denpyoDto.getBiko());
		
	}
	
	// 販売明細登録
	private void saveMeisai(DenpyoDto denpyoDto, String denpyoNo) {
		
		List<SalesEntity> salesList = new ArrayList<>();
		for(ShohinDto shohin : denpyoDto.getShohinDto()) {
			SalesEntity entity = new SalesEntity();
			entity.setDenpyoNo(denpyoNo);
			entity.setShohinCd(shohin.getShohinCd());
			entity.setKounyuDate(denpyoDto.getKounyuDate());
			entity.setCustomerCd(denpyoDto.getCustomerCd());
			salesList.add(entity);
		}
		
		salesDao.registryMeisai(salesList);
		
	}
	
	// すべての分類を取得
	public List<BunruiDto> allBunruiList(){
		
		List<BunruiDto> bunruiDto = new ArrayList<>();
		
		bunruiDto = bunruiDao.selectAllBunrui();
		
		
		return bunruiDto;
		
	}
	
	// 分類の登録
	public void countBunrui(BunruiDto bunruiDto) {
		
		String daiBunruiName = bunruiDto.getDaiBunrui().getKey();
		
		int result = bunruiDao.selectCountBunrui(daiBunruiName);
		
		String daiBunruiCd = bunruiDto.getDaiBunrui().name().substring(0,2);
		
		String bunruiCd = generateBunruiCd(daiBunruiCd, result);
		
		BunruiEntity entity = new BunruiEntity();
		entity.setBunruiCd(bunruiCd);
		entity.setDaiBunruiName(daiBunruiName);
		entity.setShoBunruiName(bunruiDto.getShoBunrui());
		
		
		bunruiDao.insertBunrui(entity);
		
		
	}
	
	private String generateBunruiCd(String daiBunrui,int cdNo) {
		String bunruiNo = String.format("%03d", cdNo + 1);
		
		return daiBunrui + randomStringGenerator() + bunruiNo;
	}
	
	private String randomStringGenerator() {
	
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int length = 2; // 生成する文字列の長さ

        for(int i = 0; i < length; i++) {
            char randomChar = (char) ('A' + random.nextInt(26)); 
            stringBuilder.append(randomChar);
        }
        
        return stringBuilder.toString();
	
	}
	
	public List<BunruiNameDto> getDaiBunrui(){
		
		List<BunruiNameDto> daiBunruiList = new ArrayList<>();
		for(DaiBunruiEnum daiBunrui : DaiBunruiEnum.values()) {
			BunruiNameDto dto = new BunruiNameDto();
			dto.setBunruiName(daiBunrui.getKey());
			daiBunruiList.add(dto);
		}
		
		return daiBunruiList;
	}
}