package com.simple.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.simple.o2o.entity.Product;

public interface ProductDao {
	
	/**
	 * 插入商品
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);
	
	/**
	 * 更新商品信息
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
	
	/**
	 * 通过productId查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);
	
	/**
	 * 分页查询商品，可输入的条件有:商品名(模糊),商品状态,店铺id,商品类别
	 * 
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition, 
								   @Param("rowIndex") int rowIndex, 
								   @Param("pageSize") int pageSize);
	/**
	 * 查询对应商品的总数
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	
	/**
	 * 删除商品类别之前，将商品类别id置空
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
	
}
