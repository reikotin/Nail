package com.reiko.nail.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.SearchItemDto;
import com.reiko.nail.dto.SearchShiireDto;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.dto.ShohinRequest;
import com.reiko.nail.entity.ShiireEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.response.ResponseData;
import com.reiko.nail.service.MessageService;
import com.reiko.nail.service.ShohinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShohinController {
	
	private final static Logger logger = LogManager.getLogger(ShohinController.class);
	private final ShohinService shohinService;
	private final MessageService messageService;
	
	@RequestMapping(value = "/getShohinInfo", method = {RequestMethod.GET})
	public ResponseEntity<ShohinDto> getShohinInfo(@RequestParam String shohinCd){
		if(StringUtils.isEmpty(shohinCd)) {
			return ResponseEntity.ok(null);
		}		
		ShohinDto shohinDto = shohinService.getShohinKingaku(shohinCd);
		return new ResponseEntity<ShohinDto>(shohinDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/GetShoBunrui", method = {RequestMethod.GET})
	public ResponseEntity<List<BunruiNameDto>> getShoBunrui(@RequestParam String daiBunruiName, String searchKbn){
		
		List<BunruiNameDto> shoBunruiList = shohinService.getShoBunruiList(daiBunruiName, searchKbn);
		
		if(StringUtils.equals(searchKbn, "2")){
			BunruiNameDto dto = new BunruiNameDto();
			dto.setBunruiName("");
			shoBunruiList.add(0, dto);
		}
				
		if(shoBunruiList.size() == 0) {
			return ResponseEntity.ok(null);
		}
		
		return new ResponseEntity<List<BunruiNameDto>>(shoBunruiList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/GetItemNameList", method = {RequestMethod.GET})
	public ResponseEntity<List<ShiireEntity>> getItemName(@RequestParam String daiBunrui, String shoBunrui){
		List<ShiireEntity> itemNameList = shohinService.getItemNameList(daiBunrui, shoBunrui);
		return new ResponseEntity<List<ShiireEntity>>(itemNameList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/SearchShiireItem", method = {RequestMethod.GET})
	public ResponseEntity<List<ShiireEntity>> getShiireItem(@ModelAttribute SearchShiireDto searchShiireDto){
		
		List<ShiireEntity> list = new ArrayList<>();
		
		list = shohinService.searchShiireForNewShiire(searchShiireDto);	
		
		return new ResponseEntity<List<ShiireEntity>>(list, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/GetSeasonList", method = {RequestMethod.GET})
	public ResponseEntity<List<String>> getSeasonList(@RequestParam String theme){
		
		if(StringUtils.isEmpty(theme)) {
			
			return ResponseEntity.ok(null);
		}
		
		List<String> resultList = shohinService.relationItemToTheme(theme);	
		
		return new ResponseEntity<List<String>>(resultList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/GetShohinList", method = {RequestMethod.GET})
	public ResponseEntity<List<ShohinEntity>> getShohinList(@ModelAttribute SearchItemDto searchItemDto){
		
		List<ShohinEntity> list	= shohinService.getSearchItemList(searchItemDto);
		return new ResponseEntity<List<ShohinEntity>>(list, HttpStatus.OK);
	}
	
	/**
	 * 商品一括削除
	 * @param requestList
	 * @return resultメッセージ
	 */
	@RequestMapping(value = "/IkkatsuDeleteShohin", method = {RequestMethod.POST})
	public ResponseEntity<ResponseData<String>> ikkatsuDeleteShohin(@RequestBody List<ShohinRequest> requestList){
		ResponseData<String> res = new ResponseData<>();
		logger.info(messageService.getMessage("proccess.Start", new String[] {"商品一括削除"}));
		
		int deleteCount = shohinService.ikkatsuDeleteShohin(requestList);
		if(deleteCount > 0) {
			logger.info(messageService.getMessage("delete.data.count", new String[] {"商品", Integer.toString(deleteCount)}));
			res.setData(messageService.getMessage("delete.success", null));
			res.setHasError(false);
			res.setMessage(Integer.toString(deleteCount) + "件の商品を削除しました。");
			res.setData(messageService.getMessage("delete.success", null));
		} else {
			res.setHasError(true);
			res.setMessage("管理者に報告してください。");
			res.setData("商品一括削除に失敗しました。");
		}
		
		logger.info(messageService.getMessage("proccess.End", new String[] {"商品一括削除"}));
		
		
		return new ResponseEntity<ResponseData<String>>(res, HttpStatus.OK);
	}

}
