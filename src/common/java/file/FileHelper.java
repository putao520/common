package common.java.file;


import common.java.nlogger.nlogger;
import common.java.string.StringHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 *
 */
public class FileHelper {
	public final static String newTempFileName(){
		String path = System.getProperty("java.io.tmpdir");
		return path + "GrapeFW" + UUID.randomUUID().toString();
	}
	/**
	 * 获得系统临时文件夹
	 * */
	public final static String getTempPath(){
		return System.getProperty("java.io.tmpdir");
	}
	/**获得文件通过从本地或者网络
	 * @param path 数据来源
	 * @return
	 */
	public final static File newTempFile(String path){
		File rsFile = null;
		try {
			File newFile = File.createTempFile("grapetmp", null);
            rsFile = FileHelper.getFileEx(path, newFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			nlogger.logInfo(e, path);
		}
		return rsFile;
	}

	/**
	 * 获得随机临时文件
	 * */
	public final static String newRandomTempFile(){
		return newRandomTempFile(null);
	}
	/**
	 * 获得包含指定标识的随机临时文件
	 * */
	public final static String newRandomTempFile(String suffix){
		String filePath = null;
		try {
			File newFile = File.createTempFile("grapetmp", suffix);
			filePath = newFile.getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	/**
	 * @param path 指定的文件夹
	 * @param fileName 指定的文件名
	 * 在指定目录下,根据文件名创建文件,如果文件已存在,在文件名后加 _i
	 * 例子:file.txt 如果存在则是 file_0.txt,如果file_0存在则是file_0_1,依次循环
	 * */
	public final static File newFileEx(String path,String fileName){
		String[] fN = fileName.split("\\.");
		String name = StringHelper.join(fN, ".", fN.length - 1);
		String eName= fN[fN.length - 1];
		String suff = "";
		int i =0;
		String tempPath;
		do{
			tempPath = path + "/" + name + suff + "." + eName;
			//----为下一轮准备suff
			suff = suff + "_" + i;
			i++;
		}while (!createFileEx(tempPath));
		return new File(tempPath);
	}


	public final static boolean createFileEx(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {// 判断文件是否存在
			return false;
		}
		return createFile(filePath);
	}
	public final static boolean createFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {// 判断文件是否存在
			return false;
		}
		if (filePath.endsWith(File.separator)) {// 判断文件是否为目录
			return false;
		}
		if (!file.getParentFile().exists()) {// 判断目标文件所在的目录是否存在
			// 如果目标文件所在的文件夹不存在，则创建父文件夹
			if (!file.getParentFile().mkdirs()) {// 判断创建目录是否成功
				return false;
			}
		}
		try {
			// 创建目标文件
			return file.createNewFile();
		} catch (IOException e) {// 捕获异常
			nlogger.logInfo(e , "创建文件失败！" + filePath);
			return false;
		}
	}
	
	/**从本地或者网络 复制文件到本地
	 * @param sourceURI		来源文件地址
	 * @param targetPath	目的文件地址
	 * @return
	 */
	public final static boolean copyFile(String sourceURI,String targetPath){
		File rsFile = null;
        if (FileHelper.createFile(targetPath)) {//目标文件新建成功
			File targetFile = new File(targetPath);
            rsFile = FileHelper.getFileEx(sourceURI, targetFile);
		}
		return rsFile != null;
	}

	/**获得文件，从本地或者网络
	 * @param path		来源文件地址
	 * @param newFile 	目标文件地址
	 * @return
	 */
	public final static File getFileEx(String path,File newFile){
		return new File(path);
	}
	/**获得文件类型
	 * @param filepath
	 * @return
	 */
    public final static String getfiletype(String filepath) {
		File f = new File(filepath);
		String[] strings;
		if( f.exists() ){
			strings = filepath.split("\\.");
			return strings.length > 0 ? strings[ strings.length -1 ] : "";
		}
		else{
			return "";
		}
	}
	
	/**读文件内容到内存
     * @param
	 * @return
	 */
	public final static byte[] getFile(String path){
		return getFile( new File(path));
	}
	public final static byte[] getFile(File file){
		ByteArrayOutputStream outByte= new ByteArrayOutputStream();
		try (InputStream in = new FileInputStream(file)){
			//System.out.println("以字节为单位读取文件内容，一次读多个字节：");一次读多个字节
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			while ((byteread = in.read(tempbytes)) != -1) {
				outByte.write(tempbytes, 0, byteread);
			}
		}catch (Exception e1) {
			nlogger.logInfo(e1);
		}
		return outByte.toByteArray();
	}


	public final static void deleteFile(String filepath) {
		int i;
		String[] paths = filepath.split(",");
		for (i = 0; i < paths.length; i++) {
            try {
                Files.deleteIfExists(Paths.get(paths[i]));
			} catch (Exception e) {
            }
		}
	}

	public final static <T> void deleteFile(List<T> filepath) {
		for (Object obj : filepath) {
			File f = null;
			if (obj instanceof File) {
				f = (File) obj;
			} else if (obj instanceof String) {
				f = new File((String) obj);
			}
			if (f != null && f.exists()) {
				f.delete();
			}
		}
	}

	/**
	 * 获得文件扩展名
	 * */
    public final static String fileExtension(String filePath) {
        String eName = filePath.substring(0, filePath.lastIndexOf('.') + 1);
        return eName.length() > 0 ? eName : "UNKNOW";
    }

    /**
     * 移动src文件到dest
     */
    public final static boolean moveFile(String src, String dest, boolean over) {
        File srcFile = new File(src);
        if (!srcFile.exists()) {
            //来源文件不存在
            return false;
        }
        File destFile = new File(dest);
        if (destFile.exists()) {
            //目标文件已经存在
            if (!over) {
                return false;
            } else {
                destFile.delete();
            }
        }
        return srcFile.renameTo(destFile);
    }
	/**
	 * 移动src文件到dest
	 */
	public final static boolean moveFile(byte[] srcData, String dest, boolean over) {
		File destFile = new File(dest);
		if (destFile.exists()) {
			//目标文件已经存在
			if (!over) {
				return false;
			} else {
				destFile.delete();
			}
		}
		try{
			if( !destFile.createNewFile() ){
				return false;
			}
		}
		catch (Exception e){
			nlogger.logInfo(e);
			return false;
		}
		try(FileOutputStream fOutputStream = new FileOutputStream(destFile)){
			fOutputStream.write(srcData);
		}
		catch (Exception e){
			nlogger.logInfo(e);
			return false;
		}

		return true;
	}
}
