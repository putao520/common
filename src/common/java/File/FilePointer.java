package common.java.File;

/**
 * 文件块操作
 */
public class FilePointer {
    private final int BlockID;
    private final int BlockPointer;
    private int length;

    private FilePointer(long offset) {
        this.BlockID = FilePointer.getBlockIdx(offset);
        this.BlockPointer = FilePointer.getBlockPoint(offset);
    }

    private FilePointer(int blockID, int offset, int length) {
        this.BlockID = blockID;
        this.BlockPointer = offset;
        this.length = length;
    }

    public static FilePointer build(int blockID, int offset, int length) {
        return new FilePointer(blockID, offset, length);
    }

    public static FilePointer build(long offset) {
        return new FilePointer(offset);
    }

    public static int getBlockIdx(long offset) {
        return (int) offset / FileHelper.MAX_BLOCK_LENGTH;
    }

    public static int getBlockPoint(long offset) {
        return (int) offset / FileHelper.MAX_BLOCK_LENGTH;
    }

    public int idx() {
        return this.BlockID;
    }

    public int point() {
        return this.BlockPointer;
    }

    public int length() {
        return this.length;
    }
}
