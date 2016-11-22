package com.dark.imageutil;

import com.dark.imageutil.image.ImageDimension;
import com.dark.imageutil.image.ImageUtil;
import com.dark.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by darkxue on 17/11/16.
 * this demo is show  us  how to use imageUtil
 */
public class ImageDemo {

    private static String originalUrl="http://www.wallcoo.com/game/MMOG_NIE_Tx2/wallpapers/1280x800/gxwlm.jpg";
    private static String targetPath="/data/workspaces/test/java_demo/target/";


    private static final Logger logger = LoggerFactory.getLogger(ImageDemo.class);
    public static void main(String[] args){
        // 根据比例压缩
        // 1. 从网络获取图片数据，由于网络可能会很慢，所以这个操作最好是只进行一次
        byte[] imageData = null;
        try {
            imageData = HttpClientUtil.getFileData(originalUrl);

        } catch (IOException e) {
            logger.error("Cannot get data for url {}, exception is {}", originalUrl,
                    e.getMessage());
        }


        // 获取图片的原始大小
        String formatName = ImageUtil.getImageFormatName(imageData);
        ImageDimension originalDimension = ImageUtil.getImageDimension(imageData, formatName);

        // 获取图片的类型，暂时认为图片只能为jpg
        if (formatName == null) {
            formatName = ImageUtil.IMAGE_FORMAT.JPG.getValue();
        }
        // 3. 将原始宽度和标准宽度进行比较，如果需要改变尺寸，　且原始宽度大于标准宽度，则等比压缩。
        // 否则，取其实际的宽度和高度
        ImageDimension composeDimension = null;

        int standardWidth = 500;

        // 根据标准宽度，等比计算出对应的高度
        int newHeight = (int) (standardWidth * originalDimension.getHeight()
                / (double) originalDimension.getWidth());

        composeDimension = new ImageDimension(standardWidth, newHeight);

        ByteArrayOutputStream os;
        try {

            imageData = ImageUtil.compressPic(imageData, formatName);//先压缩

            os = ImageUtil.cutAndZoomImage(imageData, formatName, null, composeDimension, 0);

            OutputStream outputStream=new FileOutputStream(new File(targetPath+"WATCH.jpg"));

            os.writeTo(outputStream);
        } catch (IOException e) {
            logger.error("cannot get the output stream for url {}", originalUrl);

        }
    }
}
