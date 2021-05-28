package com.scyproject.redis;

public class UserKey extends BasePrefix{

	public UserKey(String prefix, int expireSeconds) {
		super(prefix, expireSeconds);
	}

	public UserKey(String prefix) {
		super(prefix);
	}

	static public UserKey getById =new UserKey("id",30);
	static public UserKey getByName =new UserKey("name",30);
}