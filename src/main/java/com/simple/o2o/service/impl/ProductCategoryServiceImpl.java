package com.simple.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.o2o.dao.ProductCategoryDao;
import com.simple.o2o.dao.ProductDao;
import com.simple.o2o.dto.ProductCategoryExecution;
import com.simple.o2o.entity.ProductCategory;
import com.simple.o2o.enums.ProductCategoryStateEnum;
import com.simple.o2o.exceptions.ProductCategoryOperationException;
import com.simple.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{

	@Autowired
	private ProductCategoryDao productCategoryDao; 
	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	public ProductCategoryExecution batchAddProductCategoryList(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList != null && productCategoryList.size() > 0){
			try{
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effectedNum <= 0){
					throw new ProductCategoryOperationException("店铺类别创建失败");
				}else{
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			}catch(Exception e){
				throw new ProductCategoryOperationException("batchAddProductCategoryList error" + e.getMessage());
			}
		}else{
			return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException{
		//先解除tb_product里的商品与productCategoryId的关联
		try{
			//清除商品类别id
			int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
			if(effectedNum < 0){
				throw new ProductCategoryOperationException("商品类别更新失败");
			}
		}catch(Exception e){
			throw new ProductCategoryOperationException("deleteProductCategory error" + e.getMessage());
		}
		//删除商品类别
		try{
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum <= 0){
				throw new ProductCategoryOperationException("商品类别删除失败");
			}else{
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch(Exception e){
			throw new ProductCategoryOperationException("deleteProductCategory error" + e.getMessage());
		}
	}

}
