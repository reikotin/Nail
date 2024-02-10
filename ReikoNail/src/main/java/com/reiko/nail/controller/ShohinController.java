package com.reiko.nail.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.SearchItemDto;
import com.reiko.nail.dto.SearchShiireDto;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.entity.ShiireEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.service.ShohinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShohinController {
	
	private final ShohinService shohinService;
	
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
		
//		System.out.println("大 : " + daiBunrui + " 小 : " + shoBunrui);		
		List<ShiireEntity> itemNameList = shohinService.getItemNameList(daiBunrui, shoBunrui);
		
		return new ResponseEntity<List<ShiireEntity>>(itemNameList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/SearchShiireItem", method = {RequestMethod.GET})
	public ResponseEntity<List<ShiireEntity>> getShiireItem(@ModelAttribute SearchShiireDto searchShiireDto){
		
		List<ShiireEntity> list = new ArrayList<>();
		
		list = shohinService.searchShiireList(searchShiireDto);	
		
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
	
//	@RequestMapping(value = "/Valid", method = {RequestMethod.POST})
//	public ResponseEntity<ShiireEntity> valid(@RequestBody ShiireEntity shiireEntity){
//		System.out.println("test");
//		shiireEntity.setItemName("上書き");
//		System.out.println(shiireEntity);
//		
//		return new ResponseEntity<ShiireEntity>(shiireEntity, HttpStatus.OK);
//	}

}
