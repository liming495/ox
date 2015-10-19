package com.ox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class GenerateNo {
    final AtomicInteger counter = new AtomicInteger(0);

    private static GenerateNo generateNo = null;

    public static GenerateNo getInstance() {
        if (generateNo == null) {
            synchronized (GenerateNo.class) {
                if (generateNo == null) {
                    generateNo = new GenerateNo();
                }
            }
        }
        return generateNo;
    }

    public synchronized String getNextNo(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        int ret = counter.incrementAndGet();
        if (ret > 10000){
            counter.set(0);
        }
        String retString = "0000" +  ret;
        return formatter.format(date) + retString.substring(retString.length() - 4,  retString.length());
    }

}
