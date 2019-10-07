package com.mr.nanke.dto;

import java.io.InputStream;

public class ImageHolder {
	
	/**
	 * 封装流对象及名字  		简化添加图片的方法参数
	 */
	private InputStream thumbnailInputStream;
	
	private String fileName ;

	
	
	public ImageHolder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImageHolder(InputStream thumbnailInputStream, String fileName) {
		super();
		this.thumbnailInputStream = thumbnailInputStream;
		this.fileName = fileName;
	}

	public InputStream getThumbnailInputStream() {
		return thumbnailInputStream;
	}

	public void setThumbnailInputStream(InputStream thumbnailInputStream) {
		this.thumbnailInputStream = thumbnailInputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
