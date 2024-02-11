package com.reiko.nail.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reiko.nail.dto.BunruiDto;
import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.DenpyoDto;
import com.reiko.nail.dto.EditDenpyoDto;
import com.reiko.nail.dto.ExportDenpyo;
import com.reiko.nail.dto.SearchItemDto;
import com.reiko.nail.dto.SearchShiireDto;
import com.reiko.nail.dto.ShiireDto;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.dto.UpdateShohinDto;
import com.reiko.nail.entity.CustomerEntity;
import com.reiko.nail.entity.ShiireEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.enums.DaiBunruiEnum;
import com.reiko.nail.enums.DenpyoHakkoFlagEnum;
import com.reiko.nail.enums.HassoHohoEnum;
import com.reiko.nail.enums.SeasonEnum;
import com.reiko.nail.enums.ThemeTypeEnum;
import com.reiko.nail.enums.UpdateFlagEnum;
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
		
		List<DenpyoDto> denpyoList = nailService.selectAllDenpyoList();
		
		model.addAttribute("denpyoList", denpyoList);
	
		return "Home";
	}
	
	@RequestMapping(value = "/Denpyo/{denpyoNo}", method = {RequestMethod.GET})
	public String editDenpyo(@PathVariable String denpyoNo, Model model) {
		List<ShohinEntity> shohinList = nailService.getShohinList();
		ResponseData<EditDenpyoDto> data = nailService.getEditDenpyoJoho(denpyoNo);
		
		model.addAttribute("hassoHohoList", HassoHohoEnum.values());
		model.addAttribute("shohinList", shohinList);
		model.addAttribute("denpyoDto", new EditDenpyoDto());
		model.addAttribute("hassoDate", data.getMessage());
		model.addAttribute("isHassouZumi", data.isHasError());
		model.addAttribute("editDenpyo", data.getData());
		model.addAttribute("meisaiList", data.getData().getShohinJoho());
		
		return "EditDenpyo";
	}
	
	@RequestMapping(value = "/UpdateDenpyo", method = {RequestMethod.POST})
	public String updateDenpyo(@ModelAttribute EditDenpyoDto denpyoDto, Model model) {
		String denpyoNo = denpyoDto.getDenpyoNo();
		String status = denpyoDto.getStatus();

		String headMessage = "";
		String itemMessage = "伝票番号:" + denpyoNo;
		// 伝票更新
		if(StringUtils.equals(status, DenpyoHakkoFlagEnum.DELETE.getKey())) {
			String deleteMessage = nailService.deleteDenpyo(denpyoNo);
			itemMessage = itemMessage + "\n" + deleteMessage;
			headMessage = "削除完了しました";
		}
		
		int result = nailService.updateDenpyo(denpyoDto);
		if(result == 0) {
			headMessage = "伝票を更新しました。";
		}else if(result == 1) {
			itemMessage += "\n伝票・明細の更新に失敗しました。\n直前の処理を確認してください";
		}
		
		model.addAttribute("headMessage", headMessage);
		model.addAttribute("itemMessage", itemMessage);
		
		return "Kanryo";
	}

	@RequestMapping(value = "/NewDenpyo", method = {RequestMethod.GET})
	public String newDenpyo(Model model) {
		
		List<ShohinEntity> shohinList = nailService.getShohinList();
		List<CustomerEntity> customerList = customerService.getAllCustomer();
		
		EditDenpyoDto denpyoDto = new EditDenpyoDto();
		model.addAttribute("hassoHohoList", HassoHohoEnum.values());
		model.addAttribute("customerList", customerList);
		model.addAttribute("shohinList", shohinList);
		model.addAttribute("denpyoDto", denpyoDto);
		
		return "NewDenpyo";
	}
	
	@RequestMapping(value = "/Save", method = {RequestMethod.POST})
	public String saveDenpyo(@ModelAttribute EditDenpyoDto denpyoDto, Model model) {
		
		if(denpyoDto.isCustomerJohoHenshu()) {
			customerService.updateCustomerJoho(denpyoDto);
		} else {
			customerService.updateRuikeiKounyuKingaku(denpyoDto);
		}
		//System.out.println(denpyoDto);
		nailService.saveDenpyo(denpyoDto);
		
		String message = "伝票登録完了しました。";
		model.addAttribute("next", "続けて登録する");
		model.addAttribute("url", "/NewDenpyo");
		model.addAttribute("headMessage", message);
		return "kanryo";
	}
	
	@RequestMapping(value = "/ItemIndex", method = {RequestMethod.GET})
	public String itemIndex(Model model){

		List<String> themeList = new ArrayList<>();
		for(ThemeTypeEnum theme	 : ThemeTypeEnum.values()) {
			themeList.add(theme.getKey());
		}
		themeList.add(0, "");

		model.addAttribute("themeList", themeList);
		model.addAttribute("searchItem", new SearchItemDto());

		return "ItemIndex";
	}
	
//	@RequestMapping(value = "/ItemIndex", method = {RequestMethod.POST})
//	public String searchItem(SearchItemDto searchItemDto, Model model) {
//		
//		String message = null;
//		
//		List<ShohinEntity> shohinList = new ArrayList<>();
//		SearchItemDto searchItem = new SearchItemDto();
//		ResponseData<SearchItemDto> response = new ResponseData<SearchItemDto>();
//		
//		if(StringUtils.equals(searchItemDto.getSearchKbn(), "0")) {
//			response = shohinService.searchItemList(searchItemDto);
//			shohinList = shohinService.getSearchItemList(response.getData());
//		} else if (StringUtils.equals(searchItemDto.getSearchKbn(), "1")) {
//			shohinList = nailService.getShohinList();
//		}
//
//		int count = shohinList.size();
//		
//		List<ShohinEntity> shohinCdList = nailService.getShohinList();
//		
//		model.addAttribute("shohinCdList", shohinCdList);
//		model.addAttribute("message", message);
//		model.addAttribute("count", "検索結果 : " + count + "件");
//		model.addAttribute("searchItem", searchItem);
//		model.addAttribute("shohinList", shohinList);
//		
//		return "ItemIndex";
//	}
	
	@RequestMapping(value = "/NewItem", method = {RequestMethod.GET})
	public String newItem(Model model) {
		
		List<ShiireEntity> shiireList = shohinService.getColorAndPartsShiireList();
		
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
		model.addAttribute("next", "続けて登録する");		
		return "Kanryo";
	}
	
	@RequestMapping(value = "/EditItem/{shohinCd}", method = {RequestMethod.GET})
	public String editItem(@PathVariable String shohinCd, Model model){

		ShohinEntity shohinEntity = shohinService.getShohinInfo(shohinCd);
		
		List<ShiireEntity> shiireList = new ArrayList<>();
		if(StringUtils.isNotEmpty(shohinEntity.getShiireIdList())) {
			shiireList = shohinService.getShiireListById(shohinEntity.getShiireIdList());
		}
		
		model.addAttribute("flags", UpdateFlagEnum.values());
		model.addAttribute("shohin", new UpdateShohinDto());
		model.addAttribute("shohinEntity", shohinEntity);
		model.addAttribute("shiireList", shiireList);
		
		return "EditItem";
	}
	
	@RequestMapping(value = "/UpdateItem", method = {RequestMethod.POST})
	public String updateItem(UpdateShohinDto shohinDto, Model model) {
		
		String flag = shohinDto.getUpdateFlag();
		String resultMessage = null;
		if(StringUtils.equals(flag ,UpdateFlagEnum.UPDATE.getKey())) {
			shohinService.updateItem(shohinDto);
			resultMessage = "正常に更新できました。";
		} else if(StringUtils.equals(flag, UpdateFlagEnum.DELETE.getKey())) {
			shohinService.deleteItem(shohinDto);
			resultMessage = "下記の商品を削除しました。";
		}
		
		model.addAttribute("headMessage", resultMessage);
		model.addAttribute("itemMessage", "商品コード : " + shohinDto.getShohinCd());
		return "Kanryo";
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
		model.addAttribute("next", "続けて登録する");		
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
	
	@RequestMapping(value = "/EditShiire/{shiireId}", method = {RequestMethod.GET})
	public String editShiire(@PathVariable String shiireCd, Model model) {
		// TODO
		
		return "EditShiire";
	}
	
	@RequestMapping(value = "/Export", method = {RequestMethod.GET})
	public String exportDenpyo(Model model) {
		
		 
		DenpyoDto dto = new	DenpyoDto();
		
		List<ExportDenpyo> exportList = nailService.denpyoHakko();
		Integer goukei = 0;
		for(int i = 0; i < exportList.size(); i++) {
			goukei += exportList.get(i).getShokei();
		}
		
		int tableSizeTyousei = 0;
		if(exportList.size() < 5) {
			tableSizeTyousei = 5 - exportList.size();
		}
		
		for(int i = 0; i < tableSizeTyousei; i++) {
			ExportDenpyo exportDto = new ExportDenpyo();
			exportDto.setDenpyoNo("　");
			exportDto.setCustomerSei("　");
			exportDto.setCustomerMei("　");
			exportDto.setKounyuDate(null);
			exportDto.setShohinCd("　");
			exportDto.setZeikomiGaku(null);
			exportDto.setSuryo(null);
			exportDto.setShokei(null);
			exportList.add(exportDto);
		}
		
		for(int i = 0; i < exportList.size(); i++) {
			exportList.get(i).setNumber(i+1);
		}
		
		String denpyoNo = exportList.get(0).getDenpyoNo();
		LocalDate kounyuDate = exportList.get(0).getKounyuDate();
		String customerSei = exportList.get(0).getCustomerSei();
		String customerMei = exportList.get(0).getCustomerMei();
		
		model.addAttribute("denpyoNo", denpyoNo);
		model.addAttribute("kounyuDate", kounyuDate);		
		model.addAttribute("customerSei", customerSei + "　");
		model.addAttribute("customerMei", customerMei + "　");
		model.addAttribute("goukei", goukei);
		
		model.addAttribute("exportList", exportList);
		return "Export";
	}
	
	
}
