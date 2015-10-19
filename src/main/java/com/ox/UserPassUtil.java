package com.ox;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class UserPassUtil {
    private static final String PASS_KEY = "you key";

    /**
     * 加密
     *
     * @param pass md5后密码
     * @param time 时间
     * @return
     */
    private static String encrypt(String pass, long time) {
        return BaseUtil.MD5(time + pass + PASS_KEY);
    }

    /**
     * @param pass      密码
     * @param time      时间
     * @param isPassMd5 密码是否已加md5
     * @return
     */
    public static String encrypt(String pass, long time, boolean isPassMd5) {
        if (!isPassMd5){
            pass = BaseUtil.MD5(pass);
        }
        return encrypt(pass, time);
    }
}
