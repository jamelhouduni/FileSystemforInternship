import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * This is an abstract class that just houses a few methods that read the given file and returns what was read in a certain format.
 */
public abstract class Reading {

    private String fileName;
    private  byte[] b;
    private ByteBuffer bb;
    private RandomAccessFile f;
    private int tes;
    private short sho;
    private String str = "";
    private byte byt;

    /**
     * Method to read the file and return an int
     * @param f The RandomAccessFile to read
     * @param start starting byte to read from
     * @param leng length to read
     * @return bytes that were read in an int format
     */
    public int readInt(RandomAccessFile f, int start, int leng){
        try {
            f.seek(start);
            b = new byte[leng];
            f.read(b);
            bb = ByteBuffer.wrap(b);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            tes = bb.getInt();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return tes;
    }

    /**
     * Method to read the file and return a short
     * @param f The RandomAccessFile to read
     * @param start starting byte to read from
     * @param leng length to read
     * @return bytes that were read in an short format
     */
    public short readSho(RandomAccessFile f, int start, int leng){
        try {
            f.seek(start);
            b = new byte[leng];
            f.read(b);
            bb = ByteBuffer.wrap(b);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            sho = bb.getShort();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return sho;
    }

    /**
     * Method to read the file and return a String
     * @param f The RandomAccessFile to read
     * @param start starting byte to read from
     * @param leng length to read
     * @return bytes that were read in a String format
     */
    public String readStr(RandomAccessFile f, int start, int leng){
        try {
            str = "";
            f.seek(start);
            b = new byte[leng];
            f.read(b);
            bb = ByteBuffer.wrap(b);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            for(int i = 0; i < leng; i++){
                str = str + (char)bb.get(i);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return str;

        }

    /**
     * Method to read the file and return a byte
     * @param f The RandomAccessFile to read
     * @param start starting byte to read from
     * @param leng length to read
     * @return bytes that were read in a byte format
     */
    public byte readByte(RandomAccessFile f, int start, int leng){
        try {
            f.seek(start);
            b = new byte[leng];
            f.read(b);
            bb = ByteBuffer.wrap(b);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            byt = bb.get();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return byt;

    }

}
