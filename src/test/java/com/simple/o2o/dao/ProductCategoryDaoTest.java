package com.simple.o2o.dao;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.simple.o2o.BaseTest;
import com.simple.o2o.entity.ProductCategory;

public class ProductCategoryDaoTest extends BaseTest{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	@Ignore
	public void testQueryProductCategoryList(){
		long shopId = 3;
		List<ProductCategory> queryProductCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("店铺商品类别数"+queryProductCategoryList.size());
		
	}
	
	@Test
	@Ignore
	public void testBatchInsertProductCategory(){
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryName("商品类别1");
		productCategory.setPriority(1);
		productCategory.setCreateTime(new Date());
		productCategory.setShopId(3l);
		
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("商品类别2");
		productCategory2.setPriority(2);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(3L);
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory);
		productCategoryList.add(productCategory2);
		int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectedNum);
	}
	
	@Test
	public void testDeleteProductCategory(){
		long shopId = 3;
		List<ProductCategory> queryProductCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		for(ProductCategory pc : queryProductCategoryList){
			if("商品类别1".equals(pc.getProductCategoryName()) || "商品类别2".equals(pc.getProductCategoryName())){
				int effectedNum = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
				assertEquals(1,effectedNum);
			}
		}
		
	}
	
	
}
