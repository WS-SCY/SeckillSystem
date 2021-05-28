package com.scyproject.redis;

abstract class BasePrefix implements KeyPrefix{
	public String prefix;
	public int expireSeconds;

	public BasePrefix(String prefix,int expireSeconds){
		this.prefix = prefix;
		this.expireSeconds = expireSeconds;
	}
	public BasePrefix(String prefix){
		this(prefix,0);
	}
	public int getExpireSeconds(){
		return expireSeconds;
	}
	public String getPrefix(){
		return this.getClass().getSimpleName()+":"+prefix;
	}

}