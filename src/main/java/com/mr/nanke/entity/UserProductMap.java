package com.mr.nanke.entity;

import java.util.Date;
/***
 * 2.0顾客消费的商品映射
 * @author 夏小颜
 *
 */
public class UserProductMap {
	private Long userProductId;
	private Date createTime;
	private Integer point;
	//顾客信息实体
	private PersonInfo user;
	//商品信息实体
	private Product product;
	private Shop shop;
	//操作员信息实体
	private PersonInfo operator;

	public PersonInfo getOperator() {
		return operator;
	}

	public void setOperator(PersonInfo operator) {
		this.operator = operator;
	}

	public Long getUserProductId() {
		return userProductId;
	}

	public void setUserProductId(Long userProductId) {
		this.userProductId = userProductId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public PersonInfo getUser() {
		return user;
	}

	public void setUser(PersonInfo user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
