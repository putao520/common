package common.java.File;

import common.java.nLogger.nLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;

public class ZipFileHelper {
    static final int BUFFER = 8192;

    private final File zipFile;

    public ZipFileHelper(String pathName) {
        zipFile = new File(pathName);
    }

    public List<String> decompress() {
        List<String> resultPath = new ArrayList<>();
        String zipFilePath = this.zipFile.getAbsolutePath();
        String savePath = zipFilePath.substring(0, zipFilePath.lastIndexOf(".")) + File.separator; //保存解压文件目录
        new File(savePath).mkdir(); //创建保存目录
        resultPath.add(savePath);
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    deCompressDirectory(entry, savePath);
                    System.out.println("解压缩：" + savePath + entry.getName());
                } else {
                    deCompressFile(zipFile.getInputStream(entry), savePath + entry.getName());
                    System.out.println("解压缩：" + savePath + entry.getName());
                }
                resultPath.add(savePath + entry.getName());
            }
        } catch (Exception e) {
            nLogger.logInfo(e);
        }
        return resultPath;
    }

    public void compress(List<String> pathNames) {
        String[] pathName = new String[pathNames.size()];
        pathNames.toArray(pathName);
        compress(pathName);
    }

    public void compress(String... pathName) {
        ZipOutputStream out;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
                    new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            for (String s : pathName) {
                compress(new File(s), out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void compress(String srcPathName) {
        File file = new File(srcPathName);
        if (!file.exists()){
            throw new RuntimeException(srcPathName + "不存在！");
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
                    new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            System.out.println("压缩：" + basedir + file.getName());
            this.compressDirectory(file, out, basedir);
        } else {
            System.out.println("压缩：" + basedir + file.getName());
            this.compressFile(file, out, basedir);
        }
    }

    /**
     * 解压缩一个目录
     */
    private void deCompressDirectory(ZipEntry entry, String basedir) {
        File file = new File(basedir + entry.getName());
        file.mkdirs();
    }

    /**
     * 解压缩一个文件
     */
    private void deCompressFile(InputStream is, String filename) {
        File file = new File(filename);
        if (!file.exists() && filename.lastIndexOf("/") != -1) { //如果是目录先创建
            new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
        }
        try {
            file.createNewFile(); //创建文件
            int count;
            byte[] data = new byte[BUFFER];
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file), BUFFER);
            while ((count = is.read(data)) != -1) {
                bos.write(data, 0, count);
            }
            bos.flush();
            bos.close();
        } catch (Exception e) {
            nLogger.logInfo(e);
        }
    }

    /**
     * 压缩一个目录
     */
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()){
            return;
        }

        File[] files = dir.listFiles();
        for (File file : files) {
            /* 递归 */
            compress(file, out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));

            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*
    public static void main(String[] args) {
        ZipCompressor zc = new ZipCompressor("C:/Users/wj/Desktop/aaa.zip");
        zc.compress("C:/Users/wj/Desktop/water.js","C:/Users/wj/Desktop/index.jsp");
    }
    */
}
