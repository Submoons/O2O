/**
 * 
 */
$(function(){
	
	getlist(); //调用
	
	//根据用户信息获取店铺列表
	function getlist(e){
		$.ajax({
			url:'/o2o/shopadmin/getshoplist',
			type:"GET",
			dataType:"json",
			success:function(data){
				if(data.success){
					handleList(data.shopList);
					handleUser(data.user);
				}
			}
		});
	}
	
	//显示用户名
	function handleUser(data){
		$('#user-name').text(data.name);
	}
	
	//显示列表
	function handleList(data){
		var html = '';
		//迭代进去
		data.map(function(item, index){
			html += '<div class="row row-shop"><div class="col-40">'
				 + item.shopName + '</div><div class="col-40">'
				 + shopStatus(item.enableStatus)
				 + '</div><div class="col-20">'
				 + goShop(item.enableStatus, item.shopId) + '</div></div>';
		});
		$('.shop-wrap').html(html);
	}
	
	//根据状态值用文字输出
	function shopStatus(status){
		if(status == 0){
			return '审核中';
		}else if(status == -1){
			return '店铺非法';
		}else if(status == 1){
			return '审核通过';
		}
	}
	
	//生成操作的点击链接
	function goShop(status, id){
		if(status == 1){
			return '<a href="/o2o/shopadmin/shopmanagement?shopId=' + id + '">进入</a>';
		}else{
			return '';
		}
	}
});