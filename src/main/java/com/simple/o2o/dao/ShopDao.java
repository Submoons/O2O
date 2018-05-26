package com.simple.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.simple.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * 新增店铺
	 * @param shop
	 * @return 
	 */
	int insertShop(Shop shop);

	/**
	 * 更新店铺
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
	
	/**
	 * 根据shopId查找店铺信息
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(long shopId);
	
	/**
	 * 分页查询店铺，可输入的条件有：店铺名(模糊)，店铺状态，店铺区域，区域id，owner
	 * @param shopCondition	查询的条件
	 * @param rowIndex	从第几行开始取数据
	 * @param pageSize	返回的条数
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, 
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 返回queryShopList总数
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
}
