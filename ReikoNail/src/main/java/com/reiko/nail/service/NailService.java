package com.reiko.nail.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reiko.nail.dao.BunruiDao;
import com.reiko.nail.dao.DenpyoDao;
import com.reiko.nail.dao.SalesDao;
import com.reiko.nail.dao.ShohinDao;
import com.reiko.nail.dto.BunruiDto;
import com.reiko.nail.dto.BunruiNameDto;
import com.reiko.nail.dto.DenpyoDto;
import com.reiko.nail.dto.EditDenpyoDto;
import com.reiko.nail.dto.ExportDenpyo;
import com.reiko.nail.dto.ShohinDto;
import com.reiko.nail.entity.BunruiEntity;
import com.reiko.nail.entity.DenpyoEntity;
import com.reiko.nail.entity.SalesEntity;
import com.reiko.nail.entity.ShohinEntity;
import com.reiko.nail.enums.DaiBunruiEnum;
import com.reiko.nail.enums.DenpyoHakkoFlagEnum;
import com.reiko.nail.response.ResponseData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NailService {
	
	private final DenpyoDao denpyoDao;
	
	private final ShohinDao shohinDao;
	
	private final SalesDao salesDao;
	
	private final BunruiDao bunruiDao;

	// 伝票情報の取得
	public List<DenpyoEntity> selectAllDenpyoList() {
		
		List<DenpyoEntity> list = denpyoDao.selectByAllDenpyoList();
		list.forEach(denpyo -> {
			if(DenpyoHakkoFlagEnum.getByKey(denpyo.getDenpyoHakkozumiFlag()) == DenpyoHakkoFlagEnum.HASSOZUMI) {
		        denpyo.setDenpyoHakkozumiFlag(DenpyoHakkoFlagEnum.HASSOZUMI.getValue());
		    } else {
		        denpyo.setDenpyoHakkozumiFlag(DenpyoHakkoFlagEnum.UKETSUKETYU.getValue());
		    }
		});
		return list;
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
	
	// 伝票の論理削除
	public String deleteDenpyo(String denpyoNo) {
		int denpyoDeleteCount = denpyoDao.deleteDenpyoByPrimary(denpyoNo);
		int meisaiDeleteCount = salesDao.deleteMeisaiByPrimary(denpyoNo);
		
		String denpyo = "伝票削除数:" + denpyoDeleteCount + "件<br>";
		String meisai = "明細削除数:" + meisaiDeleteCount + "件";
		return denpyo + meisai;	
	}
	
	// 伝票情報・明細情報の更新
	@Transactional
	public int updateDenpyo(EditDenpyoDto denpyoDto) {
		System.out.println("伝票情報の更新処理を開始します。");
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
        		System.out.println("金額変更なしで伝票を更新しました");
        		System.out.println("伝票番号: " + denpyoDto.getDenpyoNo());
        	}
        	System.out.println("伝票情報の更新処理を終了します。");
        	
        	
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
	        		System.out.println("金額変更ありで伝票を更新しました");
	        		System.out.println("伝票番号: " + denpyoDto.getDenpyoNo());
	        	}
	           	System.out.println("伝票情報の更新処理を終了します。");
	           	System.out.println("明細情報の更新処理を開始します。");
	           	int deleteCount = salesDao.deleteMeisaiByPrimary(denpyoDto.getDenpyoNo());
	           	if(result != 0) {
	        		System.out.println("伝票番号: " + denpyoDto.getDenpyoNo() + "に紐づく、明細情報を削除しました。");
	        		System.out.println("削除件数: " + deleteCount + "件");
	        	}
	           	System.out.println("新しい明細情報の登録処理を開始します。");
	           	int updateMeisaiCount = updateMeisai(denpyoDto);
	           	System.out.println("登録件数: " + updateMeisaiCount + "件");
	           	System.out.println("新しい明細情報の登録処理を終了します。");
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
	public List<ExportDenpyo> denpyoHakko(){
		return denpyoDao.exportDenpyo("2402040003");
	}
	
	// 編集用の伝票情報の取得
	public ResponseData<EditDenpyoDto> getEditDenpyoJoho(String denpyoNo) {
		ResponseData<EditDenpyoDto> resulData = new ResponseData<EditDenpyoDto>();
		EditDenpyoDto denpyoJoho = new EditDenpyoDto();		
		List<ShohinEntity> shohinJoho = new ArrayList<>();
		denpyoJoho = denpyoDao.selectByEditDenpyoForCustomerJoho(denpyoNo);
		
		if(DenpyoHakkoFlagEnum.getByKey(denpyoJoho.getDenpyoHakkozumiFlag()) == DenpyoHakkoFlagEnum.HASSOZUMI) {
			// 発送済みメッセージ
			resulData.setMessage(hassouzumiMessage(denpyoJoho.getHassoDate()));
			// 発送済みフラグ
			resulData.setHasError(true); 
			denpyoJoho.setDenpyoHakkozumiFlag(DenpyoHakkoFlagEnum.HASSOZUMI.getValue());
		} else {
			denpyoJoho.setDenpyoHakkozumiFlag(DenpyoHakkoFlagEnum.UKETSUKETYU.getValue());
		}
		// 商品情報のセット
		shohinJoho = denpyoDao.selectByEditDenpyoForShohinJoho(denpyoNo);
		denpyoJoho.setShohinJoho(shohinJoho);
		
		resulData.setData(denpyoJoho);
		
		return resulData;
	}
	
    private static String hassouzumiMessage(LocalDate date) {
        String formattedDate = "発送日 : " +  date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        return formattedDate;
    }
}