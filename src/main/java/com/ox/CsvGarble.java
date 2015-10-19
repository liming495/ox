package com.ox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class CsvGarble {
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }

        String fileName = args[0];
        String tofile = null;
        String charset = "GBK";

        // String fileName = "D:\\共享目录\\测试环境\\trade_bill.csv";

        if (args.length > 1) {
            tofile = args[1];
        } else {
            tofile = fileName + ".r.csv";
        }

        if (args.length > 2) {
            charset = args[2];
        }
        garble(fileName, tofile, charset);
    }

    public static boolean garble(String filePath, String tofile, String charset) {
        if (!checkFile(filePath, false)) {
            return false;
        }

        deleteFile(tofile);

        if (!checkFile(tofile, true)) {
            return false;
        }

        int i = 0;
        boolean isnewLine = false;
        HashMap<String, String> data = new HashMap<String, String>();
        FileInputStream in = null;
        BufferedReader r = null;
        FileWriter writer = null;
        try {
            in = new FileInputStream(filePath);
            r = new BufferedReader(new InputStreamReader(in, charset));
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(tofile, true);

            String read;
            while ((read = r.readLine()) != null) {
                i++;
                // data.put(MD5(read), read);
                data.put(read, read);
                if (i > 100000) {
                    String[] array = data.keySet().toArray(new String[]{});
                    for (int j = 0; j < array.length; j++) {
                        String string = data.get(array[j]);
                        if (isnewLine) {
                            string = "\n" + string;
                        }
                        writer.write(string);

                        if (!isnewLine) {
                            isnewLine = true;
                        }
                    }

                    i = 0;
                    data.clear();
                }
            }

            String[] array = data.keySet().toArray(new String[]{});
            for (int j = 0; j < array.length; j++) {
                String string = data.get(array[j]);
                if (isnewLine) {
                    string = "\n" + string;
                }
                writer.write(string);

                if (!isnewLine) {
                    isnewLine = true;
                }
            }

        } catch (IOException ex) {
            return false;
        } finally {
            try {
                if (r != null)
                    r.close();

                if (writer != null)
                    writer.close();

                if (in != null)
                    in.close();
            } catch (IOException ex) {
            }
        }

        return true;
    }

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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

    public static boolean checkFile(String file, boolean create) {
        File f = new File(file);

        if (f.getParent() != null) {
            File fpath = new File(f.getParent());

            if (!fpath.exists()) {
                if (create) {
                    fpath.mkdirs();
                } else {
                    return false;
                }
            }
        }

        if (!f.exists()) {
            if (create) {
                try {
                    f.createNewFile();
                } catch (IOException e) {

                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    public static boolean deleteFile(String filePath) {
        if (!checkFile(filePath, false)) {
            return true;
        }
        File f = new File(filePath);
        return f.delete();

    }
}
