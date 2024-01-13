package com.reiko.nail.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.ShohinDto;
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
	public ResponseEntity<List<BunruiNameDto>> getShoBunrui(@RequestParam String daiBunruiName){
		
		List<BunruiNameDto> shoBunruiList = shohinService.getShoBunruiList(daiBunruiName);
		if(shoBunruiList.size() == 0) {
			return ResponseEntity.ok(null);
		}
		
		return new ResponseEntity<List<BunruiNameDto>>(shoBunruiList, HttpStatus.OK);
	}

}
