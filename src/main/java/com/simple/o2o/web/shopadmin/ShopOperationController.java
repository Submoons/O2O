package com.simple.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="shopadmin", method=RequestMethod.GET)
public class ShopOperationController {
	@RequestMapping(value="/shopoperation")
	public String shopOperation(){
		
		return "shop/shopoperation";
	}
	
	@RequestMapping(value="/shoplist")
	public String shopList(){
		
		return "shop/shoplist";
	}
	
	@RequestMapping(value="/shopmanagement")
	public String shopManagement(){
		
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value="/productcategorymanagement")
	public String productCategoryManagement(){
		
		return "shop/productcategorymanagement";
	}
	
	@RequestMapping(value="/productoperation")
	public String productOperation(){
		
		return "shop/productoperation";
	}
	
	@RequestMapping(value="/productmanagement")
	public String productManagement(){
		return "shop/productmanagement";
	}
}
