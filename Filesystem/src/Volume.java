import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class reads important information in the filesystem image
 */
public class Volume extends Reading{

    private String fileName;
    private RandomAccessFile f;
    private int[] direct;
    private int[] rootPointers;
    private int inodesPerGroup;
    private int inodeSize;

    /**
     * An instance of this class immediately displays important information contained in superblock
     * and displays the contents of the root directory
     * @param fileName
     */
    public Volume(String fileName){
        this.fileName = fileName;

        try {
            f = new RandomAccessFile(fileName, "r");
        }

        catch (IOException e){
            e.printStackTrace();
        }

        SuperBlock();

        Inode two = new Inode(f, 2, testBlockGroup(2));
        two.tableInfo();
        rootPointers = two.getBlocks();
        Directory root = new Directory(f, rootPointers);
        root.contents();

    }

    public void SuperBlock(){
        System.out.println("SUPERBLOCK INFO: ");
        System.out.println("Number of Inodes: " + readInt(f, 1024, 4));
        System.out.println("Number of Blocks: " + readInt(f, 1024 + 4, 4));
        System.out.println("Block Size: " + readInt(f, 1024 + 24, 4));
        System.out.println("No. Blocks per Group: " + readInt(f, 1024 + 32, 4));
        inodesPerGroup = readInt(f, 1024 + 40, 4);
        System.out.println("No. Inodes per Group: " + inodesPerGroup);
        inodeSize = readInt(f, 1024 + 88, 4);
        System.out.println("Size of each Inode: " + inodeSize);
        System.out.println("Volume Label: " + readStr(f, 1024 + 120, 16));
        System.out.println("====================================");
    }


    /**
     * This method determines which block group a given inode number is in
     * @param number the number of the inode
     * @return the block group the inode is in
     */
    public int testBlockGroup(int number){
        if(number > inodesPerGroup){
            if(number > inodesPerGroup * 2){
                return 2;
            }
            else{
                return 1;
            }
        }
        else{
            return 0;
        }
    }


    public RandomAccessFile getF(){
        return f;
    }

    public int[] getRootPointers(){
        return rootPointers;
    }

    public void closeFile(){
        try{
            f.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

}
