import java.io.RandomAccessFile;

/**
 * The directory class is a representation of a file
 * or directory
 */
public class Directory extends Reading{

    private String name;
    private int found;
    private RandomAccessFile f;
    private int[] direct;
    private int[] whichDirect = new int[15];
    private int fileType;
    private int foundType;

    /**
     * An instance of this class requires the working file, and an array of ints which are
     * pointers to the blocks in the filesystem iamge that house the directory/file
     * @param f the filesystem image
     * @param direct int array which are pointers to the actual data for the directory/file
     */
    public Directory(RandomAccessFile f, int[] direct){
        this.f = f;
        this.direct = direct;
    }

    /**
     * displays the contents of the directory
     * this is only called if the instance is a directory and not a file
     */
    public void contents() {
        System.out.println("DIRECTORY CONTENTS");

        int blocks = 0;
        for (int i = 0; i < direct.length; i++) {
            if (direct[i] != 0) {
                    blocks++;
                    whichDirect[i] = 1;
            }
            else {
                whichDirect[i] = 0;
            }
        }

        int count = 0;
        for (int i = 0; i < direct.length; i++) {
                while (count < 1024) {
                    int startB = (1024 * direct[i]) + count;
                    int inode = readInt(f, startB, 4);
                    int leng = readSho(f, startB + 4, 2);
                    int nameLeng = readByte(f, startB + 6, 1);
                    int fileType = readByte(f, startB + 7, 1);

                    name = readStr(f, startB + 8, nameLeng);

                    System.out.println(inode + " " + leng + " " + nameLeng + " " + fileType + " " + name);
                    count += leng;
                    startB += leng;
                }
        }
        System.out.println("====================================");
    }

    /**
     * This method trawls through the contents of the directory and checks if the next
     * directory/file in the path is present and returns the number of the inode relating to that.
     * @param path the name of the next directory/file
     * @return the number of the inode of the found directory/file
     */
    public int search(String path){

        int foundInode = 0;
        boolean found = false;
        int blocks = 0;
        for (int i = 0; i < direct.length; i++) {
            if (direct[i] != 0) {
                blocks++;
                whichDirect[i] = 1;
            }
            else{
                whichDirect[i] = 0;
            }
        }
        int count = 0;
        for (int i = 0; i < direct.length; i++) {
                if (whichDirect[i] == 1) {
                    while (count < 1024) {
                        int startB = (1024 * direct[i]) + count;
                        int inode = readInt(f, startB, 4);
                        int leng = readSho(f, startB + 4, 2);
                        int nameLeng = readByte(f, startB + 6, 1);
                        fileType = readByte(f, startB + 7, 1);
                        name = readStr(f, startB + 8, nameLeng);
                        if (path.equals(name)) {
                            foundInode = inode;
                            foundType = fileType;
                        }

                        count += leng;
                        startB += leng;
                    }
                }
        }
        return foundInode;
    }

    /**
     * This method retrieves the actual contents of the file and is only called if this instance is a file.
     * @return a string representation of the contents of this file
     */
    public String retrieve(){
        String complete = "";
        int blocks = 0;
        for (int i = 0; i < direct.length; i++) {
            if (direct[i] != 0) {
                blocks++;
                whichDirect[i] = 1;
            }
        }

        int startB = (1024 * direct[0]);
        int[] testIndirect = new int[256];
        String[] parts = new String[blocks];
        int partsCount = 0;
        for(int i = 0; i < direct.length; i++) {
            if(i >= 12 && whichDirect[i] == 1) {
                if(i == 12){
                    startB = 1024 * direct[i];
                    for(int j = 0; j < 256; j++){
                         testIndirect[j] = readInt(f, startB + (j*4), 4);
                         if(testIndirect[j] != 0){
                             complete += readStr(f, 1024 * testIndirect[j], 1024);
                         }
                    }
                }
                else if(i == 13){
                    for(int j = 0; j < 256; j++){
                        startB = 1024 * direct[i];
                        if(readInt(f, startB + (j*4), 4) != 0){
                            startB = 1024 * readInt(f, startB + (j*4), 4);
                            for(int k = 0; k < 256; k++){
                                if(readInt(f, startB + (k*4), 4) != 0){
                                    complete += readStr(f, 1024 * readInt(f, startB + (k*4), 4), 1024);
                                }
                            }
                        }
                    }
                }
                else if (i == 14){
                    for(int j = 0; j < 256; j++){
                        startB = 1024 * direct[i];
                        if(readInt(f, startB + (j*4), 4) != 0){
                            startB = 1024 * readInt(f, startB + (j*4), 4);
                            for(int k = 0; k < 256; k++){
                                if(readInt(f, startB + (k*4), 4) != 0){
                                    startB = 1024 * readInt(f, startB + (k*4), 4);
                                    for(int l = 0; l < 256; l++){
                                        if(readInt(f, startB + (l*4), 4) != 0){
                                            complete += readStr(f, 1024 * readInt(f, startB + (l*4), 4), 1024);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                if (whichDirect[i] == 1) {
                    startB = (1024 * direct[i]);
                    parts[partsCount] = readStr(f, startB, 1024);
                    complete += parts[partsCount];
                    partsCount++;
                }
            }
        }
        return complete;
    }

    public int getFileType(){
        return fileType;
    }

    public int getFoundType(){
        return foundType;
    }
    public int[] getDirect(){
        return direct;
    }

    public int[] getWhichDirect(){
        return whichDirect;
    }

}
