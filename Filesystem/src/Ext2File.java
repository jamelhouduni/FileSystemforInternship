/**
 * This class finds and displays the requested file
 */
public class Ext2File extends Reading {

    private String path;
    private Volume vol;
    private int[] direct = new int[15];
    private int[] whichDirect = new int[15];
    private Directory last;

    /**
     * Instance of Ext2File requires an instance of Volume class
     * and a String path in order to find the required file
     * @param vol the volume that the file will be found using
     * @param path the path of the file to be found
     */
    public Ext2File(Volume vol, String path){
        this.path = path;
        this.vol = vol;
    }

    /**
     * Method to find the file which also directly prints its contents to the console.
     * It takes no parameters as the class stores the required path
     */
    public void find(){
        int fileInode;
        Directory dir = new Directory(vol.getF(), vol.getRootPointers());
        String paths[];

        if(path.contains("/")) {
            paths = path.split("/");
            for(int i = 0; i < paths.length; i++){
                fileInode = dir.search(paths[i]);
                if (dir.getFileType() == 2){
                    Inode in = new Inode(vol.getF(), fileInode, vol.testBlockGroup(fileInode));
                    in.tableInfo();
                    direct = in.getBlocks();
                    dir = new Directory(vol.getF(), direct);
                    dir.contents();
                }
                else {
                    Inode in = new Inode(vol.getF(), fileInode, vol.testBlockGroup(fileInode));
                    in.tableInfo();
                    direct = in.getBlocks();
                    dir = new Directory(vol.getF(),direct);
                    String fileString = dir.retrieve();
                    System.out.println(fileString);
                }
            }
        }
        else {
            fileInode = dir.search(path);
            int foundType = dir.getFoundType();
            Inode in = new Inode(vol.getF(), fileInode, vol.testBlockGroup(fileInode));
            in.tableInfo();
            direct = in.getBlocks();
            Directory finding = new Directory(vol.getF(), direct);
            if (foundType == 1) {
                long iSize = (long) in.getUpperSize() << 32 | (long) in.getLowerSize();
                System.out.println(finding.retrieve());
            }
            else{
                finding.contents();
            }
        }

    }

}
