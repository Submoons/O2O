package com.simple.o2o.entity;

import java.util.Date;
import java.util.List;

/**
 * 商品实体类
 * @author simple
 *
 */
public class Product {
	//id
	private Long productId;
	//名字
	private String productName;
	//描述
	private String productDesc;
	//简略图
	private String imgAddr;
	//原价
	private String normalPrice;
	//打折价
	private String promotionPrice;
	//权重
	private Integer priority;
	private Date createTime;
	private Date lastEditTime;
	//0.下架      1.在前端展示系统展示
	private Integer enableStatus;
	//商品详情图片列表      一对多关系
	private List<ProductImg> productImgList;
	//商品类别类
	private ProductCategory productCategory;
	//店铺
	private Shop shop;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getImgAddr() {
		return imgAddr;
	}
	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}
	public String getNormalPrice() {
		return normalPrice;
	}
	public void setNormalPrice(String normalPrice) {
		this.normalPrice = normalPrice;
	}
	public String getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(String promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEidtTime() {
		return lastEditTime;
	}
	public void setLastEidtTime(Date lastEidtTime) {
		this.lastEditTime = lastEidtTime;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public List<ProductImg> getProductImgList() {
		return productImgList;
	}
	public void setProductImgList(List<ProductImg> productImgList) {
		this.productImgList = productImgList;
	}
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}
