package com.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 **/
public class ZhengZe {

    public static void main(String[] args) {
        String str = "123.168.1.1";
        String regEx = "((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})";
        String regEx2 = "((22[4-9]|23[0-9])(\\.(([0-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5]))))){3})|(255.255.255.255)";
        Pattern pattern = Pattern.compile(regEx);
        Pattern pattern2 = Pattern.compile(regEx2);
        Matcher matcher = pattern.matcher(str);
        Matcher matcher2 = pattern2.matcher(str);
        System.out.println(matcher.matches());
        System.out.println(matcher2.matches());

    }


}
