package com.reiko.nail.dao;

import org.apache.ibatis.annotations.Mapper;

import com.reiko.nail.entity.ShiireRirekiEntity;

@Mapper
public interface RirekiDao {

	void insertShiireRireki(ShiireRirekiEntity shiireRirekiEntity);
}
