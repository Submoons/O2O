package com.simple.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.o2o.dto.ImageHolder;
import com.simple.o2o.dto.ShopExecution;
import com.simple.o2o.entity.Area;
import com.simple.o2o.entity.PersonInfo;
import com.simple.o2o.entity.Shop;
import com.simple.o2o.entity.ShopCategory;
import com.simple.o2o.enums.ShopStateEnum;
import com.simple.o2o.service.AreaService;
import com.simple.o2o.service.ShopCategoryService;
import com.simple.o2o.service.ShopService;
import com.simple.o2o.util.CodeUtil;
import com.simple.o2o.util.HttpServletRequestUtil;

/**
 * 实现店铺管理相关逻辑
 * @author simple
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaServcie;

	/**
	 * 点击店铺列表信息时显示的相关管理信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getshopmanagementinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取店铺id
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		//如果id没有
		if(shopId <= 0){
			//前端没传shopId,试着从session中获取(是否登录...)
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			//还是没获取到
			if(currentShopObj == null){
				//重定向到之前的页面去
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			}else{
				//如果获取到了,从currnetShopObj中取出shopId
				Shop currentShop = (Shop)currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else{
			//shopId不为0的话，前端传过来了
			Shop currentShop = new Shop();
			//设置shopId
			currentShop.setShopId(shopId);
			//设置对象到session中
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
		
	}
	
	/**
	 * 获取店铺列表信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshoplist", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("simple");
		//放入session中
		request.getSession().setAttribute("user", user);
		//从session中获取用户
		user = (PersonInfo) request.getSession().getAttribute("user");
		try{
			//把用户设置到店铺
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			//调用service方法分页列表
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		
		return modelMap;
	}
	
	/**
	 * 获取店铺区域信息和店铺类别信息
	 * 返回到前端页面直接显示
	 * 
	 * @return
	 */
	@RequestMapping(value="/getshopinitinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try{
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaServcie.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;
	}
	
	/**
	 * 根据店铺id获取店铺信息然后修改
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshopbyid", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取id
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		//判断id
		if(shopId > -1){
			try{
				//获取该id的店铺信息
				Shop shop = shopService.getByShopId(shopId);
				//查找它的区域信息
				List<Area> areaList = areaServcie.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
				
			}catch(Exception e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg","empty shopId");
		}
		
		return modelMap;
	}
	
	/**
	 * 店铺修改
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//验证验证码的正确性
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		
		//1.接收并转化相应的参数,包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		//转化为shop对象
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try{
			shop = mapper.readValue(shopStr, Shop.class);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//图片处理相关
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//判断是否有上传的文件流
		if(commonsMultipartResolver.isMultipart(request)){
			//有将request转换
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			//提取出文件流
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		
		//2修改店铺
		//判断店铺实例和shopId是否为空
		if(shop != null && shop.getShopId() != null){
			
			ShopExecution se;
			try {
				if(shopImg == null){
					se = shopService.modifyShop(shop, null);
				}else{
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageHolder);
				}
				if(se.getState() == ShopStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
				}else{
					modelMap.put("success", false);
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺id");
			return modelMap;
		}
	}
	
	/**
	 * 店铺注册
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//验证验证码的正确性
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		
		//1.接收并转化相应的参数,包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		//转化为shop对象
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try{
			shop = mapper.readValue(shopStr, Shop.class);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//图片处理相关
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//判断是否有上传的文件流
		if(commonsMultipartResolver.isMultipart(request)){
			//有将request转换
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			//提取出文件流
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else{
			//没有文件流就报错
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		//2注册店铺
		//判断店铺实例和shopImg是否为空
		if(shop != null && shopImg !=null){
			//店主信息从session中获取 
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
				se = shopService.addShop(shop, imageHolder);
				
				if(se.getState() == ShopStateEnum.CHECK.getState()){
					modelMap.put("success", true);
					//该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList == null || shopList.size() == 0){
						shopList = new ArrayList<Shop>();
					}
					//把新店铺加入
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);

				}else{
					modelMap.put("success", false);
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	
	
	
	
	
	/**
	 * 处理shopImg的类型转换
	 * 
	 * @param ins
	 * @param file
	 
	private static void inputStreamToFile(InputStream ins, File file){
		FileOutputStream os = null;
		
		try{
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while((bytesRead = ins.read(buffer)) != -1){
				os.write(buffer, 0, bytesRead);
			}
		}catch(Exception e){
			throw new RuntimeException("调用inputStreamToFile产生异常:" + e.getMessage());
		}finally{
			try{
				if(os != null){
					os.close();
				}
				if(ins != null){
					ins.close();
				}
			}catch(IOException e){
				throw new RuntimeException("inputStreamToFile关闭IO产生异常:" + e.getMessage());
			}
		}
	}*/
}
