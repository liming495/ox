package com.ox;

import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class FileUtil {
    public final static String Charset = "utf-8";

    // [start] ResourceBundle 读取

    /**
     * 获得资源定义文件
     *
     * @return
     */
    public static ResourceBundle getResourceBundle(Class clazz) {
        return getResourceBundle(null, clazz);
    }

    public static ResourceBundle getResourceBundle(Class clazz, Locale locale) {
        return getResourceBundle(null, clazz, locale);
    }

    public static ResourceBundle getResourceBundle(String rootPath, Class clazz) {
        return getResourceBundle(null, clazz, null);
    }

    /**
     * 获得资源定义文件
     *
     * @param rootPath
     *            根目录，不输入就是当前包的目录
     * @param clazz
     *            类
     * @return
     */
    public static ResourceBundle getResourceBundle(String rootPath, Class clazz, Locale locale) {
        ResourceBundle bundle = null;

        StringBuffer sb = new StringBuffer("");
        if (BaseUtil.isValid(rootPath)) {
            sb.append(rootPath);
            if (!(rootPath.trim().endsWith("/") || rootPath.trim().endsWith("\\"))) {
                sb.append("/");
            }
        }

        sb.append(clazz.getPackage().getName().replace(".", "/"));
        sb.append("/resources/" + clazz.getSimpleName());
        try {
            if (locale == null) {
                bundle = ResourceBundle.getBundle(sb.toString());
            } else {
                bundle = ResourceBundle.getBundle(sb.toString(), locale);
            }
        } catch (Exception ex) {
        }
        return bundle;
    }

    /**
     * 文件重命名
     *
     * @param oldName
     * @param newName
     * @return
     */
    public static boolean fileReName(String oldName, String newName) {
        File file = new File(oldName);
        File newfile = new File(newName);
        if (file.isFile() && (!newfile.exists())) {
            file.renameTo(newfile);
            return true;
        }
        return false;
    }

    // [end]

    // [start] Properties 存取

    /**
     * 查找并判断的文件的绝对路径
     *
     * @param clazz
     * @param fileName
     * @return
     */
    public static String getAbsoluteFilePathName(Class clazz, String fileName) {
        if (clazz == null || fileName == null || fileName.isEmpty())
            return null;
        StringBuilder sb = new StringBuilder("");
        String className = clazz.getName();
        for (int i = 0; i < className.length(); i++) {
            if (className.charAt(i) == '.') {
                sb.append("/");
            } else {
                sb.append(className.charAt(i));
            }
        }
        sb.insert(0, '/');
        sb.append(".class");
        className = sb.toString();
        String path;
        try {
            path = URLDecoder.decode(clazz.getResource(className).getPath(), FileUtil.Charset);
        } catch (UnsupportedEncodingException e) {
            path = clazz.getResource(className).getPath();
        }
        path = path.replace("file:", "");
        File file = new File(path);
        String fullPathName = null;
        while (file != null) {
            if (file.exists()) {
                String s = null;
                if (file.getAbsolutePath().endsWith(String.valueOf(File.separatorChar))) {
                    s = file.getAbsolutePath() + fileName;
                } else {
                    s = file.getAbsolutePath() + File.separatorChar + fileName;
                }
                if (new File(s).exists()) {
                    fullPathName = s;
                    break;
                }
            }
            file = file.getParentFile();
        }
        return fullPathName;
    }

    /**
     * 获取本地配置文件
     *
     * @param fileName
     * @return
     */
    public static Properties getProperties(Class clazz, String fileName) {
        String fullPathName = FileUtil.getAbsoluteFilePathName(clazz, fileName);
        if (fullPathName != null) {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(fullPathName));
                return prop;
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * 获取本地配置文件
     *
     * @param fileName
     * @return
     */
    public static Properties getProperties(String fileName) {
        Properties prop = new Properties();
        checkFile(fileName, true);
        try {
            prop.load(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return prop;
    }

    /*
     * 保存配置文件
     */
    public static boolean saveProperties(Properties prop, String fileName, String remark) {
        OutputStream fos;
        try {
            fos = new FileOutputStream(fileName);
            prop.store(fos, remark);
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    // [end]

    // [start] 通用文件存取

    /**
     * 获取本应用的路径
     *
     * @return
     */
    public static String getAppPath() {
        String p = FileUtil.class.getResource("/").getPath();
        return p;
    }

    /**
     * 获取文件的类型
     *
     * @param filename
     * @return
     */
    public static String getFileType(String filename) {
        int i = filename.indexOf('.');
        if (i >= 0) {
            return filename.substring(i + 1);
        }
        return "";
    }

    /**
     * 获得临时目录
     *
     * @return
     */
    public static String getTmpPath() {
        Properties properties = System.getProperties();
        return (String) properties.get("java.io.tmpdir");
    }

    /**
     * 将字节数组保存到一个临时文件中
     *
     * @param bytes
     * @param fileExtension
     * @return
     * @throws IOException
     */
    public static String saveBytesBufferToTmpdir(byte[] bytes, String fileExtension) throws IOException {
        return saveBytesBufferToTmpdir(ByteBuffer.wrap(bytes), fileExtension);
    }

    /**
     * 将ByteBuffer保存到一个文件中
     *
     * @param buf
     * @param fileExtension
     * @return
     * @throws IOException
     */
    public static String saveBytesBufferToTmpdir(ByteBuffer buf, String fileExtension) throws IOException {
        File tmpPath = new File(getTmpPath());
        File file = File.createTempFile("~r~", "." + fileExtension, tmpPath);
        return saveBytesBufferToFile(buf, file);
    }

    /**
     * 将ByteBuffer保存到一个文件中
     *
     * @param buf
     * @param filename
     * @return
     * @throws IOException
     */
    public static String saveBytesBufferToFile(ByteBuffer buf, String filename) throws IOException {
        return saveBytesBufferToFile(buf, new File(filename));
    }

    public static String saveBytesBufferToFile(ByteBuffer buf, File file) throws IOException {
        boolean append = false;
        @SuppressWarnings("resource")
        FileChannel outChannel = new FileOutputStream(file, append).getChannel();
        buf.rewind();
        outChannel.write(buf);
        outChannel.close();
        return file.getAbsolutePath();
    }

    /**
     * 将一个文件读出并保存在一个ByteBuffer中
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    public static ByteBuffer readByteBufferFromFile(String filepath) throws IOException {
        File file = new File(filepath);
        FileChannel inChannel;
        inChannel = new FileInputStream(file).getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) file.length());
        inChannel.read(buf);
        inChannel.close();
        buf.rewind();
        return buf;
    }

    /*
     * 校验文件夹没有的话创建
     */
    public static boolean checkFolder(String Folder, boolean create) {
        File f = new File(Folder);
        return checkFolder(f, create);
    }

    /*
     * 校验文件夹没有的话创建
     */
    public static boolean checkFolder(File folder, boolean create) {
        if (folder.getParent() != null) {
            File fpath = new File(folder.getParent());

            if (!fpath.exists()) {
                if (create) {
                    fpath.mkdirs();
                } else {
                    return false;
                }
            }
        }
        if (!folder.exists()) {
            if (create) {
                folder.mkdirs();
            } else {
                return false;
            }
        }
        return true;
    }

    /*
     * 校验文件没有的话创建
     */
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

    /**
     * 文件拷贝
     *
     * @param oldFilePath
     * @param newFilePath
     * @param isCover
     *            是否覆盖
     * @return
     */
    public static boolean fileCopy(String oldFilePath, String newFilePath, boolean isCover) {
        if (!checkFile(oldFilePath, false)) {
            return false;
        }
        if (checkFile(newFilePath, true) && (!isCover)) {
            return false;
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        byte[] buff = new byte[4096];
        try {
            in = new FileInputStream(oldFilePath);
            out = new FileOutputStream(newFilePath);

            int len = 0;
            while ((len = in.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return true;
    }

    /*
     * 快速写覆盖文件
     */
    public static boolean writeFile(String filePath, InputStream in, boolean createFile, String Charset) {
        if (!checkFile(filePath, createFile)) {
            return false;
        }
        try {
            OutputStream writerStream = new java.io.FileOutputStream(filePath);
            int i;
            while ((i = in.read()) > -1) {
                writerStream.write(i);
            }
            writerStream.close();
        } catch (IOException e1) {
            return false;
        }
        return true;
    }

    /*
     * 快速写覆盖文件
     */
    public static boolean writeFile(String filePath, String data, boolean createFile) {
        return writeFile(filePath, data, createFile, FileUtil.Charset);
    }

    /*
     * 快速写覆盖文件
     */
    public static boolean writeFile(String filePath, String data, boolean createFile, String Charset) {
        if (!checkFile(filePath, createFile)) {
            return false;
        }
        try {
            OutputStream writerStream = new java.io.FileOutputStream(filePath);
            BufferedWriter bufferedWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(writerStream, Charset));
            bufferedWriter.write(data);
            bufferedWriter.close();

        } catch (IOException e1) {
            return false;
        }
        return true;
    }

    /*
     * 快速追加文件
     */
    public static boolean appendFile(String filePath, String data, boolean createFile, String Charset) {
        if (!checkFile(filePath, createFile)) {
            return false;
        }
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(filePath, true);
            writer.write(data);
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
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

    public static String readFile(String filePath) {
        if (!checkFile(filePath, false)) {
            return null;
        }
        char[] buffer = new char[1024];
        StringBuilder stringBuffer = new StringBuilder();
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.Charset));
            int read;
            while ((read = bufferedReader.read(buffer)) > 0) {
                stringBuffer.append(buffer, 0, read);
            }
            bufferedReader.close();
            return stringBuffer.toString();
        } catch (IOException e1) {
            return null;
        }
    }

    /*
     * 快速读文件
     */
    public static ArrayList<String> readTextFile(String filePath, String charset) {
        if (!checkFile(filePath, false)) {
            return null;
        }
        ArrayList<String> data = new ArrayList<String>();
        FileInputStream in = null;
        BufferedReader r = null;
        try {
            in = new FileInputStream(filePath);
            r = new BufferedReader(new InputStreamReader(in, charset));
            String read;
            while ((read = r.readLine()) != null) {
                data.add(read);
            }
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                if (r != null)
                    r.close();
                if (in != null)
                    in.close();
            } catch (IOException ex) {
            }
        }
        return data;
    }

    /*
     * 选择文件
     */
    public static String getSelectFile(Component parent, String FileTitle, String FileType, boolean bfile, boolean pathOld, String badge) {
        JFileChooser chooser = new JFileChooser();
        File filepathFile = null;
        Properties properties = null;
        if (pathOld) {
            if (badge == null || badge.equals("")) {
                badge = parent.getClass().getName();
            }
            properties = getProperties(System.getProperty("user.dir") + "/XFile.temp");
            String path = properties.getProperty(badge, System.getProperty("user.dir"));
            if (path == null) {
                path = System.getProperty("user.dir");
            }
            filepathFile = new File(path);
        } else {
            filepathFile = new File(System.getProperty("user.dir"));
        }
        chooser.setCurrentDirectory(filepathFile);
        chooser.setDialogTitle(FileTitle);
        if (!bfile) {
            chooser.setFileSelectionMode(1);
        } else {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(FileType, FileType);
            chooser.setFileFilter(filter);
        }
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (pathOld) {
                properties.setProperty(badge, chooser.getSelectedFile().getParent());
                saveProperties(properties, System.getProperty("user.dir") + "/XFile.temp", "读文件的临时文件，可以删除，删除后选文件就从系统目录下选择");
            }
            return chooser.getSelectedFile().getPath();
        }
        return null;
    }

    // [end]

    // [start] XML文件存取 管理
    public static boolean writeXmlCharset(String fileName, String xml) {
        return writeXml(fileName, "<?xml version=\"1.0\" encoding=\"" + FileUtil.Charset + "\"?>" + xml);
    }

    /**
     * 保存Xml文件到本地
     *
     * @param fileName
     * @param xml
     * @return
     */
    public static boolean writeXml(String fileName, String xml) {
        if (!checkFile(fileName, true)) {
            return false;
        }
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(fileName);
            outStream.write(xml.getBytes(FileUtil.Charset));
        } catch (Exception ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, null, ex);
            return false;
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (Exception ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, null, ex);
                return false;
            }
        }
        return true;
    }

    /**
     * 读取资源文件夹中的XML
     *
     * @param clazz
     * @return
     */
    public static Document readXml(Class clazz) {
        java.io.InputStream stream = clazz.getResourceAsStream(clazz.getSimpleName() + ".xml");
        if (stream == null)
            return null;
        return readXml(stream, true);
    }

    /**
     * 读取资源文件夹中的XML
     *
     * @param clazz
     * @return
     */
    public static Document readXml(Class clazz, String fileName) {
        java.io.InputStream stream = clazz.getResourceAsStream(fileName + ".xml");
        if (stream == null)
            return null;
        return readXml(stream, true);
    }

    /**
     * 读取本地 xml 文件并转换为Document
     */
    public static Document readXml(String fileName) {
        if (!checkFile(fileName, false)) {
            return null;
        }
        java.io.InputStream stream;
        try {
            stream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }
        return readXml(stream, true);
    }

    private static Document readXml(java.io.InputStream stream, boolean endclose) {
        Document dataDocument = null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            dataDocument = documentBuilder.parse(stream);
        } catch (Exception e) {
            return null;
        } finally {
            if (endclose) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
        return dataDocument;
    }

    // [end]

    // [start] 序列化保存文件

    /**
     * 读取序列化到本地文件的一个对象
     */
    public static Object readObject(String fileName) {
        ObjectInputStream in = null;
        File opml = new File(fileName);
        if (!opml.exists()) {
            return null;
        }
        Object object = null;
        try {
            in = new ObjectInputStream(new FileInputStream(fileName));
            object = in.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, null, ex);
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, null, ex);
            }
        }
        return object;
    }

    /**
     * 将对象序列化到本地一个文件
     *
     * @param fileName
     * @param object
     * @return
     */
    public static boolean wtiteObject(String fileName, Object object) {
        if (!checkFile(fileName, true))
            return false;
        FileOutputStream fs = null;
        ObjectOutputStream os = null;
        try {
            fs = new FileOutputStream(fileName);
            os = new ObjectOutputStream(fs);
            os.writeObject(object);
            os.close();
            fs.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, null, ex);
            return false;
        } finally {
            try {
                fs.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, null, ex);
                return false;
            }
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, null, ex);
                return false;
            }
        }
        return true;
    }

    /**
     * 对象序列化
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static byte[] getBytes(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof String) {
            try {
                return ((String) obj).getBytes(Charset);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        java.io.ByteArrayOutputStream a = new java.io.ByteArrayOutputStream();
        try {
            ObjectOutputStream o = new ObjectOutputStream(a);
            o.writeObject(obj);
            o.flush();
            byte[] bytes = a.toByteArray();
            o.close();
            a.close();
            return bytes;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 对出序列化的对象
     *
     * @param bytes
     * @return
     */
    public static Object getObject(byte[] bytes) {
        if (bytes == null)
            return null;
        java.io.ByteArrayInputStream a = new java.io.ByteArrayInputStream(bytes);
        try {
            ObjectInputStream o = new ObjectInputStream(a);
            Object obj = o.readObject();
            o.close();
            a.close();
            return obj;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                a.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 把不是csv格式的文件转换为csv格式的
     *
     * @param oldPath 旧文件
     * @param newPath 新文件
     * @param itemKey 列分割符号
     * @param lineKey 行分割符号
     * @param egisKey 逗号转换方式
     * @param isTrim 是否去掉前后空格
     * @param charset 字符集
     */
    public static int ToCsvFile(String oldPath, String newPath, String itemKey, String lineKey, char egisKey, boolean isTrim, String charset) {
        if (!checkFile(oldPath, false)) {
            return -1;
        }
        char[] itemKeycc = itemKey.toCharArray();
        char[] lineKeycc = lineKey.toCharArray();
        BufferedReader bufferedReader = null;
        FileInputStream inputStream = null;

        OutputStream writerStream = null;
        BufferedWriter bufferedWriter = null;

        try {
            inputStream = new FileInputStream(oldPath);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));

            writerStream = new java.io.FileOutputStream(newPath);
            bufferedWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(writerStream, Charset));

            int read;
            char buff[] = new char[1024];
            StringBuffer sb = new StringBuffer();
            ArrayList<String> len = new ArrayList<String>();
            int readoff = 0;
            while ((read = bufferedReader.read(buff, readoff, buff.length - readoff)) > 0) {
                readoff = 0;
                for (int i = 0; i < read; i++) {
                    if (buff[i] == ',') {
                        sb.append(egisKey);
                    } else if (buff[i] == lineKeycc[0]) {
                        switch (checkArray(lineKeycc, buff, i, read)) {
                            case -1:
                                readoff = read - i;
                                break;
                            case 0:
                                sb.append(buff[i]);
                                break;
                            case 1:
                                if (isTrim) {
                                    len.add(sb.toString().trim());
                                } else {
                                    len.add(sb.toString());
                                }
                                String d = listToString(len, ",");
                                // 写文件
                                bufferedWriter.write(d);
                                bufferedWriter.write("\n");
                                len.clear();
                                sb.setLength(0);
                                i = i + lineKeycc.length - 1;
                                break;
                        }
                    } else if (buff[i] == itemKeycc[0]) {
                        switch (checkArray(itemKeycc, buff, i, read)) {
                            case -1:
                                readoff = read - i;
                                break;
                            case 0:
                                sb.append(buff[i]);
                                break;
                            case 1:
                                if (isTrim) {
                                    len.add(sb.toString().trim());
                                } else {
                                    len.add(sb.toString());
                                }
                                sb.setLength(0);
                                i = i + itemKeycc.length - 1;
                                break;
                        }
                    } else {
                        sb.append(buff[i]);
                    }

                    if (readoff > 0) {
                        for (int j = 0; j < readoff; j++) {
                            buff[j] = buff[i + j];
                        }
                        break;
                    }
                }
            }

            if (readoff > 0) {
                for (int j = 0; j < readoff; j++) {
                    sb.append(buff[j]);
                }
            }

            if (isTrim) {
                len.add(sb.toString().trim());
            } else {
                len.add(sb.toString());
            }
            String d = listToString(len, ",");
            bufferedWriter.write(d);
            bufferedWriter.write("\n");
            len.clear();
            sb.setLength(0);

        } catch (IOException e) {
            return -1;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
            }
        }
        return 0;
    }

    /**
     * 把list转换为string
     *
     * @param data
     * @param itemKey
     * @return
     */
    public static String listToString(ArrayList<String> data, String itemKey) {
        StringBuffer sb = new StringBuffer();
        sb.append(data.get(0));
        for (int i = 1; i < data.size(); i++) {
            sb.append(itemKey).append(data.get(i));
        }
        return sb.toString();
    }

    /**
     * 对比前一个数组是不是跟后一个的第checkidx个相等
     *
     * @param base
     *            原始的数组
     * @param check
     *            要对比的数组
     * @param checkidx
     *            对比数组的下标
     * @param checklen
     *            对比数组的上标
     * @return -1 长度不够<br>
     *         0 不包含<br>
     *         1包含<br>
     */
    private static int checkArray(char[] base, char[] check, int checkidx, int checklen) {
        if (checklen > checkidx + base.length) {

            for (int j = 0; j < base.length; j++) {
                if (check[checkidx + j] != base[j]) {
                    return 0;

                }
            }
        } else {
            return -1;
        }

        return 1;
    }

    /**
     * 将一个对象转化为字符串
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    // [end]

    public static void main(String data[]) {

        String oldPath = "c:\\1.txt";
        String newPath = "c:\\1.txt.csv";
        String itemKey = "|!|";
        String lineKey = "\n";
        ToCsvFile(oldPath, newPath, itemKey, lineKey, '.', true, "UTF-8");

    }
}
