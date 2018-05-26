package com.simple.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import com.simple.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random(); 

    /**
     * 处理缩略图，并返回新生成图片的相对值路径
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr  =targetAddr +realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
            .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "watermark.png")),0.25f)
            .outputQuality(0.8f).toFile(dest);
        }catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work/o2o/xxx.jpg,
     * 那么 home work o2o 这三个文件夹都得自动创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        // TODO Auto-generated method stub
        String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }


    /**
     * 生成随机文件名，当前年月日小时分钟秒+五位随机数
     */
    public static String getRandomFileName() {
        //获取随机的五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr+rannum;
    }
    /**
     * 获取输入文件流的扩展名
     * @param args
     * @throws IOException
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
/*    public static void main(String[] args) throws IOException {

        Thumbnails.of(new File("/Users/mac/Downloads/luoto.png")).size(200, 200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/jingyu.png")), 0.25f)
                .outputQuality(0.8f).toFile("/Users/mac/Downloads/luotonew.png");
    }*/
    /**
     * storePath是文件的路径还是目录的路径,
     * 如果storePath是文件路径则删除该文件,
     * 如果storePath是目录路径则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
    	//获取路径
    	File fileOrPath = new File(PathUtil.getImgBasePath()+ storePath);
    	//判断路径是否存在
    	if(fileOrPath.exists()){
    		//判断是否是目录路径
    		if(fileOrPath.isDirectory()){
    			//遍历取出删除
    			File files[] = fileOrPath.listFiles();
    			for(int i=0; i<files.length; i++){
    				files[i].delete();
    			}
    		}
    		//不是目录直接删除，也删除目录
    		fileOrPath.delete();
    	}
    }

    /**
     * 处理详情图，并返回新生成图片的相对值路径
     * @param productImgHolder
     * @param dest
     * @return
     */
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		//获取不重复的随机名
		String realFileName = getRandomFileName();
		//获取文件的扩展名
        String extension = getFileExtension(thumbnail.getImageName());
        //如果目录不存在则自动创建
        makeDirPath(targetAddr);
        //获取文件存储的相对路径(带文件名)
        String relativeAddr = targetAddr +realFileName + extension;
        //获取文件要保存到的目标路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        //调用Thumbnails生成带有水印的图片
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
            .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "watermark.png")),0.25f)
            .outputQuality(0.9f).toFile(dest);
        }catch (IOException e) {
        	throw new RuntimeException("创建详情图失败:" + e.toString());
        }
        //返回图片相对路径地址
        return relativeAddr;
	}
}