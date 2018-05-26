package com.simple.o2o.enums;

public enum ProductCategoryStateEnum {
	SUCCESS(1, "创建成功"), EMPTY_LIST(-1002,"添加数少于1"),INNER_ERROR(-1001, "内部系统错误");
	private int state;
	private String stateInfo;

	private ProductCategoryStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 依据传入的state返回相应的enum值
	 */
	public static ProductCategoryStateEnum stateOf(int state) {
		for (ProductCategoryStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

}