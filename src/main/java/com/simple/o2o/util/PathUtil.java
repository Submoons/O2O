package com.simple.o2o.util;

public class PathUtil {

    /*
     * 根据不同的操作系统，设置储存图片文件不同的根目录
     */
    private static String seperator = System.getProperty("file.separator");
    public static String getImgBasePath() {

        String os =System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")) {
          basePath = "E:/O2O/image";    //根据自己的实际路径进行设置
        }else {
            basePath = "/home/o2o/image/";//根据自己的实际路径进行设置
        }
        basePath = basePath.replace("/", seperator);
        return basePath;
    }

    //根据不同的业务需求返回不同的子路径
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop/"+ shopId + "/";
        return imagePath.replace("/", seperator);
    }
}