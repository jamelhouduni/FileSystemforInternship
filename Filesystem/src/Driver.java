public class Driver {

    public static void main(String[] args) {
        Volume v = new Volume("ext2fs");
        Ext2File file = new Ext2File(v, "deep/down/in/the/filesystem/there/lived/a/file");   //deep/down/in/the/filesystem/there/lived/a/file
        file.find();
        v.closeFile();
    }
}
