$(function() {
	var loading = false;
	var maxItems = 20;
	var pageSize = 3;

	var listUrl = '/o2o/frontend/getproductshoplist';
	// 默认页码
	var pageNum = 1;
	var shopId = getQueryString('shopId');
	var productCategoryId = '';
	var productName = '';

	var searchDivUrl = '/o2o/frontend/getlistdetailinfo?shopId=' + shopId;
	// 店铺的基本信息显示及商品类别列表供搜索
	getSearchDivData();
	// 预先加载10条商品信息
	addItems(pageSize, pageNum);

	function getSearchDivData() {
		var url = searchDivUrl;
		$
				.getJSON(
						url,
						function(data) {
							if (data.success) {
								var shop = data.shop;
								console.log(shop);
								$('#shop-cover-pic').attr('src', getContextPath() + shop.shopImg);
								$('#shop-update-time').html(
										new Date(shop.lastEditTime)
												.Format("yyyy-MM-dd"));
								$('#shop-name').html(shop.shopName);
								$('#shop-desc').html(shop.shopDesc);
								$('#shop-addr').html(shop.shopAddr);
								$('#shop-phone').html(shop.phone);
								//获取商品的信息
								var productCategoryList = data.productCategories;
								var html = '';
								productCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-product-search-id='
													+ item.productCategoryId
													+ '>'
													+ item.productCategoryName
													+ '</a>';
										});
								$('#shopdetail-button-div').html(html);
							}
						});
	}

	//获取分页查询出来的商品列表信息
	function addItems(pageSize, pageIndex) {
		// 生成新条目的HTML
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&productCategoryId=' + productCategoryId
				+ '&productName=' + productName + '&shopId=' + shopId;
		//设定加载符，若还在后台加载数据则不能再次访问后台，避免多次重复加载
		loading = true;
		$.getJSON(url, function(data) {
			if (data.success) {
				maxItems = data.count;
				var html = '';
				//遍历商品列表，拼接出卡片列表展示出来
				data.productList.map(function(item, index) {
					html += '' + '<div class="card" data-product-id='
							+ item.productId + '>'
							+ '<div class="card-header">' + item.productName
							+ '</div>' + '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ getContextPath() + item.imgAddr + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.productDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>点击查看</span>' + '</div>'
							+ '</div>';
				});
				$('.list-div').append(html);
				//当前已经显示出来的条数
				var total = $('.list-div .card').length;
				//若当前显示出来的条数等于或者大于数据库中的列表总数，则停止加载
				if (total >= maxItems) {
					// 隐藏加载提示符
					$('.infinite-scroll-preloader').hide();
				}else{
					$('.infinite-scroll-preloader').show();
				}
				//翻页显示
				pageNum += 1;
				loading = false;
				$.refreshScroller();
			}
		});
	}
	//下滑自动分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});
	//显示新商品的时候重置页码，清空原来的商品列表，按新列表的条件查询出对应商品列表
	$('#shopdetail-button-div').on(
			'click',
			'.button',
			function(e) {
				//获取商品类别的ID
				productCategoryId = e.target.dataset.productSearchId;
				if (productCategoryId) {
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						productCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					$('.list-div').empty();
					pageNum = 1;
					addItems(pageSize, pageNum);
				}
			});

	//点击卡片进入到该商品的详情页
	$('.list-div').on(
			'click',
			'.card',
			function(e) {
				var productId = e.currentTarget.dataset.productId;
				window.location.href = '/o2o/frontend/productdetail?productId='
						+ productId;
			});
	//根据商品名字查询
	$('#search').on('change', function(e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
