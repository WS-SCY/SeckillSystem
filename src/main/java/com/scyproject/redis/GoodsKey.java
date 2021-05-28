package com.scyproject.redis;

public class GoodsKey extends BasePrefix{
	public GoodsKey(String prefix, int expireSeconds) {
		super(prefix, expireSeconds);
	}

	public GoodsKey(String prefix) {
		super(prefix);
	}

	public static GoodsKey getGoodsList = new GoodsKey("gl",60);
	public static GoodsKey getGoodsDetail = new GoodsKey("gd", 60);
	public static GoodsKey getGoodsStock= new GoodsKey("gs",0 );
}
