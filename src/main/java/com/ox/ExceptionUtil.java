package com.ox;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class ExceptionUtil {
    /**
     * 获取异常详细信息
     * @param e
     * @return
     */
    public static String getDetailMessage(Throwable e) {
        StringWriter writer = new StringWriter();
        try {
            e.printStackTrace(new PrintWriter(writer));
            return writer.getBuffer().toString();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception ex) {
                    e.printStackTrace();
                }
        }
    }
}
