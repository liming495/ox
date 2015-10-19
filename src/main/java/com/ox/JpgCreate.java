package com.ox;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class JpgCreate {
    static BufferedImage image;

    @SuppressWarnings("restriction")
    static void createImage(String fileLocation) {
        try {
            FileOutputStream fos = new FileOutputStream(fileLocation);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
            encoder.encode(image);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void graphicsGeneration(String data, Color backupColor, Color fontcColor, int width, int height, int data_x, int data_y, int fontsize, String outfile) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(backupColor);
        Font font = new Font(graphics.getFont().getName(), graphics.getFont().getStyle(), fontsize);
        graphics.setFont(font);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(fontcColor);
        graphics.drawString(data, data_x, data_y);
        graphics.dispose();
        createImage(outfile);
    }

    public static void main(String[] args) {
        //JpgCreate.graphicsGeneration("name1", Color.black, Color.white, 1280, 720, 100, 300, 90, "F:/1.jpg");
        //JpgCreate.graphicsGeneration("name2", Color.black, Color.white, 800, 600, 100, 300, 90, "c:/2.jpg");
        //JpgCreate.graphicsGeneration("name3", Color.black, Color.white, 800, 600, 100, 300, 90, "c:/3.jpg");
        try {
            JpgCreate.resizeImage(new FileInputStream("F:/1.jpg"), new FileOutputStream("F:/2.jpg"), 512, "jpg");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 改变图片的大小到宽为size，然后高随着宽等比例变化
     * @param is 上传的图片的输入流
     * @param os 改变了图片的大小后，把图片的流输出到目标OutputStream
     * @param size 新图片的宽
     * @param format 新图片的格式
     * @throws IOException
     */
    public static void resizeImage(InputStream is, OutputStream os, int size, String format) throws IOException {
        BufferedImage prevImage = ImageIO.read(is);
        double width = prevImage.getWidth();
        double height = prevImage.getHeight();
        double percent = size/width;
        int newWidth = (int)(width * percent);
        int newHeight = (int)(height * percent);
        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
        ImageIO.write(image, format, os);
        os.flush();
        is.close();
        os.close();
    }
}
