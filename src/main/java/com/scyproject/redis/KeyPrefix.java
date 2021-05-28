package com.scyproject.redis;

public interface KeyPrefix{
	public int getExpireSeconds();
	public String getPrefix();
}