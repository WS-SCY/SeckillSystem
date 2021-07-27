package com.scyproject.redis;

public class GoodsKey extends BasePrefix{
	public GoodsKey(String prefix, int expireSeconds) {
		super(prefix, expireSeconds);
	}

	public GoodsKey(String prefix) {
		super(prefix);
	}
	//商品列表
	public static GoodsKey getGoodsList = new GoodsKey("gl",600);
	//商品详情
	public static GoodsKey getGoodsDetail = new GoodsKey("gd", 10);
	//商品库存
	public static GoodsKey getGoodsStock= new GoodsKey("gs",0 );
}
