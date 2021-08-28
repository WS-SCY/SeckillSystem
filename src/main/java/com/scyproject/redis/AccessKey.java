package com.scyproject.redis;

public class AccessKey extends BasePrefix{

    private AccessKey( int expireSeconds, String prefix) {
        super(prefix,expireSeconds);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }

}