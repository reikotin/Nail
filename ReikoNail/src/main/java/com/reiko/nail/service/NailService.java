
package com.reiko.nail.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reiko.nail.dao.BunruiDao;
import com.reiko.nail.dao.DenpyoDao;
import com.reiko.nail.dao.RirekiDao;
import com.reiko.nail.dao.SalesDao;
import com.reiko.nail.dao.ShiireDao;
import com.reiko.nail.dao.ShohinDao;
import com.reiko.nail.dto.BunruiDto;
import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.DenpyoDto;
import com.reiko.nail.dto.EditDenpyoDto;
import com.reiko.nail.dto.EditShiireDto;
import com.reiko.nail.dto.ExportDenpyo;
import com.reiko.nail.entity.BunruiEntity;
import com.reiko.nail.entity.DenpyoEntity;
import com.reiko.nail.entity.SalesEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.enums.DaiBunruiEnum;
import com.reiko.nail.enums.DenpyoHakkoFlagEnum;
import com.reiko.nail.enums.HassoHohoEnum;
import com.reiko.nail.response.ResponseData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NailService {
	
	private final static Logger logger = LogManager.getLogger(NailService.class);
	private final DenpyoDao denpyoDao;
	private final ShohinDao shohinDao;
	private final SalesDao salesDao;
	private final BunruiDao bunruiDao;
	private final ShiireDao shiireDao;
	private final RirekiDao rirekiDao;
	private final MessageService messageService;

	// 伝票情報の取得
	public List<DenpyoDto> selectAllDenpyoList() {
		
		List<DenpyoDto> list = denpyoDao.selectByAllDenpyoList();
		logger.info(messageService.getMessage("get.data.count", new String[] {"伝票情報", Integer.toString(list.size())}));
		
		list.forEach(denpyo -> {
			if(DenpyoHakkoFlagEnum.getByKey(denpyo.getDenpyoHakkozumiFlag()) == DenpyoHakkoFlagEnum.HASSOZUMI) {
		        denpyo.setDenpyoHakkozumiFlag(DenpyoHakkoFlagEnum.HASSOZUMI.getValue());
		    } else {
		        denpyo.setDenpyoHakkozumiFlag(DenpyoHakkoFlagEnum.UKETSUKETYU.getValue());
		    }
			if(HassoHohoEnum.getByKey(denpyo.getHassoHoho()) == HassoHohoEnum.QUICKPOST) {
				denpyo.setHassoHoho(HassoHohoEnum.QUICKPOST.getValue());
			}
		});
		
		return list;
	}
	
	public List<ShohinEntity> getShohinList(){
		List<ShohinEntity> shohinList = shohinDao.selectAllShohinList();
		
		return shohinList;
	}

	// 伝票登録
	public void saveDenpyo(EditDenpyoDto denpyoDto) {
		/** 購入日の伝票枚数を取得し、伝票番号に変換 */
		int denpyoMaisu = denpyoDao.countDenpyoMaisu(denpyoDto.getKounyuDate());
		String denpyoDate = denpyoDto.getKounyuDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
		String denpyoNo = denpyoDate + String.format("%04d", denpyoMaisu);
		
		saveMeisai(denpyoDto, denpyoNo);
		
		Integer zeinukiGaku = 0;
		Integer zeiGaku = 0;
		Integer zeikomiGaku = 0;

		for(ShohinEntity shohinDto : denpyoDto.getShohinJoho()) {
			zeinukiGaku += shohinDto.getZeinukiGaku();
			zeiGaku += shohinDto.getZeiGaku();
			zeikomiGaku += shohinDto.getZeikomiGaku();
		}
		
		denpyoDao.insertDenpyo(zeinukiGaku, zeiGaku, zeikomiGaku, denpyoDto.getCustomerCd(), denpyoNo, denpyoDto.getKounyuDate(), denpyoDto.getBiko());
	}
	
	// 販売明細登録
	private void saveMeisai(EditDenpyoDto denpyoDto, String denpyoNo) {
		
		List<SalesEntity> salesList = new ArrayList<>();
		for(ShohinEntity shohin : denpyoDto.getShohinJoho()) {
			SalesEntity entity = new SalesEntity();
			entity.setDenpyoNo(denpyoNo);
			entity.setShohinCd(shohin.getShohinCd());
			entity.setKounyuDate(denpyoDto.getKounyuDate());
			entity.setCustomerCd(denpyoDto.getCustomerCd());
			salesList.add(entity);
		}
		salesDao.registryMeisai(salesList);
	}
	
	// 伝票の論理削除
	public String deleteDenpyo(String denpyoNo) {
		logger.info(messageService.getMessage("delete.denpyo.info", new String[] {denpyoNo}));
		int denpyoDeleteCount = denpyoDao.deleteDenpyoByPrimary(denpyoNo);
		if(denpyoDeleteCount == 1) {
			logger.info(messageService.getMessage("delete.success", null));
		}
		logger.info(messageService.getMessage("delete.meisai.info", new String[] {denpyoNo}));
		int meisaiDeleteCount = salesDao.deleteMeisaiByPrimary(denpyoNo);
		if(meisaiDeleteCount >= 1) {
			logger.info(messageService.getMessage("delete.success", null));
			logger.info(messageService.getMessage("delete.data.count", new String[] {"明細", Integer.toString(meisaiDeleteCount)}));
		}
		
		String denpyo = "\n伝票削除数:" + denpyoDeleteCount + "件\n";
		String meisai = "明細削除数:" + meisaiDeleteCount + "件";
		return denpyo + meisai;	
	}
	
	// 伝票情報・明細情報の更新
	@Transactional
	public int updateDenpyo(EditDenpyoDto denpyoDto) {
		logger.info(messageService.getMessage("proccess.Start", new String[] {"伝票情報更新"}));
		List<ShohinEntity> shohinJoho = denpyoDao.selectByEditDenpyoForShohinJoho(denpyoDto.getDenpyoNo());
	
        List<String> beforeShohinCdList = shohinJoho.stream()
        		.map(ShohinEntity::getShohinCd)
        		.collect(Collectors.toList());
        List<String> afterShohinCdList = denpyoDto.getShohinJoho().stream()
        		.map(ShohinEntity::getShohinCd)
        		.collect(Collectors.toList());
        // 伝票で商品情報の変更があるかにないか
        boolean isShohinCdMatch = shohinCdMatch(beforeShohinCdList, afterShohinCdList);
        DenpyoEntity entity = new DenpyoEntity();
       	entity.setDenpyoNo(denpyoDto.getDenpyoNo());
    	entity.setHassoHoho(denpyoDto.getHassoHoho());
    	entity.setHassoDate(denpyoDto.getHassoDate());
    	entity.setHassoRyo(denpyoDto.getHassoRyo());
    	entity.setTsuisekiNo(denpyoDto.getTsuisekiNo());
    	entity.setBiko(denpyoDto.getBiko());
    	entity.setDenpyoHakkozumiFlag(denpyoDto.getStatus());
    	int returnInt = 0;
        // 変更なし
        if(isShohinCdMatch) {
        	int result = denpyoDao.updateDenpyoByNotKingakuHenko(entity);
        	if(result != 0) {
        		logger.info("金額変更なしで更新完了しました");
        		logger.info("更新伝票番号: " + denpyoDto.getDenpyoNo());
        	}
        	logger.info(messageService.getMessage("proccess.End", new String[] {"伝票情報更新"}));
        } else {
        	Integer zeinukiGaku = 0;
        	Integer zeiGaku = 0;
        	Integer zeikomiGaku = 0;
        	for(ShohinEntity after : denpyoDto.getShohinJoho()) {
        		zeinukiGaku += after.getZeinukiGaku();
        		zeiGaku += after.getZeiGaku();
        		zeikomiGaku += after.getZeikomiGaku();
        	}
        	entity.setZeinukiGaku(zeinukiGaku);
        	entity.setZeiGaku(zeiGaku);
        	entity.setZeikomiGaku(zeikomiGaku);
        	try {
	        	int result = denpyoDao.updateDenpyoByKingakuHenko(entity);
	           	if(result != 0) {
	           		logger.info("金額変更ありで更新完了しました。");
	           		logger.info("更新伝票番号: " + denpyoDto.getDenpyoNo());
	        	}
	           	logger.info(messageService.getMessage("proccess.End", new String[] {"伝票情報更新"}));
	           	logger.info(messageService.getMessage("proccess.Start", new String[] {"明細情報更新"}));
	           	int deleteCount = salesDao.deleteMeisaiByPrimary(denpyoDto.getDenpyoNo());
	           	if(result != 0) {
	           		logger.info("伝票番号: " + denpyoDto.getDenpyoNo() + "に紐づく、明細情報を削除しました。");
	        		logger.info(messageService.getMessage("delete.data.count", new String[] {"新しい明細", Integer.toString(deleteCount)}));
	        	}
	           	
	           	logger.info(messageService.getMessage("proccess.Start", new String[] {"新しい明細情報の登録"}));
	           	
	           	int updateMeisaiCount = updateMeisai(denpyoDto);
	           	logger.info(messageService.getMessage("insert.data.count", new String[] {"新しい明細", Integer.toString(updateMeisaiCount)}));
	           	logger.info(messageService.getMessage("proccess.End", new String[] {"新しい明細情報の登録"}));
        	} catch (Exception e) {
        		returnInt = 1;
				e.printStackTrace();
			}
           	
        }
        
        
        return returnInt;
	}
	
	private int updateMeisai(EditDenpyoDto denpyoDto) {
		List<SalesEntity> salesList = new ArrayList<>();
		for(ShohinEntity entity : denpyoDto.getShohinJoho()) {
			SalesEntity sale = new SalesEntity();
			sale.setDenpyoNo(denpyoDto.getDenpyoNo());
			sale.setCustomerCd(denpyoDto.getCustomerCd());
			sale.setShohinCd(entity.getShohinCd());
			sale.setKounyuDate(denpyoDto.getKounyuDate());
			salesList.add(sale);
		}
		return salesDao.registryMeisai(salesList);
	}
	
	/**
	 * 伝票で商品情報の変更があるかにないか
	 * @param beforeList
	 * @param afterList
	 * @return ない場合true、ある場合false
	 */
	private boolean shohinCdMatch(List<String> beforeList, List<String> afterList) {
		return beforeList.containsAll(afterList) && afterList.containsAll(beforeList);
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
	
	// 分類コードの作成
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
	
	public String minDate() {
		// 本日の日付
	    LocalDate today = LocalDate.now();
	    // 一週間前の日付を計算
	    LocalDate oneWeekAgo = today.minusWeeks(2);
	    // フォーマットしてHTMLの形式に変換
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String minDate = oneWeekAgo.format(formatter);
	    return minDate;   		
	}
	
	// 領収書兼納品書の取得
	public List<ExportDenpyo> denpyoHakko(String denpyoNo){
		List<ExportDenpyo> exportList = denpyoDao.exportDenpyo(denpyoNo);
		if(exportList.size() != 0) {
			logger.info(messageService.getMessage("get.denpyo.info", new String[] {denpyoNo}));
		}
		// 表示の都合上、強制的にリストサイズを5にする。
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
		
		return exportList;
	}
	
	// 編集用の伝票情報の取得
	public ResponseData<EditDenpyoDto> getEditDenpyoJoho(String denpyoNo) {
		ResponseData<EditDenpyoDto> resulData = new ResponseData<EditDenpyoDto>();
		EditDenpyoDto denpyoJoho = new EditDenpyoDto();		
		List<ShohinEntity> shohinJoho = new ArrayList<>();
		denpyoJoho = denpyoDao.selectByEditDenpyoForCustomerJoho(denpyoNo);
		
		if(DenpyoHakkoFlagEnum.getByKey(denpyoJoho.getDenpyoHakkozumiFlag()) == DenpyoHakkoFlagEnum.HASSOZUMI) {
			// 発送済みフラグ
			resulData.setHasError(true); 
			denpyoJoho.setDenpyoHakkozumiFlag(DenpyoHakkoFlagEnum.HASSOZUMI.getValue());
		} else {
			denpyoJoho.setDenpyoHakkozumiFlag(DenpyoHakkoFlagEnum.UKETSUKETYU.getValue());
		}
		
		if(HassoHohoEnum.getByKey(denpyoJoho.getHassoHoho()) == HassoHohoEnum.QUICKPOST) {
			denpyoJoho.setHassoHoho(HassoHohoEnum.QUICKPOST.getKey());
		}
		// 商品情報のセット
		shohinJoho = denpyoDao.selectByEditDenpyoForShohinJoho(denpyoNo);
		denpyoJoho.setShohinJoho(shohinJoho);
		
		resulData.setData(denpyoJoho);
		
		return resulData;
	}
	
	public EditShiireDto findByShiire(String shiireId) {
		return shiireDao.selectByShiire(shiireId);
	}
	
	/**
	 * 仕入情報更新
	 * @param editShiireDto
	 * @return {@code ResponseData<String>}仕入情報の更新結果メッセージ
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseData<String> updateShiireJoho(EditShiireDto editShiireDto) throws Exception {
		logger.info(messageService.getMessage("proccess.Start", new String[] {"仕入商品情報更新"}));
		int result = shiireDao.updateShiire(
				editShiireDto.getDaiBunruiName(), 
				editShiireDto.getShoBunruiName(), 
				editShiireDto.getItemName(), 
				editShiireDto.getZeinukiGaku(), 
				editShiireDto.getZeiGaku(),
				editShiireDto.getZeikomiGaku(), 
				editShiireDto.getItemSize(), 
				editShiireDto.getJanCd(), 
				editShiireDto.getShiireId(),
				editShiireDto.getShiireSaki(), 
				editShiireDto.getShiireMaker());
		ResponseData<String> response = new ResponseData<>();
		if (result != 1) {
			throw new Exception(messageService.getMessage("update.shiire.Warn", new String[] {"仕入ID", editShiireDto.getShiireId()}));
		}
		logger.info(messageService.getMessage("update.shiire.Info", new String[] {"仕入ID", editShiireDto.getShiireId()}));
		logger.info(messageService.getMessage("proccess.End", new String[] {"仕入商品情報更新"}));
		logger.info(messageService.getMessage("proccess.Start", new String[] {"仕入履歴更新"}));
		int rirekiResult = rirekiDao.updateRireki(editShiireDto.getRirekiId(), editShiireDto.getShiireDate(), editShiireDto.getSuryo());
		if (rirekiResult != 1) {
			throw new Exception(messageService.getMessage("update.shiire.Warn", new String[] {"履歴ID", editShiireDto.getRirekiId()}));
		}
		
		logger.info(messageService.getMessage("update.shiire.Info", new String[] {"履歴ID", editShiireDto.getRirekiId()}));
		logger.info(messageService.getMessage("proccess.End", new String[] {"仕入履歴更新"}));
		response.setMessage(messageService.getMessage("update.success", null));
		
		return response;
	}
	
	public ResponseData<List<String>> deleteShiireJoho(String rirekiId) {
		ResponseData<List<String>> res = new ResponseData<>();
		
		int deleteCount = rirekiDao.deleteRireki(rirekiId);
		if (deleteCount != 1) {
			res.setHasError(true);
			res.setData(Arrays.asList("履歴ID : " + rirekiId + "の削除に失敗しました。"));
			res.setMessage(messageService.getMessage("delete.Warn", new String[] {"仕入履歴"}));
			return res;
		}
		res.setData(Arrays.asList("削除履歴ID : " + rirekiId));
		res.setMessage(messageService.getMessage("delete.success", null));
		return res;
	}

}