package common.java.File;

import common.java.String.StringHelper;
import common.java.nLogger.nLogger;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FileHelper<T extends FileHelper> {
    protected static final int MAX_BLOCK_LENGTH = 0xffffffff;
    protected final File file;
    public long readPoint = 0;
    public long writePoint = 0;
    private FileInputStream inStream;
    private FileOutputStream outStream;
    private MappedByteBuffer[] fileMap;
    private Consumer<File> func;

    protected FileHelper(File file) {
        this.file = file;
    }

    public static FileHelper load(File file) {
        return new FileHelper<>(file);
    }

    protected void error_handle() {
        if (func != null) {
            func.accept(this.file);
            throw new RuntimeException("file failed!");
        }
    }

    public T setErrorHandle(Consumer<File> func) {
        this.func = func;
        return (T) this;
    }

    /**
     * 新建文件
     */
    public T create() {
        boolean rb;
        try {
            rb = this.file.createNewFile();
        } catch (Exception e) {
            rb = false;
        }
        if (!rb) {
            error_handle();
        }
        return (T) this;
    }

    /**
     * 文件是否存在
     */
    public boolean exists() {
        return this.file.exists();
    }

    /**
     * 文件移动
     */
    public T move(File path) {
        boolean rb;
        try {
            rb = this.file.renameTo(path);
        } catch (Exception e) {
            rb = false;
        }
        if (!rb) {
            error_handle();
        }
        return (T) this;
    }

    /**
     * 文件复制
     */
    public T copy(File path) {
        try {
            Files.copy(this.file.toPath(), path.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            error_handle();
        }
        return (T) this;
    }

    /**
     * 删除文件
     */
    public T delete() {
        boolean rb;
        try {
            rb = this.file.delete();
        } catch (Exception e) {
            rb = false;
        }
        if (!rb) {
            error_handle();
        }
        return (T) this;
    }

    /**
     * 比较文件是否一致
     */
    public boolean equal(File file) {
        boolean rb;
        try {
            rb = Files.isSameFile(this.file.toPath(), file.toPath());
        } catch (Exception e) {
            rb = false;
        }
        return rb;
    }

    /**
     * 获得文件大小
     */
    public long size() {
        return this.file.length();
    }

    /**
     * 获得文件路径
     */
    public String path() {
        return file.getAbsolutePath();
    }

    /**
     * 获得文件名
     */
    public String fileName() {
        return file.getName();
    }

    /**
     * 获得文件最后修改时间
     */
    public long lastModified() {
        return file.lastModified();
    }

    /**
     * 获得文件最后修改时间
     */
    public T setLastModified(long unixtime) {
        boolean rb;
        try {
            rb = file.setLastModified(unixtime);
        } catch (Exception e) {
            rb = false;
        }
        if (!rb) {
            error_handle();
        }
        return (T) this;
    }

    protected FileInputStream getInputStream() {
        if (inStream == null) {
            try {
                inStream = new FileInputStream(this.file);
            } catch (Exception e) {
                inStream = null;
                error_handle();
            }
        }
        return this.inStream;
    }

    protected FileOutputStream getOutputStream() {
        if (outStream == null) {
            try {
                outStream = new FileOutputStream(this.file);
            } catch (Exception e) {
                outStream = null;
                error_handle();
            }
        }
        return this.outStream;
    }

    protected void release() {
        this.unmapAll();
        try {
            if (this.inStream != null) {
                this.inStream.close();
                this.inStream = null;
            }
            if (this.outStream != null) {
                this.outStream.flush();
                this.outStream.close();
                this.outStream = null;
            }
        } catch (Exception e) {
        }
    }

    protected void unmap(MappedByteBuffer fmap) {
        AccessController.doPrivileged((PrivilegedAction) () -> {
            try {
                Method getCleanerMethod = fmap.getClass().getMethod("cleaner");
                getCleanerMethod.setAccessible(true);
                Unsafe.getUnsafe().invokeCleaner(fmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private void unmapAll() {
        if (fileMap != null) {
            for (MappedByteBuffer fmap : fileMap) {
                if (fmap != null && fmap.isLoaded()) {
                    this.unmap(fmap);
                }
            }
        }
    }

    protected MappedByteBuffer[] getFileBuffer(long offset, int length) {
        int beginIdx = FilePointer.getBlockIdx(offset);
        int endIdx = FilePointer.getBlockIdx(offset + length);
        List<MappedByteBuffer> mapList = new ArrayList<>();
        for (int i = beginIdx; i <= endIdx; i++) {
            MappedByteBuffer fmap = fileMap[i];
            mapList.add(fmap);
            if (!fmap.isLoaded()) {
                fmap.load();
            }
        }
        return mapList.toArray(new MappedByteBuffer[0]);
    }

    protected MappedByteBuffer getFileBuffer(long offset) {
        int blockIdx = FilePointer.getBlockIdx(offset);
        MappedByteBuffer fmap = fileMap[blockIdx];
        if (!fmap.isLoaded()) {
            fmap.load();
        }
        return fmap;
    }

    protected MappedByteBuffer getFileBuffer(int idx) {
        return fileMap[idx];
    }

    protected MappedByteBuffer[] getFileBuffer() {
        if (fileMap == null) {
            List<MappedByteBuffer> mapList = new ArrayList<>();
            try {
                FileInputStream fis = this.getInputStream();
                FileChannel fc = fis.getChannel();
                int blockSize = (int) Math.ceil(fc.size() / MAX_BLOCK_LENGTH);
                long tCmp, tSize;
                for (int i = 0; i < blockSize; i++) {
                    tCmp = fc.size() - i * MAX_BLOCK_LENGTH;
                    tSize = (tCmp > MAX_BLOCK_LENGTH) ? MAX_BLOCK_LENGTH : tCmp;
                    mapList.add(fc.map(FileChannel.MapMode.READ_WRITE, i * MAX_BLOCK_LENGTH, tSize));
                }
                fileMap = mapList.toArray(new MappedByteBuffer[0]);
            } catch (Exception e) {
                fileMap = null;
            }
        } else {
            for (MappedByteBuffer mappedByteBuffer : fileMap) {
                mappedByteBuffer.reset();
            }
        }
        return fileMap;
    }

    // -----------------------------------------------------------
    public final static String newTempFileName() {
        return getTempDirectory() + "GrapeFWv3" + UUID.randomUUID();
    }

    /**
     * 获得系统临时文件夹
     */
    public final static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获得当前目录
     */
    public final static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * 获得当前用户目录
     */
    public final static String getUserDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * 获得文件通过从本地或者网络
     *
     * @param path 数据来源
     * @return
     */
    public final static File buildTempFileAt(String path) {
        try {
            File newFile = File.createTempFile("grape_tmp", null);
            return FileHelper.getFileEx(path, newFile);
        } catch (IOException e) {
            throw new RuntimeException(path, e);
        }
    }

    /**
     * 获得随机临时文件
     */
    public final static String buildTempFile() {
        return buildTempFile(null);
    }

    /**
     * 获得包含指定标识的随机临时文件
     */
    public final static String buildTempFile(String suffix) {
        try {
            File newFile = File.createTempFile("grape_tmp", suffix);
            return newFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param path     指定的文件夹
     * @param fileName 指定的文件名
     *                 在指定目录下,根据文件名创建文件,如果文件已存在,在文件名后加 _i
     *                 例子:file.txt 如果存在则是 file_0.txt,如果file_0存在则是file_0_1,依次循环
     */
    public final static File buildFile(String path, String fileName) {
        String[] fN = fileName.split("\\.");
        String name = StringHelper.join(fN, ".", fN.length - 1);
        String eName = fN[fN.length - 1];
        String suff = "";
        int i = 0;
        String tempPath;
        do {
            tempPath = path + "/" + name + suff + "." + eName;
            //----为下一轮准备suff
            suff = suff + "_" + i;
            i++;
        } while (!createFile(tempPath));
        return new File(tempPath);
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
            nLogger.logInfo(e, "创建文件失败！" + filePath);
            return false;
        }
    }

    /**
     * 从本地或者网络 复制文件到本地
     *
     * @param sourcePath 来源文件地址
     * @param targetPath 目的文件地址
     * @return
     */
    public final static boolean copyFile(String sourcePath, String targetPath) {
        File rsFile = null;
        if (FileHelper.createFile(targetPath)) {//目标文件新建成功
            File targetFile = new File(targetPath);
            rsFile = FileHelper.getFileEx(sourcePath, targetFile);
        }
        return rsFile != null;
    }

    /**
     * 获得文件，从本地或者网络
     *
     * @param path    来源文件地址
     * @param newFile 目标文件地址
     * @return
     */
    public final static File getFileEx(String path, File newFile) {
        // 复制 path 文件内容到 newFile
        File oldFile = new File(path);
        try {
            Files.copy(oldFile.toPath(), newFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oldFile;
    }

    /**
     * 获得文件类型(获得文件扩展名)
     *
     * @param filepath
     * @return
     */
    public final static String getFileType(String filepath) {
        File f = new File(filepath);
        String[] strings;
        if (f.exists()) {
            strings = filepath.split("\\.");
            return strings.length > 0 ? strings[strings.length - 1] : "";
        } else {
            return "";
        }
    }

    /**
     * 读文件内容到内存
     *
     * @param
     * @return
     */
    public final static byte[] getFile(String path) {
        return getFile(new File(path));
    }

    public final static byte[] getFile(File file) {
        ByteArrayOutputStream outByte = new ByteArrayOutputStream();
        try (InputStream in = new FileInputStream(file)) {
            //System.out.println("以字节为单位读取文件内容，一次读多个字节：");一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread;
            while ((byteread = in.read(tempbytes)) != -1) {
                outByte.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            nLogger.logInfo(e1);
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
     */
    public final static String fileExtension(String filePath) {
        String eName = filePath.substring(0, filePath.lastIndexOf('.') + 1);
        return eName.length() > 0 ? eName : "UNKNOWN";
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
     *
     * @param srcData 数据源
     * @param dest    目标文件路径
     * @param over    是否覆盖
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
        try {
            if (!destFile.createNewFile()) {
                return false;
            }
        } catch (Exception e) {
            nLogger.logInfo(e);
            return false;
        }
        try (FileOutputStream fOutputStream = new FileOutputStream(destFile)) {
            fOutputStream.write(srcData);
        } catch (Exception e) {
            nLogger.logInfo(e);
            return false;
        }

        return true;
    }
}
