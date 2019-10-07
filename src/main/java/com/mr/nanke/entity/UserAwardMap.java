package com.mr.nanke.entity;

import java.util.Date;
/***
 * 
 * 2.0 顾客已领取奖品的映射
 * @author 夏小颜
 *
 */
public class UserAwardMap {

	private Long userAwardId;
	private Date createTime;
	//使用状态0：未兑换，1.已兑换
	private Integer usedStatus;
	//领取奖品所消耗的积分
	private Integer point;
	//奖品信息实体类
	private Award award;
	//店铺实体类
	private Shop shop;
	//顾客信息
	private PersonInfo user;
	//操作员的信息实体类
	private PersonInfo operator;
	
	public PersonInfo getOperator() {
		return operator;
	}

	public void setOperator(PersonInfo operator) {
		this.operator = operator;
	}

	public Long getUserAwardId() {
		return userAwardId;
	}

	public void setUserAwardId(Long userAwardId) {
		this.userAwardId = userAwardId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUsedStatus() {
		return usedStatus;
	}

	public void setUsedStatus(Integer usedStatus) {
		this.usedStatus = usedStatus;
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

	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
