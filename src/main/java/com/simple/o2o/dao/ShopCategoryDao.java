package com.simple.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.simple.o2o.entity.ShopCategory;

public interface ShopCategoryDao {
	/**
	 * 列出店铺类别
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
