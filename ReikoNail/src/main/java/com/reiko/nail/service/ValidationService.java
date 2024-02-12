package com.reiko.nail.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidationService {

	private final Validator validator;
	
	public List<String> validateCustomer(Object obj){
		System.out.println("入力チェックを開始します。");
		List<String> messageList = new ArrayList<>();
		
		Set<ConstraintViolation<Object>> violations = validator.validate(obj);
		
		for (ConstraintViolation<Object> violation : violations) {
			messageList.add(violation.getMessage());
	    }
		messageList.sort(Comparator.comparingInt(str -> {

			switch(str) {
			case "姓を入力して下さい。": return 1;
			case "名を入力して下さい。": return 2;
			case "姓（フリガナ）を入力して下さい": return 3;
			case "名（フリガナ）を入力して下さい": return 4;
			case "郵便番号を入力して下さい": return 5;
			case "都道府県・市区町村を入力して下さい": return 6;
			case "番地を入力して下さい": return 7;
			}
			return Integer.MAX_VALUE;
		}));
		
		System.out.println("入力チェックを終了しました。");
		System.out.println("エラー件数: " + messageList.size() + "件");

		return messageList;
	}
	
}
