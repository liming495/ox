package com.ox;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 一个快捷的配置文件,可以保存为xml或ini文件,作用根ini文件类似,感觉这样很方便, ini格式<br>
 * [类型] <br>
 * key = 值 <br>
 * key1 = 值1 <br>
 * [类型1]<br>
 * key = 值<br>
 * key1 = 值1<br>
 * <p/>
 * xml格式<br>
 * <config><br>
 * <a><br>
 * <property name="1" value = "1"/><br>
 * <property name="2" value = "sss"/><br>
 * <property name="3" value = "true"/><br>
 * </a><br>
 * <a1><br>
 * <property name="1" value = "1"/><br>
 * <property name="2" value = "sss"/><br>
 * <property name="3" value = "true"/><br>
 * </a1><br>
 * </config><br>
 * User: LiMing
 * Date: 2015/8/11
 */
public class Config extends HashMap<String, HashMap<String, String>> {
    private static final long serialVersionUID = -7271793397119152179L;
    private String path;
    private boolean isini = false;
    private final ArrayList<String> indexsArea = new ArrayList<String>();

    private final HashMap<String, ArrayList<String>> indexsKey = new HashMap<String, ArrayList<String>>();
    private String charset = BaseUtil.charset;

    public ArrayList<String> getIndexsArea() {
        return indexsArea;
    }

    public void setIsini(boolean isini) {
        this.isini = isini;
    }

    public boolean isIsini() {
        return isini;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getString(String area, String key, String defData, boolean nullSaveDefault) {
        if (!containsKey(area)) {
            if (nullSaveDefault) {
                setData(area, key, defData, true);
            }
            return defData;
        }

        HashMap<String, String> list = get(area);

        if (!list.containsKey(key)) {
            if (nullSaveDefault) {
                setData(area, key, defData, true);
            }
            return defData;
        }

        return list.get(key).trim();
    }

    public Boolean getBoolean(String area, String key, Boolean defData, boolean nullSaveDefault) {
        if (!containsKey(area)) {
            if (nullSaveDefault) {
                setData(area, key, defData, true);
            }
            return defData;
        }
        HashMap<String, String> list = get(area);
        if (!list.containsKey(key)) {
            if (nullSaveDefault) {
                setData(area, key, defData, true);
            }
            return defData;
        }

        return list.get(key).trim().toUpperCase().equals("TRUE");
    }

    public int getInt(String area, String key, int defData, boolean nullSaveDefault) {
        if (!containsKey(area)) {
            if (nullSaveDefault) {
                setData(area, key, defData, true);
            }
            return defData;
        }

        HashMap<String, String> list = get(area);

        if (!list.containsKey(key)) {
            if (nullSaveDefault) {
                setData(area, key, defData, true);
            }
            return defData;
        }

        return Integer.valueOf(list.get(key).trim());
    }

    public void setData(String area, String key, Object data, boolean immediateSave) {
        setData(area, key, data.toString(), immediateSave);
    }

    private void setIdx(String area, String key) {
        ArrayList<String> keyidx = indexsKey.get(area);

        if (keyidx == null) {
            keyidx = new ArrayList<String>();
            indexsKey.put(area, keyidx);
            indexsArea.add(area);
        }

        if (!keyidx.contains(key))
            keyidx.add(key);
    }

    public void setData(String area, String key, String data, boolean immediateSave) {
        setIdx(area, key);
        HashMap<String, String> value = null;
        if (containsKey(area)) {
            value = get(area);
        }

        if (value == null)
            value = new HashMap<String, String>();

        value.put(key, data);

        put(area, value);

        if (immediateSave) {
            save();
        }

    }

    public static Config fromFile(String fileName) {
        if (!FileUtil.checkFile(fileName, false)) {
            return null;
        }
        Document document = FileUtil.readXml(fileName);
        return fromXml(document, fileName);
    }

    public static Config fromIni(String fileName, boolean create, String charset) {
        if (!FileUtil.checkFile(fileName, create)) {
            return null;
        }

        String data;
        Config cfg = new Config();
        cfg.isini = true;
        cfg.path = fileName;
        cfg.charset = charset;
        String areaName = null, key = null, value = null;

        try {

            FileInputStream inputStream = new FileInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));
            int j;
            boolean procUtf_8 = false;
            while ((data = bufferedReader.readLine()) != null) {
                data = data.trim();
                if (data.length() < 1) {
                    continue;
                }

                // utf-8第一行
                if (!procUtf_8) {
                    procUtf_8 = true;
                    if (data.endsWith("]") && (!data.startsWith("["))) {
                        int top1 = data.lastIndexOf('[');
                        if (top1 > 0) {
                            data = data.substring(top1);
                        }
                    }
                }

                if (data.startsWith("[") && data.endsWith("]")) {
                    areaName = data.substring(1, data.length() - 1).trim();
                } else if ((j = data.indexOf('=')) > 0) {
                    if (areaName != null) {
                        key = data.substring(0, j);
                        value = data.substring(j + 1);
                        cfg.setData(areaName, key, value, false);
                    }
                }
            }

            bufferedReader.close();

        } catch (IOException e1) {
            return null;
        }

        return cfg;
    }

    public static Config fromXml(Document document, String fileName) {
        Config cfg = new Config();
        cfg.path = fileName;
        cfg.isini = false;
        String areaName = null, key = null, value = null;
        NodeList childNodes = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node areas = childNodes.item(i);
            if (areas.getNodeName().equals("#text"))
                continue;
            areaName = areas.getNodeName();
            NodeList items = areas.getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                NamedNodeMap attributes = item.getAttributes();
                if (attributes != null && attributes.getLength() > 1) {
                    key = attributes.item(0).getNodeValue();
                    value = attributes.item(1).getNodeValue();
                    cfg.setData(areaName, key, value, false);
                }
            }

        }
        return cfg;
    }

    public void save() {
        String data;
        if (this.isini) {
            data = toIni();
        } else {
            data = toXml();
        }
        FileUtil.writeFile(path, data, true, charset);

    }

    private String toIni() {
        StringBuilder sb = new StringBuilder();
        for (String s : indexsArea) {
            sb.append("[");
            sb.append(s);
            sb.append("]\r\n");
            HashMap<String, String> items = get(s);
            if (!(items == null || items.isEmpty())) {
                ArrayList<String> keys = indexsKey.get(s);
                for (String k : keys) {
                    sb.append(k);
                    sb.append("=");
                    sb.append(items.get(k));
                    sb.append("\r\n");
                }
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public String toXml() {
        StringBuilder sb = new StringBuilder("<config>\n");
        for (String s : indexsArea) {
            sb.append("	<");
            sb.append(s);
            sb.append(">\r\n");
            HashMap<String, String> items = get(s);
            if (!(items == null || items.isEmpty())) {
                ArrayList<String> keys = indexsKey.get(s);
                for (String k : keys) {
                    sb.append("		<property name=\"");
                    sb.append(k);
                    sb.append("\" value = \"");
                    sb.append(items.get(k));
                    sb.append("\"/>\r\n");
                }
            }
            sb.append("	</");
            sb.append(s);
            sb.append(">\r\n");
        }
        sb.append("</config>\r\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        String data;
        if (this.isini) {
            data = toIni();
        } else {
            data = toXml();
        }
        return data;

    }

    public static void main(String[] args) {
        Config cfg = Config.fromIni("c:\\DevConfig.ini", true, null);

        cfg.setData("a", "1", 1, false);
        cfg.setData("a", "2", "sss", false);
        cfg.setData("a", "3", true, false);

        cfg.setData("a1", "1", 1, false);
        cfg.setData("a1", "2", "sss", false);
        cfg.setData("a1", "3", true, true);

        System.out.println(cfg.toXml());
        System.out.println(cfg);

    }
}
