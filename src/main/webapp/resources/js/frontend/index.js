$(function() {
    var url = '/o2o/frontend/getmainlistinfo';

    $.getJSON(url, function (data) {
    	console.log(data);
        if (data.success) {
            var headLineList = data.headLines;
            var swiperHtml = '';
            headLineList.map(function (item, index) {
                swiperHtml += ''
                            + '<div class="swiper-slide img-wrap">'
                            +      '<img class="banner-img" src="'+ getContextPath() + item.lineImg +'" alt="'+ item.lineName +'">'
                            + '</div>';
            });
            //轮播组件 封装者
            $('.swiper-wrapper').html(swiperHtml);
            //设定轮播图的轮换时间为3S
            $(".swiper-container").swiper({
                autoplay: 3000,
                //用户对轮播图进行操作，是否自动停止
                autoplayDisableOnInteraction: true
            });
            //获取一级列表
            var shopCategoryList = data.categoryList;
            var categoryHtml = '';
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                             +  '<div class="col-50 shop-classify" data-category='+ item.shopCategoryId +'>'
                             +      '<div class="word">'
                             +          '<p class="shop-title">'+ item.shopCategoryName +'</p>'
                             +          '<p class="shop-desc">'+ item.shopCategoryDesc +'</p>'
                             +      '</div>'
                             +      '<div class="shop-classify-img-warp">'
                             +          '<img class="shop-img" src="'+ getContextPath() + item.shopCategoryImg +'">'
                             +      '</div>'
                             +  '</div>';
            });
            //展示在前台
            $('.row').html(categoryHtml);
        }
    });
    //点击我，则显示侧边栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });
    
    //点击一级大类，则携带shopCategoryId进入该类别下面的所有店铺列表
    $('.row').on('click', '.shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
        window.location.href = newUrl;
    });

});
