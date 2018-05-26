package com.simple.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.o2o.dao.ProductDao;
import com.simple.o2o.dao.ProductImgDao;
import com.simple.o2o.dto.ImageHolder;
import com.simple.o2o.dto.ProductExecution;
import com.simple.o2o.entity.Product;
import com.simple.o2o.entity.ProductImg;
import com.simple.o2o.enums.ProductStateEnum;
import com.simple.o2o.exceptions.ProductOperationException;
import com.simple.o2o.exceptions.ShopOperationException;
import com.simple.o2o.service.ProductService;
import com.simple.o2o.util.ImageUtil;
import com.simple.o2o.util.PageCalculator;
import com.simple.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	
	@Override
	@Transactional
	//1.处理缩略图，获取缩略图相对路径并赋值给product
	//2.往tb_product写入商品信息，获取productId
	//3.结合productId批量处理商品详情图
	//4.将商品详情图列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) 
			throws ProductOperationException {
		//空值判断
		if(product != null && product.getShop() != null && product.getShop().getShopId() != null){
			//给商品设置默认属性
			product.setCreateTime(new Date());
			product.setLastEidtTime(new Date());
			//默认为上架状态
			product.setEnableStatus(1);
			//若商品的缩略图不为空则添加
			if(thumbnail != null){
				addThumbnail(product, thumbnail);
			}
			try{
				//创建商品信息
				int effectedNum = productDao.insertProduct(product);
				if(effectedNum <= 0){
					throw new ProductOperationException("创建商品信息失败");
				}
			}catch(Exception e){
				throw new ProductOperationException("创建商品信息失败" + e.toString());
			}
			//若商品详情图不为空则添加
			if(productImgHolderList != null && productImgHolderList.size() > 0){
				addProductImgList(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		}else{
			//传参为空则返回控制错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * 添加缩略图
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		//获取基准路径
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		//处理图片,返回新生成的地址
		String productImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		//设置到product里
		product.setImgAddr(productImgAddr);
	}

	/**
	 * 批量添加商品详情图
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product,List<ImageHolder> productImgHolderList) {
		//获取图片存储地址，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		//遍历图片一次去处理，并添加进productImg实体类
		for(ImageHolder productImgHolder : productImgHolderList){
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		//如果确实是有图片要添加，就执行批量添加操作
		if(productImgList.size() > 0){
			try{
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if(effectedNum <= 0){
					throw new ProductOperationException("创建商品详情图片失败");
				}
			}catch(Exception e){
				throw new ProductOperationException("创建商品详情图片失败:" + e.toString());
			}
			
		}
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}

	@Override
	//1.若缩略图参数有值，则处理缩略图:若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
	//2.若商品详情图参数有值，对商品详情图片同样上述操作
	//3.将tb_product_img下面的该商品原先的商品详情图全部删除
	//4.更新tb_product的信息
	public ProductExecution modifyShop(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ShopOperationException {
		//判断商品是否为空
		if(product != null && product.getShop() != null && product .getShop().getShopId() != null){
			//给商品设置上默认属性值
			product.setLastEidtTime(new Date());
			//若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加
			if(thumbnail != null){
				//获取原有商品类,并从中获取图片地址
				Product tempProduct = productDao.queryProductById(product.getProductId());
				if(tempProduct.getImgAddr() != null){
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}	
				addThumbnail(product, thumbnail);
			}
			//如果有新存入的商品详情图，则将原先的删除，并添加新图片
			if(productImgHolderList != null && productImgHolderList.size() > 0){
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try{
				//更新商品信息
				int effectedNum = productDao.updateProduct(product);
				if(effectedNum <= 0){
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			}catch(Exception e){
				throw new ProductOperationException("更新商品信息失败:" + e.toString());
			}
			
		}else{
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * 删除某个商品下的所有详情图
	 * @param productId
	 */
	private void deleteProductImgList(Long productId) {
		//根据productId获取原来的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		//干掉原来的图片
		for(ProductImg productImg : productImgList){
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//删除数据库里原有的图片信息
		productImgDao.deleteProductImgByProductId(productId);
		
	}

	@Override
	public ProductExecution getProductList(Product productCondition,int pageIndex, int pageSize) {
		//转换起始行数
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		//获取商品列表和总数
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count = productDao.queryProductCount(productCondition);
		//放入ProductExecution
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

}
