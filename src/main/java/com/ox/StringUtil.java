package com.ox;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class StringUtil {
    /**
     * 简单将数据拼装
     *
     * @param data
     * @return
     */
    public static String append(String... data) {
        if (null == data || data.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (String str : data) {
            buffer.append(str);
        }
        return buffer.toString();
    }

    /**
     * 合并字符数组，null将跳过不合并
     *
     * @param data
     * @return
     */
    public static String appendSkipNull(String... data) {
        if (data.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (String str : data) {
            if (null == str) {
                continue;
            }
            buffer.append(str);
        }
        return buffer.toString();
    }

    /**
     * ISO-8859-1 转 UTF-8
     *
     * @param value
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String iso2utf(String value) throws UnsupportedEncodingException {
        if (value == null) {
            return null;
        }
        return new String(value.getBytes("ISO-8859-1"), "UTF-8");
    }

    /**
     * 去掉小数点后面的0
     *
     * @param str 原始字符串
     * @return
     */
    public static String trim(String str) {
        if (str == null)
            return null;
        if (str.contains(".") && str.charAt(str.length() - 1) == '0') {
            return trim(str.substring(0, str.length() - 1));
        } else {
            return str.charAt(str.length() - 1) == '.' ? str.substring(0, str.length() - 1) : str;
        }
    }

    /**
     * 判断手机号码格式
     *
     * @param value
     * @return
     */
    public static boolean isPhone(String value) {
        if (value == null || value.trim().equals("") || value.length() != 11)
            return false;
        Pattern p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    /**
     * 判断手机验证码格式
     *
     * @param value
     * @return
     */
    public static boolean isPhoneCode(String value) {
        if (value == null || value.trim().equals("") || value.length() != 6)
            return false;
        Pattern p = Pattern.compile("\\d{6}");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    /**
     * 判断邮箱格式
     *
     * @param value
     * @return
     */
    public static boolean isEmail(String value) {
        if (value == null || value.trim().equals(""))
            return false;
        Pattern p = Pattern.compile("^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    /**
     * 判断密码格式
     *
     * @param value
     * @return
     */
    public static boolean isPass(String value) {
        if (value == null || value.trim().equals(""))
            return false;
        Pattern p = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\\\D+$).{8,16}$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    /**
     * 获得随机数
     *
     * @param i
     * @return
     */
    public static long getRandomCode(Integer i) {
        if (i == null || i.equals(0) || i.equals(1))
            i = 6;
        String str1 = "8";
        String str2 = "1";
        for (int j = 0; j < (i - 1); j++) {
            str1 += "9";
            str2 += "0";
        }
        long x = new Random().nextInt(Integer.valueOf(str1));
        x = x + Integer.valueOf(str2);
        return x;
    }

    /**
     * 正整数
     *
     * @param value
     * @return
     */
    public static boolean isInteger(String value) {
        if (value == null || value.trim().equals("") || value.length() > 10)
            return false;
        Pattern p = Pattern.compile("\\d*");
        Matcher m = p.matcher(value);
        return m.matches();
    }


    public static boolean isLong(String value) {
        if (value == null || value.trim().equals("") || value.length() > 18)
            return false;
        Pattern p = Pattern.compile("\\d*");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public static boolean isDouble(String value) {
        if (value == null || value.trim().equals("") || value.length() > 18)
            return false;
        Pattern pattern = Pattern.compile("^[-\\+]?\\d+(\\.\\d+)?$");
        return pattern.matcher(value).matches();
    }

    /**
     * 是否是全球微码
     *
     * @param value
     * @return
     */
    public static boolean isUUID(String value) {
        if (value == null || value.trim().equals("") || value.length() != 36)
            return false;
        Pattern pattern = Pattern.compile("^[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}$");
        return pattern.matcher(value).matches();
    }
}
