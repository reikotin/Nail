package com.reiko.nail.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.reiko.nail.dto.EditDenpyoDto;
import com.reiko.nail.dto.EditShiireDto;
import com.reiko.nail.dto.ExportDenpyo;
import com.reiko.nail.dto.SearchItemDto;
import com.reiko.nail.dto.SearchShiireDto;
import com.reiko.nail.dto.ShiireDto;
import com.reiko.nail.dto.ShiireRirekiDto;
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
import com.reiko.nail.service.MessageService;
import com.reiko.nail.service.NailService;
import com.reiko.nail.service.ShohinService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NailController {
	
	private final static Logger logger = LogManager.getLogger(NailController.class);
	private final NailService nailService;
	private final ShohinService shohinService;
	private final CustomerService customerService;
	private final MessageService messageService;

	@RequestMapping(value = "/", method = {RequestMethod.GET})
	public String show(Model model) {
		logger.info(messageService.getMessage("proccess.Start", new String[] {"伝票一覧取得"}));	
		
		List<DenpyoDto> denpyoList = nailService.selectAllDenpyoList();
		logger.info(messageService.getMessage("proccess.End", new String[] {"伝票一覧取得"}));	
		model.addAttribute("denpyoList", denpyoList);
	
		return "Home";
	}
	
	@RequestMapping(value = "/Kanryo", method = {RequestMethod.GET})
	public String kanryo(HttpSession session, Model model) {

		String headMessage = (String) session.getAttribute("headMessage");
		String itemMessage = (String) session.getAttribute("itemMessage1");
		String itemMessage2 = (String) session.getAttribute("itemMessage2");
		String next = (String) session.getAttribute("next");
		String url = (String) session.getAttribute("url");
		model.addAttribute("headMessage", headMessage);
		model.addAttribute("itemMessage", itemMessage);
		model.addAttribute("itemMessage2", itemMessage2);
		model.addAttribute("next", next);
		model.addAttribute("url", url);
		return "Kanryo";
	}
	
	@RequestMapping(value = "/Denpyo/{denpyoNo}", method = {RequestMethod.GET})
	public String editDenpyo(@PathVariable String denpyoNo, Model model) {
		logger.info(messageService.getMessage("proccess.Start", new String[] {"伝票番号:" + denpyoNo + "の情報取得"}));
		
		List<ShohinEntity> shohinList = nailService.getShohinList();
		ResponseData<EditDenpyoDto> data = nailService.getEditDenpyoJoho(denpyoNo);
		logger.info(messageService.getMessage("proccess.End", new String[] {"伝票番号:" + denpyoNo + "の情報取得"}));
		
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
		String itemMessage2 = "";
		int result = 0;
		if(StringUtils.equals(status, DenpyoHakkoFlagEnum.DELETE.getKey())) {
			logger.info(messageService.getMessage("proccess.Start", new String[] {"伝票削除"}));
			String deleteMessage = nailService.deleteDenpyo(denpyoNo);
			itemMessage2 = deleteMessage;
			headMessage = messageService.getMessage("delete.success", null);
			logger.info(messageService.getMessage("proccess.End", new String[] {"伝票削除"}));
		} else {
			result = nailService.updateDenpyo(denpyoDto);
			if(result == 0) {
				headMessage = messageService.getMessage("update.success", null);
			}else if(result == 1) {
				itemMessage += "\n伝票・明細の更新に失敗しました。\n直前の処理を確認してください";
			}
		}

		model.addAttribute("headMessage", headMessage);
		model.addAttribute("itemMessage", itemMessage);
		model.addAttribute("itemMessage2", itemMessage2);
		
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
			customerService.updateCustomerJohoAll(denpyoDto);
		} else {
			customerService.updateRuikeiKounyuKingaku(denpyoDto);
		}
		logger.info(messageService.getMessage("proccess.Start", new String[] {"伝票・明細登録"}));
		nailService.saveDenpyo(denpyoDto);
		logger.info(messageService.getMessage("proccess.End", new String[] {"伝票・明細登録"}));
		String message = messageService.getMessage("insert.success", null);
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
		
		model.addAttribute("headMessage", messageService.getMessage("insert.success", null));
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
			resultMessage = messageService.getMessage("update.success", null);
		} else if(StringUtils.equals(flag, UpdateFlagEnum.DELETE.getKey())) {
			shohinService.deleteItem(shohinDto);
			resultMessage = messageService.getMessage("delete.success", null);
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
		model.addAttribute("test", false);
		
		
		return "IndexShiire";
	}
	
	@RequestMapping(value = "/IndexShiire", method = {RequestMethod.POST})
	public String searchShiire(SearchShiireDto searchShiireDto, RedirectAttributes redirectAttributes, 
			Model model) {
		
		int currentNum = searchShiireDto.getNumber();
		String message = null;
		boolean isAfterResult = shohinService.dayService(searchShiireDto.getStartDate(), searchShiireDto.getEndDate());
		if(isAfterResult) {
			message = "日付の範囲が不正です。";
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/IndexShiire";
		}
		List<BunruiNameDto> daiBunruiList = nailService.getDaiBunrui();
		List<ShiireRirekiDto> shiireList = shohinService.searchShiireList(searchShiireDto);
		int totalPage = shohinService.countTotalPage(searchShiireDto);
		model.addAttribute("currentNum", currentNum);
		model.addAttribute("test", true);
		model.addAttribute("totalPages", shohinService.getPageSize(totalPage));
		model.addAttribute("count", "検索結果 : " + totalPage + "件");
		model.addAttribute("shiireList", shiireList);
		model.addAttribute("searchShiire", new SearchShiireDto());
		model.addAttribute("daiBunruiList", daiBunruiList);
		model.addAttribute("kensakuWords", searchShiireDto);
		redirectAttributes.addFlashAttribute("message", message);
		return "IndexShiire";
	}
	
	@RequestMapping(value = "/EditShiire/{rirekiId}", method = {RequestMethod.GET})
	public String editShiire(@PathVariable String rirekiId, Model model) {
		EditShiireDto res = new EditShiireDto();
		res = nailService.findByShiire(rirekiId);
		String daiBunrui = res.getDaiBunruiName();
		List<BunruiNameDto> daiBunruiList = nailService.getDaiBunrui();
		List<BunruiNameDto> shoBunruiList = shohinService.getShoBunruiList(daiBunrui, "3");
		
		
		model.addAttribute("shiireItem", res);
		model.addAttribute("minDate", nailService.minDate());
		model.addAttribute("editShiireDto", new EditShiireDto());
		model.addAttribute("daiBunruiList", daiBunruiList);
		model.addAttribute("shoBunruiList", shoBunruiList);
		
		return "EditShiire";
	}
	
	@RequestMapping(value = "/UpdateShiire", method = {RequestMethod.POST})
	public String updateShiire(@ModelAttribute EditShiireDto editShiireDto, Model model) {
		logger.info(messageService.getMessage("proccess.Start", new String[] {"仕入情報更新"}));
		ResponseData<String> res = new ResponseData<>();
		try {
			res = nailService.updateShiireJoho(editShiireDto);
		} catch (Exception e) {
			res.setHasError(true);
			res.setMessage(messageService.getMessage("update.Warn", null));
			logger.info(e.getMessage());
		}
		
		model.addAttribute("headMessage", res.getMessage());
		model.addAttribute("itemMessage", "更新仕入ID : " + editShiireDto.getShiireId());
		model.addAttribute("itemMessage2", "更新履歴ID : " + editShiireDto.getRirekiId());
		
		logger.info(messageService.getMessage("proccess.End", new String[] {"仕入情報更新"}));
		return "Kanryo";
	}
	
	@RequestMapping(value = "/DeleteShiire", method = {RequestMethod.POST})
	public ResponseEntity<ResponseData<List<String>>> deleteShiire(@RequestParam String rirekiId, HttpSession session) {
		logger.info(messageService.getMessage("proccess.Start", new String[] {"仕入履歴削除"}));
		
		ResponseData<List<String>> res = nailService.deleteShiireJoho(rirekiId);
		
		String itemMessage = res.getData().get(0);
		session.setAttribute("headMessage", res.getMessage());
		session.setAttribute("itemMessage1", itemMessage);
		session.setAttribute("itemMessage2", "");
		session.setAttribute("next", "");
		session.setAttribute("url", "/");
		
		
		
		logger.info(messageService.getMessage("proccess.End", new String[] {"仕入履歴削除"}));
		return new ResponseEntity<ResponseData<List<String>>>(res, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/Export/{denpyoNo}", method = {RequestMethod.GET})
	public String exportDenpyo(@PathVariable String denpyoNo, Model model) {
		logger.info(messageService.getMessage("proccess.Start", new String[] {"領収書兼納品書取得"}));
		List<ExportDenpyo> exportList = nailService.denpyoHakko(denpyoNo);
		logger.info(messageService.getMessage("proccess.End", new String[] {"領収書兼納品書取得"}));
        Integer shokeiSum = exportList.stream()
                .mapToInt(denpyo -> denpyo.getShokei() != null ? denpyo.getShokei() : 0)
                .sum();
		
		String ryoshushodenpyoNo = exportList.get(0).getDenpyoNo();
		LocalDate kounyuDate = exportList.get(0).getKounyuDate();
		LocalDate hassoDate = exportList.get(0).getHassoDate();
		String customerSei = exportList.get(0).getCustomerSei();
		String customerMei = exportList.get(0).getCustomerMei();
		
		
		model.addAttribute("denpyoNo", ryoshushodenpyoNo);
		model.addAttribute("kounyuDate", kounyuDate);
		model.addAttribute("hassoDate", hassoDate);
		model.addAttribute("customerSei", customerSei + "　");
		model.addAttribute("customerMei", customerMei + "　");
		model.addAttribute("goukei", shokeiSum);
		model.addAttribute("exportList", exportList);
		return "Export";
	}
	
	
}
