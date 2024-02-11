package com.reiko.nail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AddressDto {
    private String address1;
    private String address2;
    private String address3;
    private String kana1;
    private String kana2;
    private String kana3;
    private String prefcode;
    private String zipcode;
    // コンストラクタ
    public AddressDto(@JsonProperty("address1") String address1,
                      @JsonProperty("address2") String address2,
                      @JsonProperty("address3") String address3,
                      @JsonProperty("kana1") String kana1,
                      @JsonProperty("kana2") String kana2,
                      @JsonProperty("kana3") String kana3,
                      @JsonProperty("prefcode") String prefcode,
                      @JsonProperty("zipcode") String zipcode) {
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.kana1 = kana1;
        this.kana2 = kana2;
        this.kana3 = kana3;
        this.prefcode = prefcode;
        this.zipcode = zipcode;
    }
}

