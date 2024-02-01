package com.reiko.nail.controller;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reiko.nail.dto.BunruiDto;
import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.DenpyoDto;
import com.reiko.nail.dto.SearchItemDto;
import com.reiko.nail.dto.SearchShiireDto;
import com.reiko.nail.dto.ShiireDto;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.entity.CustomerEntity;
import com.reiko.nail.entity.DenpyoEntity;
import com.reiko.nail.entity.ShiireEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.enums.DaiBunruiEnum;
import com.reiko.nail.enums.SeasonEnum;
import com.reiko.nail.enums.ThemeTypeEnum;
import com.reiko.nail.response.ResponseData;
import com.reiko.nail.service.CustomerService;
import com.reiko.nail.service.NailService;
import com.reiko.nail.service.ShohinService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NailController {
	
	private final NailService nailService;
	
	private final ShohinService shohinService;
	
	private final CustomerService customerService;

	@RequestMapping(value = "/", method = {RequestMethod.GET})
	public String show(Model model) {
		
		List<DenpyoEntity> denpyoList = nailService.selectAllDenpyoList();
		
		model.addAttribute("denpyoList", denpyoList);
	
		return "Home";
	}
	
	@RequestMapping(value = "/NewDenpyo", method = {RequestMethod.GET})
	public String newDenpyo(Model model) {
		
		List<ShohinEntity> shohinList = nailService.getShohinList();
		List<CustomerEntity> customerList = customerService.getAllCustomer();
		
		DenpyoDto denpyoDto = new DenpyoDto();
		
		model.addAttribute("customerList", customerList);
		model.addAttribute("shohinList", shohinList);
		model.addAttribute("denpyoDto", denpyoDto);
		
		return "NewDenpyo";
	}
	
	@RequestMapping(value = "/Save", method = {RequestMethod.POST})
	public String saveDenpyo(@ModelAttribute DenpyoDto denpyoDto, Model model) {
		
		if(denpyoDto.isCustomerJohoHenshu()) {
			customerService.updateCustomerJoho(denpyoDto);
		}
//		int result = customerService.findByCustomer(denpyoDto.getCustomerNameSei(), denpyoDto.getCustomerNameMei());
//		if(result == 0) {
//			customerService.insertNewCustomer(denpyoDto);
//		}
		nailService.saveDenpyo(denpyoDto);
		
		String message = "登録完了しました。";
		model.addAttribute("url", "NewDenpyo");
		model.addAttribute("message", message);
		return "kanryo";
	}
	
	@RequestMapping(value = "/ItemIndex", method = {RequestMethod.GET})
	public String itemIndex(Model model){

		List<String> themeList = new ArrayList<>();
		for(ThemeTypeEnum theme	 : ThemeTypeEnum.values()) {
			themeList.add(theme.getKey());
		}
		themeList.add(0, "");
		
		
//		List<ShohinEntity> shohinCdList = nailService.getShohinList();
//		List<ShohinEntity> shohinList = studyService.getShohinList();
		SearchItemDto searchItem = new SearchItemDto();
//		model.addAttribute("shohinCdList", shohinCdList);
		model.addAttribute("themeList", themeList);
		model.addAttribute("searchItem", searchItem);

		return "ItemIndex";
	}
	
	@RequestMapping(value = "/ItemIndex", method = {RequestMethod.POST})
	public String searchItem(SearchItemDto searchItemDto, Model model) {
		
		String message = null;
		
		List<ShohinEntity> shohinList = new ArrayList<>();
		SearchItemDto searchItem = new SearchItemDto();
		ResponseData<SearchItemDto> response = new ResponseData<SearchItemDto>();
		
		if(StringUtils.equals(searchItemDto.getSearchKbn(), "0")) {
			response = shohinService.searchItemList(searchItemDto);
			shohinList = shohinService.getSearchItemList(response.getData());
		} else if (StringUtils.equals(searchItemDto.getSearchKbn(), "1")) {
			shohinList = nailService.getShohinList();
		}

		int count = shohinList.size();
		
		List<ShohinEntity> shohinCdList = nailService.getShohinList();
		
		model.addAttribute("shohinCdList", shohinCdList);
		model.addAttribute("message", message);
		model.addAttribute("count", "検索結果 : " + count + "件");
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("shohinList", shohinList);
		
		return "ItemIndex";
	}
	
	@RequestMapping(value = "/NewItem", method = {RequestMethod.GET})
	public String newItem(Model model) {
		
		List<ShiireEntity> shiireList = shohinService.getColorAndPartsShiireList();
		
//		Map<String, List<ShiireEntity>> groupedByDaiBunrui = shiireList.stream()
//		        .collect(Collectors.groupingBy(ShiireEntity::getDaiBunruiName));
//		
//		List<ShiireEntity> partsList = groupedByDaiBunrui.get(DaiBunruiEnum.PARTS.getKey());
//		List<ShiireEntity> colorList = groupedByDaiBunrui.get(DaiBunruiEnum.COLOR.getKey());
//		
//		model.addAttribute("partsList", partsList);
//		model.addAttribute("colorList", colorList);
		model.addAttribute("shiireList", shiireList);
		
		
		model.addAttribute("themTypeList", ThemeTypeEnum.values());
		model.addAttribute("seasonList", SeasonEnum.values());
		model.addAttribute("shohin", new ShohinDto());	
		
		return "NewItem";
	}
	
	@RequestMapping(value = "/SaveItem", method = {RequestMethod.POST})
	public String saveItem(@ModelAttribute ShohinDto shohinDto, Model model){

		String result = shohinService.registryNewItem(shohinDto);
		
		model.addAttribute("headMessage", "正常に登録できました。");
		model.addAttribute("itemMessage", "商品コード : " + result);
		model.addAttribute("url", "/NewItem");
		
		return "Kanryo";
	}
	
	@RequestMapping(value = "ItemIndex/{shohinCd}", method = {RequestMethod.GET})
	public String editItem(@PathVariable String shohinCd, Model model){

		ShohinEntity entity = shohinService.getShohinInfo(shohinCd);
		
		model.addAttribute("entity", entity);

		return "EditItem";
	}
	
	@RequestMapping(value = "/Delete", method = {RequestMethod.POST})
	public String itemDelete(@RequestParam String shohinCd, RedirectAttributes redirectAttributes){
		String message = null;
		int result = shohinService.deleteItem(shohinCd);
		
//		if(result == 1) {
//			message = "商品コード「" + shohinCd + "」を削除しました";
//		} else {
//			message = "商品コード「" + shohinCd + "」はすでに削除済みです";
//		}
		message = "商品コード「" + shohinCd + "」を削除しました";
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/ItemIndex";

	}
	
	@RequestMapping(value = "/Update", method = {RequestMethod.POST})
	public String updateItem(ShohinEntity shohinEntity, RedirectAttributes redirectAttributes) {
		String message = null;
		String shohinCd = shohinEntity.getShohinCd();
		
		if(shohinEntity.getZeinukiGaku() == 0) {
			message = "税抜額を0円では登録できません";
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/ItemIndex/" + shohinCd;
		} 
		int result = shohinService.updateItem(shohinEntity);
		if(result == 1) {
			message = "商品コード「" + shohinCd +"」を更新しました";
		}
		
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/ItemIndex";
	}
	
	@RequestMapping(value = "/NewBunrui", method = {RequestMethod.GET})
	public String newBunrui(Model model) {
		
//		nailService.allBunruiList();
		model.addAttribute("daiBunruiList", DaiBunruiEnum.values());
		
		model.addAttribute("bunrui", new BunruiDto());
		
		return "NewBunrui";
	}
	
	@RequestMapping(value = "/SaveBunrui", method = {RequestMethod.POST})
	public String saveBunrui(BunruiDto bunruiDto) {
		
		nailService.countBunrui(bunruiDto);
		
		return "redirect:/NewBunrui";
	}
	
	@RequestMapping(value = "/NewShiire", method = {RequestMethod.GET})
	public String newShiire(Model model) {
		
		List<BunruiNameDto> daiBunruiList = nailService.getDaiBunrui();
		
		String minDateByCalender = nailService.minDate();
		
		model.addAttribute("minDate", minDateByCalender);
		model.addAttribute("shiireDto", new ShiireDto());
		model.addAttribute("daiBunruiList", daiBunruiList);
		model.addAttribute("shoBunruiList", null);
		
		return "NewShiire";
	}
	
	
	@RequestMapping(value = "/SaveShiire", method = {RequestMethod.POST})
	public String saveShiire(@ModelAttribute ShiireDto shiireDto, Model model) {
		
		ResponseData<ShiireEntity> result = shohinService.saveShiireItem(shiireDto);
		
		if(result.isHasShiire()) {
			// 送信されてきた仕入情報
			model.addAttribute("postShiireDto", shiireDto);
			// 送信されてきた品番が、すでに登録されており、その仕入情報
			model.addAttribute("shiireEntity", result.getData());
			
			model.addAttribute("shiireDto", new ShiireDto());
			return "Kakunin";
		}
		
		model.addAttribute("headMessage", result.getMessage());
		model.addAttribute("itemMessage", "品名 : " + result.getData().getItemName());
		model.addAttribute("url", "/NewShiire");
		
		return "Kanryo";
	}
	
	@RequestMapping(value = "/IndexShiire", method = {RequestMethod.GET})
	public String indexShiire(Model model) {
		
		List<BunruiNameDto> daiBunruiList = nailService.getDaiBunrui();
		
		model.addAttribute("searchShiire", new SearchShiireDto());
		model.addAttribute("daiBunruiList", daiBunruiList);
		
		return "IndexShiire";
	}

	@RequestMapping(value = "/IndexShiire", method = {RequestMethod.POST})
	public String searchShiire(SearchShiireDto searchShiireDto, RedirectAttributes redirectAttributes, Model model) {
		String message = null;
		boolean isAfterResult = shohinService.dayService(searchShiireDto.getStartDate(), searchShiireDto.getEndDate());
		if(isAfterResult) {
			message = "日付の範囲が不正です。";
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/IndexShiire";
		}
		List<BunruiNameDto> daiBunruiList = nailService.getDaiBunrui();
		List<ShiireEntity> shiireList = shohinService.searchShiireList(searchShiireDto);
		
		model.addAttribute("count", "検索結果 : " + shiireList.size() + "件");
		model.addAttribute("shiireList", shiireList);
		model.addAttribute("searchShiire", new SearchShiireDto());
		model.addAttribute("daiBunruiList", daiBunruiList);
		redirectAttributes.addFlashAttribute("message", message);
		return "IndexShiire";
	}
	
}
