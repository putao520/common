package common.java.Image;

import common.java.Encrypt.Base64;
import common.java.File.FileHelper;
import common.java.nLogger.nLogger;
import net.coobird.thumbnailator.Thumbnails;

import java.io.*;

public class ImageHelper {
	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @Author: 
	 * @CreateTime: 
	 * @param imgStr base64编码字符串
	 * @param path 图片路径-具体到文件
	 * @return
	*/
	public static boolean generateImage(String imgStr, String path) {
		if (imgStr == null){
			return false;
		}
		try {
			// 解密
			byte[] b = Base64.decodeHex(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
					}
				}
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author: 
	 * @CreateTime: 
	 * @return
	 */
	public static String getImageStr(String imgFile) {
		String rString = null;
		try {
			rString = getImageStr(new FileInputStream(imgFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			nLogger.logInfo(e);
		}
		return rString;
	}
	public static String getImageStr(InputStream inputStream) {
	    byte[] data = null;
	    try {
			data = new byte[inputStream.available()];
			inputStream.read(data);
		} catch (IOException e) {
			nLogger.logInfo(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    // 加密
		return Base64.encode(data);
	}

	public static String thumb(String filePath, int h, int w, String suffix) {
		String newPath;
		try {
			newPath = FileHelper.buildTempFile(suffix);
			Thumbnails.of(filePath).size(w, h).toFile(newPath);
		} catch (IOException e) {
			nLogger.logInfo(e);
			newPath = null;
		}
		return newPath;
	}
}
