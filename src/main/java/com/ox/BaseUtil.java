package com.ox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class BaseUtil {
    public static String charset = "utf-8";

    public static boolean isValid(String data) {
        return !(data == null || data.isEmpty());
    }

    /**
     * 压缩数据
     *
     * @param data
     * @param charset
     * @return
     * @throws java.io.IOException
     */
    public static byte[] gzip(String data, String charset) throws IOException {

        if (data == null || data.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(data.getBytes(charset));
        gzip.close();
        return out.toByteArray();

    }

    /**
     * 解压数据
     *
     * @param str
     * @param charset
     * @return
     * @throws IOException
     */
    public static String unGzip(byte[] str, String charset) throws IOException {
        if (str == null || str.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        out.flush();
        String body = out.toString(charset);
        gunzip.close();
        out.close();
        in.close();

        return body;
    }

    public static void main(String[] args) throws IOException {
        String charset = "GBK";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(UUID.randomUUID().toString());
            sb.append("\n");
        }

        long t1 = System.currentTimeMillis();
        byte[] s = gzip(sb.toString(), charset);
        System.err.println("压缩用时: = " + (System.currentTimeMillis() - t1));
        // System.out.println(s.getBytes().length + "\r\n" + s);

        t1 = System.currentTimeMillis();
        String unGzip = unGzip(s, charset);
        System.err.println("解压缩用时: = " + (System.currentTimeMillis() - t1));
        // System.out.println(unGzip.getBytes().length + "\r\n" + unGzip);

        System.out.println(unGzip.getBytes().length + " 压缩到 " + s.length);
        System.out.println("gzip 压缩率= " + s.length * 1.00
                / unGzip.getBytes().length * 100.00 + "%");

    }

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断参数是否为空或空字符串
     *
     * @param strs
     * @return
     */
    public static boolean isnull(Object... strs) {
        for (Object str : strs) {
            if (str == null)
                return true;
            if (str instanceof String && "".equals(str.toString().trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数字型参数是否为空
     *
     * @param strs
     * @return
     */
    public static boolean isNumberNull(Object... strs) {
        for (Object str : strs) {
            if (str == null)
                return true;
            if ("0".equals(str.toString().trim())) {
                return true;
            }
        }
        return false;
    }
}
