package com.scyproject.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util  {
    private static final String salt = "1d2c3b4a";
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static String inputToForm(String input){
        String str =""+ salt.charAt(0) + salt.charAt(2) + input + salt.charAt(4) + salt.charAt(5);
        return(md5(str));
    }

    public static String formToDB(String form,String salt){
        String str = ""+salt.charAt(0)+salt.charAt(2)+form + salt.charAt(3) + salt.charAt(5);
        return md5(str);
    }

    public static String inputToDB(String input,String salt){
        String formPass = inputToForm(input);
        return formToDB( formPass,salt );
    }

//    public static void main(String[] args){
//        String i = "123456";
//        System.out.println(inputToForm(i));
//        System.out.println(formToDB( inputToForm(i) ,"123456"));
//        System.out.println( inputToDB(i,"123456") );
//    }
}
