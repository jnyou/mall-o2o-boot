package com.mr.nanke.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
	/***
	 * 由于系统的分隔符不同 需要指定替换 
	 * 1. win：( \ ) 
	 * 2. linux/max：( / )
	 */
	// 系统自动获取文件的分隔符
	private static String seperator = System.getProperty("file.separator");
	
	//使用springboot注入属性
	private static String winPath;

	private static String linuxPath;

	private static String shopPath;

	@Value("${win.base.path}")
	public void setWinPath(String winPath) {
		PathUtil.winPath = winPath;
	}

	@Value("${linux.base.path}")
	public void setLinuxPath(String linuxPath) {
		PathUtil.linuxPath = linuxPath;
	}

	@Value("${shop.relevant.path}")
	public void setShopPath(String shopPath) {
		PathUtil.shopPath = shopPath;
	}
	
	/***
	 * 返回项目图片的根路径
	 * @return
	 */
	public static String getImgBasePath() {
		String os = System.getProperty("os.name"); // 系统自动识别出系统的名字 是Linux还是window或者其他。。。
		String basePath = "";
		if(os.toLowerCase().startsWith("win")) {
			basePath = winPath;
		}else {
			basePath = linuxPath;
		}
		basePath = basePath.replace("/", seperator);
		return basePath;
	}
	
	/***
	 * 获取商品图片的地址保存在自己的店铺下
	 * @param shopId
	 * @return
	 */ 
	public static String getShopImagePath(long shopId) {
		String imagaPath = shopPath + shopId + seperator;
		return imagaPath.replace("/", seperator);
		
	}
}
