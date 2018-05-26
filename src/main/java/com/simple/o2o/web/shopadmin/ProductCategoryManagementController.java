package com.simple.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.o2o.dto.ProductCategoryExecution;
import com.simple.o2o.dto.Result;
import com.simple.o2o.entity.ProductCategory;
import com.simple.o2o.entity.Shop;
import com.simple.o2o.enums.ProductCategoryStateEnum;
import com.simple.o2o.exceptions.ProductCategoryOperationException;
import com.simple.o2o.service.ProductCategoryService;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
	@Autowired
	private ProductCategoryService productCategoryService;
	
	/**
	 * 获取商品类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getproductcategorylist", method=RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCagtegoryList(HttpServletRequest request){
		/*测试用
			Shop shop = new Shop();
			shop.setShopId(3l);
			request.getSession().setAttribute("currentShop", shop);
		*/
		
		//获取店铺管理信息存的currentShop
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		if(currentShop != null && currentShop.getShopId() > 0){
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, list);
		}else{
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false, ps.getStateInfo(), ps.getState());
		}
	}
	
	/**
	 * 批量添加商品类别
	 * @param productCategoryList
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addproductcategorys", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		//给每一个list设置shopId
		for(ProductCategory ps:productCategoryList){
			ps.setShopId(currentShop.getShopId());
		}
		//判断是否为空
		if(productCategoryList != null && productCategoryList.size() > 0){
			try{
				ProductCategoryExecution pe = productCategoryService.batchAddProductCategoryList(productCategoryList);
				if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch(ProductCategoryOperationException e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个类别");
		}
		
		return modelMap;
	}
	
	@RequestMapping(value="/removeProductCategory", method=RequestMethod.POST) 
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(productCategoryId != null && productCategoryId > 0){
			try{
				Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
				if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch(ProductCategoryOperationException e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");

		}
		return modelMap;
	}
}
