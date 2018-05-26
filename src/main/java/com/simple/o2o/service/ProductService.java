package com.simple.o2o.service;

import java.util.List;

import com.simple.o2o.dto.ImageHolder;
import com.simple.o2o.dto.ProductExecution;
import com.simple.o2o.entity.Product;
import com.simple.o2o.exceptions.ProductOperationException;
import com.simple.o2o.exceptions.ShopOperationException;

public interface ProductService {
	
	/**
	 * 添加商品信息及图片处理
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgHolderList) throws ProductOperationException;
	
	/**
	 * 通过商品id查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);
	
	/**
	 * 更新商品信息，包括对图片处理
	 * @param product
	 * @param thumbnail
	 * @return
	 * @throws ShopOperationException
	 */
	ProductExecution modifyShop(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ShopOperationException;
	
	/**
	 * 根据productCondition分页返回相应店铺列表数据
	 * 
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

}	
