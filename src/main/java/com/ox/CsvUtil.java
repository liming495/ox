package com.ox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class CsvUtil {
    public static final String CSV_COMMA = "!A!";// 两个感情号中间一个点 都是半角的 代表半角的逗号
    public static final String CSV_NEWLINE = "!B!";// 两个感情号中间一个问号 都是半角的 代表换行

    private final List<List<String>> csvData;
    private List<String> currentRowDara;

    public CsvUtil() {
        csvData = new ArrayList<List<String>>();
        currentRowDara = new ArrayList<String>();
    }

    public CsvUtil addItem(String data) {
        if (data == null) {
            data = "";
        }
        currentRowDara.add(data);
        return this;
    }

    public CsvUtil addItem(Object data) {
        if (data == null) {
            data = "";
        }
        currentRowDara.add(data.toString());
        return this;
    }

    public CsvUtil rowEnd() {
        csvData.add(currentRowDara);
        currentRowDara = new ArrayList<String>();
        return this;
    }

    public String tocsv() {
        return toCsv(csvData);
    }

    public static CsvUtil create() {
        return new CsvUtil();
    }

    public static String toCsv(String data) {
        return data.replaceAll(",", CSV_COMMA).replaceAll("\n", CSV_NEWLINE);

    }

    /**
     * 把数据转换为csv格式 <BR>
     * 如果遇见逗号 半角的 , 就转换为<.>
     *
     * @param data
     * @return
     */
    public static String toCsv(List<List<String>> data) {
        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < data.size(); j++) {
            for (int i = 0; i < data.get(j).size(); i++) {
                sb.append(data.get(j).get(i).replaceAll(",", CSV_COMMA).replaceAll("\n", CSV_NEWLINE));
                if (i < data.get(j).size() - 1) {
                    sb.append(",");
                }
            }

            if (j < data.size() - 1) {
                sb.append("\n");
            }

        }
        return sb.toString();
    }

    /**
     * 把csv格式还原为list
     *
     * @param data
     * @return
     */
    public static List<List<String>> fromCsv(String data) {
        StringBuilder sb = new StringBuilder();
        char[] charArray = data.toCharArray();
        List<List<String>> datas = new ArrayList<List<String>>();
        List<String> datalen = new ArrayList<String>();
        boolean isReplace = false;
        for (char c : charArray) {
            switch (c) {
                case ',':
                    if (isReplace) {
                        datalen.add(sb.toString().replaceAll(CSV_COMMA, ",").replaceAll(CSV_NEWLINE, "\n"));
                    } else {
                        datalen.add(sb.toString());
                    }
                    sb.setLength(0);
                    isReplace = false;
                    break;
                case '\n':
                    if (isReplace) {
                        datalen.add(sb.toString().replaceAll(CSV_COMMA, ",").replaceAll(CSV_NEWLINE, "\n"));
                    } else {
                        datalen.add(sb.toString());
                    }
                    isReplace = false;
                    sb.setLength(0);
                    datas.add(datalen);
                    datalen = new ArrayList<String>();
                    break;
                case '\r':
                    break;
                case '!':
                    sb.append(c);
                    isReplace = true;
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        // 处理结尾没有\n的最后一个字符串
        if (sb.length() > 0) {
            datalen.add(sb.toString().replaceAll(CSV_COMMA, ",").replaceAll(CSV_NEWLINE, "\n"));
        }
        if (!datalen.isEmpty()) {
            datas.add(datalen);
        }

        return datas;
    }

    /**
     * CVS单元格式转换
     *
     * @param data
     * @return
     */
    public static String fromCsvToString(String data) {
        List<List<String>> datas = fromCsv(data);
        if (datas.size() == 1) {
            return datas.get(0).get(0);
        }

        return null;
    }

    public static void main(String[] args) {
        String str = "aaaaa,aa,aaaa\naa";
        String csv = toCsv(str);

        String dest = fromCsvToString(csv);

        System.out.println(str);
        System.out.println(csv);
        System.out.println(dest);

    }

    /**
     * CVS行格式转换
     *
     * @param data
     * @return
     */
    public static List<String> fromCsvToList(String data) {
        List<List<String>> datas = fromCsv(data);
        if (datas.size() == 1) {
            return datas.get(0);
        }

        return null;
    }

    /**
     * 把csv格式还原为map kitindex指定map的key是第几个下标
     *
     * @param data
     *            csv数据
     * @param keyIndex
     *            map的key是第几个下标
     * @return 如果出错返回null
     */
    public static Map<String, List<String>> fromCsvToMap(String data, int keyIndex) {
        StringBuilder sb = new StringBuilder();
        char[] charArray = data.toCharArray();
        Map<String, List<String>> datas = new HashMap<String, List<String>>();
        List<String> datalen = new ArrayList<String>();
        boolean isReplace = false;

        for (char c : charArray) {
            switch (c) {
                case ',':
                    if (isReplace) {

                        datalen.add(sb.toString().replaceAll(CSV_COMMA, ",").replaceAll(CSV_NEWLINE, "\n"));
                    } else {
                        datalen.add(sb.toString());
                    }
                    sb.setLength(0);
                    isReplace = false;
                    break;
                case '\n':
                    if (isReplace) {
                        datalen.add(sb.toString().replaceAll(CSV_COMMA, ",").replaceAll(CSV_NEWLINE, "\n"));
                    } else {
                        datalen.add(sb.toString());
                    }
                    isReplace = false;
                    sb.setLength(0);
                    if (datalen.size() <= keyIndex) {
                        return null;
                    }
                    datas.put(datalen.get(keyIndex), datalen);
                    datalen = new ArrayList<String>();
                    break;
                case '\r':
                    break;
                case '!':
                    sb.append(c);
                    isReplace = true;
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }

        if (!datalen.isEmpty()) {
            if (datalen.size() <= keyIndex) {
                return null;
            }
            datas.put(datalen.get(keyIndex), datalen);
        }

        return datas;
    }
}
