$(function() {
	//从URL里获取productId参数的值
	var productId = getQueryString('productId');
	//通过productId获取商品信息的URL
	var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId; //获取编辑页面信息
	//获取当前店铺设定的商品类别列表的URL
	var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
	//更新修改商品列表的URL
	var productPostUrl = '/o2o/shopadmin/modifyproduct';
	//商品编辑和添加是同一个页面，改标识符标明是编辑还是添加
	var isEdit = false;
	if (productId) {
		//有id，去到编辑页面
		getInfo(productId);
		isEdit = true;
	} else {
		getCategory();
		productPostUrl = '/o2o/shopadmin/addproduct';
	}

	//获取需要编辑的商品信息并赋值给表单中
	function getInfo(id) {
		$.getJSON(infoUrl,function(data) {
			if (data.success) {
				//从json数据中获取要编辑的商品信息放入表单中
				var product = data.product;
				$('#product-name').val(product.productName);
				$('#product-desc').val(product.productDesc);
				$('#priority').val(product.priority);
				$('#normal-price').val(product.normalPrice);
				$('#promotion-price').val(product.promotionPrice);
				//获取原有商品的商品类别和改店铺所有的商品类别列表	
				var optionHtml = '';
				var optionArr = data.productCategoryList;
				var optionSelected = product.productCategory.productCategoryId; //默认选中的商品类别
				optionArr.map(function(item, index) {
					var isSelect = optionSelected === item.productCategoryId ? 'selected': '';
					optionHtml += '<option data-value="'
							+ item.productCategoryId
							+ '"'
							+ isSelect
							+ '>'
							+ item.productCategoryName
							+ '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}

	//添加新商品页面 提供商品类别列表
	function getCategory() {
		$.getJSON(categoryUrl, function(data) {
			if (data.success) {
				var productCategoryList = data.data;
				var optionHtml = '';
				productCategoryList.map(function(item,index){
					optionHtml += '<option data-value="'
							+ item.productCategoryId + '">'
							+ item.productCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}

	//针对商品详情图控件组，若该控件组的最后一个元素发生变化(即上传了图片),
	//且控件总数未达到6个，则生成新的一个文件上传控件
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		if ($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});

	//提交按钮事件响应，对应编辑和添加不同操作
	$('#submit').click(function() {
		//创建商品json对象，并从表单里面获取对应的属性值
		var product = {};
		product.productName = $('#product-name').val();
		product.productDesc = $('#product-desc').val();
		product.priority = $('#priority').val();
		product.normalPrice = $('#normal-price').val();
		product.promotionPrice = $('#promotion-price').val();
		//获取选的的商品类别值
		product.productCategory = {
			productCategoryId : $('#category').find('option').not(function() {
				return !this.selected;
			}).data('value')
		};
		product.productId = productId;
		//获取缩略图文件流
		var thumbnail = $('#small-img')[0].files[0];
		//console.log(thumbnail);
		//生成表单对象，用于接收参数并传递给后台
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		//遍历商品详情控件，获取里面的文件流
		$('.detail-img').map(function(index, item) {
			//判断该控件是否已经选择了文件
			if ($('.detail-img')[index].files.length > 0) {
				//将第i个文件流赋值给key为productImg的表单键值对里
				formData.append('productImg' + index, $('.detail-img')[index].files[0]);
			}
		});
		//将product json对象转成字符流保存至表单对象key为productStr的键值对里
		formData.append('productStr', JSON.stringify(product));
		//获取表单里输入的验证码
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			url : productPostUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					$('#captcha_img').click();
				} else {
					$.toast('提交失败！');
					$('#captcha_img').click();
				}
			}
		});
	});

});