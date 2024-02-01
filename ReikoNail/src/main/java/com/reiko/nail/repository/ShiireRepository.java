package com.reiko.nail.repository;

import org.springframework.stereotype.Repository;

import com.reiko.nail.dao.RirekiDao;
import com.reiko.nail.dao.ShiireDao;
import com.reiko.nail.dto.ShiireDto;
import com.reiko.nail.entity.ShiireEntity;
import com.reiko.nail.entity.ShiireRirekiEntity;
import com.reiko.nail.response.ResponseData;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ShiireRepository {
	
	private final ShiireDao shiireDao;
	
	private final RirekiDao rirekiDao;
	
	
	public ResponseData<ShiireEntity> registoryShiire(ShiireDto shiireDto) {
		ResponseData<ShiireEntity> response = new ResponseData<>();
		// 仕入情報
		ShiireEntity shiireEntity = new ShiireEntity();
		shiireEntity.setShiireId(shiireDto.getShiireId());
		shiireEntity.setDaiBunruiName(shiireDto.getDaiBunruiName());
		shiireEntity.setShoBunruiName(shiireDto.getShoBunruiName());
		shiireEntity.setItemName(shiireDto.getItemName());
		shiireEntity.setZeinukiGaku(shiireDto.getZeinukiGaku());
		shiireEntity.setZeiGaku(shiireDto.getZeiGaku());
		shiireEntity.setZeikomiGaku(shiireDto.getZeikomiGaku());
		shiireEntity.setShiireSaki(shiireDto.getShiireSaki());
		shiireEntity.setShiireMaker(shiireDto.getShiireMaker());
		shiireEntity.setJanCd(shiireDto.getJanCd());
		shiireEntity.setItemSize(shiireDto.getItemSize());
		
		// 履歴情報
		ShiireRirekiEntity rirekiEntity = new ShiireRirekiEntity();
		rirekiEntity.setShiireId(shiireDto.getShiireId());
		rirekiEntity.setShiireDate(shiireDto.getShiireDate());
		rirekiEntity.setSuryo(shiireDto.getSuryo());
		
		try {
			shiireDao.insertShiireItem(shiireEntity);
			rirekiDao.insertShiireRireki(rirekiEntity);
			response.setData(shiireEntity);
			response.setMessage("正常に登録完了しました");
		} catch (Exception e) {
			e.printStackTrace();
			response.setHasError(true);
			response.setData(shiireEntity);
			response.setMessage("エラーが発生しました。");
		}
		
		return response;
		
	}

}
