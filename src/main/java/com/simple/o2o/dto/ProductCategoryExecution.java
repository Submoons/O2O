package com.simple.o2o.dto;

import java.util.List;

import com.simple.o2o.entity.ProductCategory;
import com.simple.o2o.enums.ProductCategoryStateEnum;

public class ProductCategoryExecution {
	//结果状态
	private int state;
	//状态标识
	private String stateInfo;
	
	//ProductCategory列表
	private List<ProductCategory> productCategoryList;

	public ProductCategoryExecution() {
		
	}

	//操作失败时候的构造器
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum){
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	
	//操作成功时候的构造器
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum, List<ProductCategory> productCategoryList){
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.productCategoryList = productCategoryList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
	
	
	
}
