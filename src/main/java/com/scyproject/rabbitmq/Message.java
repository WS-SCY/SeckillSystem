package com.scyproject.rabbitmq;

import com.scyproject.domain.MiaoshaUser;

public class Message {
	private MiaoshaUser user;
	private long goodsId;

	public Message(MiaoshaUser miaoshaUser1, long goodsId1){
		this.user = miaoshaUser1;
		this.goodsId = goodsId1;
	}

	public Message() {

	}

	public MiaoshaUser getUser() {
		return user;
	}
	public void setUser(MiaoshaUser user) {
		this.user = user;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
}
