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
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.entity.DenpyoEntity;
import com.reiko.nail.entity.ShiireEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.enums.DaiBunruiEnum;
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
		
		DenpyoDto denpyoDto = new DenpyoDto();
		
		model.addAttribute("shohinList", shohinList);
		model.addAttribute("denpyoDto", denpyoDto);
		
		return "NewDenpyo";
	}
	
	@RequestMapping(value = "/Save", method = {RequestMethod.POST})
	public String saveDenpyo(@ModelAttribute DenpyoDto denpyoDto) {
		
		int result = customerService.findByCustomer(denpyoDto.getCustomerNameSei(), denpyoDto.getCustomerNameMei());
		
		if(result == 0) {
			customerService.insertNewCustomer(denpyoDto);
		}
		
		nailService.saveDenpyo(denpyoDto);
		 
		return "redirect:/";
	}
	
	@RequestMapping(value = "/ItemIndex", method = {RequestMethod.GET})
	public String itemIndex(Model model){

		List<ShohinEntity> shohinList = new ArrayList<>();
		List<ShohinEntity> shohinCdList = nailService.getShohinList();
//		List<ShohinEntity> shohinList = studyService.getShohinList();
		SearchItemDto searchItem = new SearchItemDto();
		model.addAttribute("shohinCdList", shohinCdList);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("shohinList", shohinList);

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
		
		model.addAttribute("shohin", new ShohinDto());	
		
		return "NewItem";
	}
	
	@RequestMapping(value = "/SaveItem", method = {RequestMethod.POST})
	public String saveItem(@ModelAttribute ShohinDto shohinDto, RedirectAttributes redirectAttributes){
		String message = null;
		
		if(shohinDto.getZeinukiGaku() == 0) {
			message = "税抜額は、0円で登録できません";
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/NewItem";
		}
		String newItemCd = shohinService.registryNewItem(shohinDto);
		if(StringUtils.isNotEmpty(newItemCd)) {
			message = "商品コード「" + newItemCd + "」で登録しました";
		} else {
			message = "登録中にエラーが発生しました";
		}
		
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/ItemIndex";
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
		
		if(result == 1) {
			message = "商品コード「" + shohinCd + "」を削除しました";
		} else {
			message = "商品コード「" + shohinCd + "」はすでに削除済みです";
		}
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
		
		model.addAttribute("shiireEntity", new ShiireEntity());
		model.addAttribute("daiBunruiList", daiBunruiList);
		model.addAttribute("shoBunruiList", null);
		
		return "NewShiire";
	}
	
	@RequestMapping(value = "/SaveShiire", method = {})
	public String saveShiire(ShiireEntity shiireEntity) {
		
		System.out.println(shiireEntity);
		return "redirect:/NewShiire";
	}
	
}
