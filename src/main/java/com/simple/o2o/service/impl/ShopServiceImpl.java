package com.simple.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.o2o.dao.ShopDao;
import com.simple.o2o.dto.ImageHolder;
import com.simple.o2o.dto.ShopExecution;
import com.simple.o2o.entity.Shop;
import com.simple.o2o.enums.ShopStateEnum;
import com.simple.o2o.exceptions.ShopOperationException;
import com.simple.o2o.service.ShopService;
import com.simple.o2o.util.ImageUtil;
import com.simple.o2o.util.PageCalculator;
import com.simple.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService{

	@Autowired
	private ShopDao shopDao;
	
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
		//空值判断
		if(shop == null){
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try{
			//给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if(effectedNum <= 0){
				throw new ShopOperationException("店铺创建失败");
			}else{
				if(thumbnail.getImage() != null){
					//存储图片
					try{
						addShopImg(shop, thumbnail);
					}catch(Exception e){
						throw new ShopOperationException("addShopImg error" + e.getMessage());
					}
					//更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum <= 0){
						throw new ShopOperationException("更新店铺图片失败");
					}
				}
			}
		}catch(Exception e){
			throw new ShopOperationException("addShop error" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	/**
	 * 存储图片
	 * @param shop
	 * @param shopImg
	 */
	private void addShopImg(Shop shop, ImageHolder thumbnail) {
		//获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) 
			throws ShopOperationException {
		//判断shop是否空
		if(shop == null || shop.getShopId() == null){
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else{
			try{
				//1.判断是否需要处理图片
				if(thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())){
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if(tempShop.getShopImg() != null){
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					//重新存图片
					addShopImg(shop, thumbnail);
				}
				//2.更新店铺信息
				shop.setLastEditTime(new Date());
				//验证更新影响的条数
				int effectedNum = shopDao.updateShop(shop);
				if(effectedNum <= 0){
					//更新失败
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				}else{
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}	
			}catch(Exception e){
				throw new ShopOperationException("modifyShop error" + e.getMessage());
			}	
		}	
		
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//转换起始行数
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		//获取店铺列表和总数
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		//放入ShopExecution
		ShopExecution se = new ShopExecution();
		if(shopList != null){
			se.setShopList(shopList);
			se.setCount(count);
		}else{
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}	
		return se;
	}


}
