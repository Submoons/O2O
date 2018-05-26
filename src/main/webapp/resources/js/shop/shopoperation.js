/**
 * 
 */
$(function(){//页面加载执行
	var shopId = getQueryString('shopId');//获取shopId
	var isEdit = shopId ? true : false; //有shopId的为编辑页面，没有则为注册
	var initUrl = '/o2o/shopadmin/getshopinitinfo';//获取店铺初始信息
	var registerShopUrl = '/o2o/shopadmin/registerShop'; //店铺注册url
	var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=' + shopId; //获取编辑页面信息
	var editShopUrl = '/o2o/shopadmin/modifyshop'; //编辑页面
	//判断是修改还是注册操作
	if(!isEdit){
		getShopInitInfo();//调用
	}else{
		getShopInfo(shopId);
	}
	
	function getShopInfo(shopId){
		$.getJSON(shopInfoUrl, function(data){
			if(data.success){
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				var shopCategory = '<option data-id="' + shop.shopCategory.shopCategoryId + '"selected>' + shop.shopCategory.shopCategoryName + '</option>';
				
				var tempAreaHtml = '';
				data.areaList.map(function(item, index){
					tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
				});
				$('#shop-category').html(shopCategory);
				$('#shop-category').attr('disabled','disabled');
				$('#area').html(tempAreaHtml);
				$("#area option [data-id='" + shop.area.areaId + "']").attr("selected","selected");
			}
		});
	}
	
	
	//获取店铺基本信息
	function getShopInitInfo(){
		//通过 HTTP GET 请求载入 JSON 数据。
		$.getJSON(initUrl, function(data){
			if(data.success){
				var tempHtml = '';	//店铺分类
				var tempAreaHtml = ''; //区域分类
				//转为数组处理(遍历)
				data.shopCategoryList.map(function(item,index){
					tempHtml += '<option data-id="' + item.shopCategoryId + '">' + item.shopCategoryName + '</option>';
				});
				data.areaList.map(function(item,index){
					tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
				});
				//放入前台
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});	
	}
	
	//提交按钮事件
	$('#submit').click(function(){
		var shop = {};
		//判断是否需要shopId
		if(isEdit){
			shop.shopId = shopId;
		}
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();
		shop.shopCategory = {
			shopCategoryId : $('#shop-category').find('option').not(function(){
				return !this.selected;
			}).data('id')
		};
		shop.area = {
			areaId : $('#area').find('option').not(function(){
				return !this.selected;
			}).data('id')
		};
		//图片传入
		var shopImg = $('#shop-img')[0].files[0];
		var formData = new FormData();
		formData.append('shopImg',shopImg);
		formData.append('shopStr',JSON.stringify(shop));
		//验证码传入
		var verifyCodeActual = $('#j_captcha').val();
		if(!verifyCodeActual){
			$.toast("请输入验证码");
		}
		formData.append('verifyCodeActual', verifyCodeActual);
		
		$.ajax({
			url: (isEdit ? editShopUrl : registerShopUrl),
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(data){
				if(data.success){
					$.toast("提交成功!");
				}else{
					$.toast("提交失败!" + data.errMsg);
				}
				$('#captcha_img').click();
			}
		});
	});
});
