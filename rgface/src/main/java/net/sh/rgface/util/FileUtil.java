package net.sh.rgface.util;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by DESTINY on 2018/4/17.
 */
public class FileUtil {

    static {
        //System.out.println("loadDLL---------------");
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void byteToImageFile(byte[] imageByte, String imagePath) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(imagePath);

        fileOutputStream.write(imageByte);

        fileOutputStream.close();
    }
//
//    public int detectFace(String imagePath, String OutPutfileName) {
//
//        long beTime = System.currentTimeMillis();
//        long endTime;
//
//        System.out.println("\nRunning DetectFaceDemo Time" + beTime);
//
//        //从配置文件lbpcascade_frontalface.xml中创建一个人脸识别器，该文件位于opencv安装目录中 替换xml进行不同特征识别
//
//        System.out.println(System.getProperty("user.dir") + "\\opencv\\xml\\haarcascade_frontalface_alt.xml");
////        String xmlfilePath = getClass().getResource("haarcascade_frontalface_alt.xml").getPath().substring(1);
////        System.out.println(xmlfilePath);
//        CascadeClassifier faceDetector = new CascadeClassifier(System.getProperty("user.dir")+"\\opencv\\\\xml\\haarcascade_frontalface_alt.xml");
//
//        Mat image = Imgcodecs.imread(imagePath); //读取图片
//
//        //在图片中检测人脸
//        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(image, faceDetections);
//
//        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
//
//        Rect[] rects = faceDetections.toArray();
//
//        if (rects == null || rects.length == 0) {
//            System.out.println("识别失败，没有检测到人脸");
//            return 1;
//        }
//
//        if (rects.length > 1) {
//            System.out.println("检测到多个人脸");
//            return 2;
//        }
//
//        //单人脸
//        Rect rect = rects[0];
//        Imgproc.rectangle(image, new Point(rect.x - 2, rect.y - 2), new Point(rect.x
//                + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
//        Mat sub = image.submat(rect);
//
//        Mat mat = new Mat();
//        Size size = new Size(211, 284);
//        Imgproc.resize(sub, mat, size);//将人脸进行截图并保存
//
//        //保存失败
//        if (!Imgcodecs.imwrite(" \\Users\\DESTINY\\Desktop\\sh_detect_face_result\\" + OutPutfileName, mat)) {
//            return 3;
//        }
//
//        endTime = System.currentTimeMillis();
//
//        System.out.println("\nDone DetectFaceDemo Time" + endTime + "use Time : " + (endTime - beTime));
//
//        return 0;
//    }

}
