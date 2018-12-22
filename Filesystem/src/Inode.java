import java.io.RandomAccessFile;

/**
 * THis class is a representation of an inode and houses important information about it
 */
public class Inode extends Reading {

    private RandomAccessFile f;
    private int fileMode;
    private int lastTime;
    private short userID;
    private short groupID;
    private short references;
    private int[] direct = new int[15];
    private int singleIndirect;
    private int doubleIndirect;
    private int tripleIndirect;
    private int number;
    private int lowerSize;
    private int upperSize;
    private int blockGroup;
    private int indirect = 0;

    /**
     * an instance of this class retrieves information about the number inode that is given
     * @param f the filesystem image to read from
     * @param number the number of the required inode
     * @param blockGroup the blockgroup the inode is in
     */
    public Inode(RandomAccessFile f, int number, int blockGroup){
        this.f = f;
        this.number = number;
        this.blockGroup = blockGroup;

    }

    /**
     * This method retrieves and displays the information about the
     * given inode.
     */
    public void tableInfo(){
        System.out.println("INODE TABLE: Inode " + number);
        int tablePointer;

        if(blockGroup == 0){
            tablePointer = readInt(f, 1024 + 1024 + 8, 4);
            tablePointer = tablePointer * 1024 + ((number-1) * 128);

        }
        else if(blockGroup == 1){
            tablePointer = readInt(f, 1024 + 1024 + 8 + 32, 4);
            tablePointer = tablePointer * 1024 + ((number - 1712 - 1) * 128);

        }
        else if(blockGroup == 2){
            tablePointer = readInt(f, 1024 + 1024 + 8 + 64, 4);
            tablePointer = tablePointer * 1024 + ((number - 3424 - 1) * 128);
        }
        else{
            tablePointer = readInt(f, 1024 + 1024 + 8, 4);
            tablePointer = tablePointer * 1024 + ((number-1) * 128);
        }

        fileMode = readSho(f, tablePointer, 2);
        lowerSize = readInt(f, tablePointer + 4, 4);
        upperSize = readInt(f, tablePointer + 108, 4);
        System.out.println("File Mode: " + String.format("%#02x", fileMode));
        System.out.println("File Mode: " + fileMode);
        System.out.println("Size (lower): " + lowerSize);
        System.out.println("Size (upper): " + upperSize);
        long fullSize = (long)upperSize << 32 | (long)lowerSize;
        System.out.println("Full Size: " + fullSize);


        for(int i = 0; i < 12; i++){
            direct[i] = readInt(f, tablePointer + 40 + (i * 4), 4);
            System.out.println("Direct Pointer " + i + ": " + direct[i]);
        }

        singleIndirect = readInt(f, tablePointer + 88, 4);
        doubleIndirect = readInt(f, tablePointer + 92, 4);
        tripleIndirect = readInt(f, tablePointer + 96, 4);

        System.out.println("Indirect Pointer: " + singleIndirect);
        System.out.println("Double Indirect Pointer: " + doubleIndirect);
        System.out.println("Triple Indirect Pointer: " + tripleIndirect);
        direct[12] = singleIndirect;
        direct[13] = doubleIndirect;
        direct[14] = tripleIndirect;
        System.out.println("====================================");
    }

    public int[] getBlocks(){
        return direct;
    }

    public int getNumber(){ return number; }

    public int getFileMode(){ return fileMode; }

    public int getLowerSize(){ return lowerSize; }

    public int getUpperSize() { return upperSize; }

}

