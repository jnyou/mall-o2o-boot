package com.mr.nanke.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mr.nanke.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	// 获取 classpath 的绝对路径 也就是resources资源文件夹的目录
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();
	static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	// 店铺的图片处理
	public static String generateThumbnail(ImageHolder imageHolder, String targetPath) {
		logger.info("===处理图片start===");
		String realFileName = getRandomFileName(); // 获得随机文件名字 2019080317230075934
		logger.debug("文件处理问完后的随机名字" + realFileName);
		String extension = getFileExtension(imageHolder.getFileName()); // 获取文件的扩展名
		makeDirPath(targetPath); // 判断 进行创建或保存的文件路径
		String relativePath = targetPath + realFileName + extension; // 文件最终完整路径
																		// upload\item\shop\34\2019080317230075934.jpg
		logger.debug("文件的最终路径：" + relativePath);
		File dest = new File(PathUtil.getImgBasePath() + relativePath); // 把文件放到项目的根目录下
																		// D:\\projectdev\\image\\upload\\item\\shop\\34\\2019080317230075934.jpg
		logger.debug("文件放的位置：" + dest.toString());
		try {
			/**
			 * watermark(位置，ImageIO.read(水印图片的路径)，水印透明度).outputQuality(图片压缩值).toFile(放入的新位置)
			 */
			Thumbnails.of(imageHolder.getThumbnailInputStream()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativePath;

	}

	/**
	 * 创建目录路径所涉及到的目录， 即/home/work/xiangze/xxx.jpg 那么这三个文件夹都要自动创建出来 home work xiangze
	 * 
	 * @param targetPath
	 *            这个参数是相对路径
	 */
	private static void makeDirPath(String targetPath) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetPath; // 绝对路径 也就是相对路径
		File dirFile = new File(realFileParentPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
	}

	/***
	 * 获取文件的扩展名
	 * 
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		// String originalFileName = thumbnail.getName();
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 生成随机的文件名 ----使用当前的年月日小时分钟秒钟 + 五位随机数
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		// 获取五位随机数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	public static void main(String[] args) {
		Thumbnails.of(new File(""));
	}

	/***
	 * @param storePath:是文件的路径还是目录的路径
	 *            如果是文件则删除文件 如果是目录，则删除目录下面的所有文件 删除图片的方法
	 */
	public static void deletefileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath); // 获得全路径
		// 如果此路径存在
		if (fileOrPath.exists()) {
			// 如果是目录 删除下面所有的文件
			if (fileOrPath.isDirectory()) {
				File[] files = fileOrPath.listFiles(); // 获得所有文件
				// 遍历
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}

	// 商品的图片处理
	public static String generateNormalImg(ImageHolder imageHolder, String targetPath) {
		logger.info("===处理图片start===");
		String realFileName = getRandomFileName(); // 获得随机文件名字 2019080317230075934
		logger.debug("文件处理问完后的随机名字" + realFileName);
		String extension = getFileExtension(imageHolder.getFileName()); // 获取文件的扩展名
		makeDirPath(targetPath); // 判断 进行创建或保存的文件路径
		String relativePath = targetPath + realFileName + extension; // 文件最终完整路径
																		// upload\item\shop\34\2019080317230075934.jpg
		logger.debug("文件的最终路径：" + relativePath);
		File dest = new File(PathUtil.getImgBasePath() + relativePath); // 把文件放到项目的根目录下
																		// D:\\projectdev\\image\\upload\\item\\shop\\34\\2019080317230075934.jpg
		logger.debug("文件放的位置：" + dest.toString());
//		try {
//			/**
//			 * watermark(位置，ImageIO.read(水印图片的路径)，水印透明度).outputQuality(图片压缩值).toFile(放入的新位置)
//			 */
//			Thumbnails.of(imageHolder.getThumbnailInputStream()).size(337, 640).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath +"/watermark.png")), 0.25f).outputQuality(0.9f).toFile(dest);
//					
//		} catch (IOException e) {
//			logger.error(e.getMessage());
//			throw new RuntimeException("创建缩略图失败：" + e.toString());
//		}
		return relativePath;
	}

}
