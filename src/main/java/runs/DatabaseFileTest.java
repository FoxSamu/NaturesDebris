package runs;

import modernity.api.util.BinaryMapFile;

import java.io.*;

public final class DatabaseFileTest {
    private DatabaseFileTest() {
    }

    private static BinaryMapFile file;

    public static void main( String[] args ) throws IOException {
        file = BinaryMapFile.create( new File( "database.data" ), 16, BinaryMapFile.Compression.NONE );

        save();
//        load();
    }

    private static void load() throws IOException {
        file.open();

        InputStream stream = file.readEntry( 1 );
        DataInput in = new DataInputStream( stream );
        System.out.println( in.readUTF() );

        InputStream stream2 = file.readEntry( 2 );
        DataInput in2 = new DataInputStream( stream2 );
        System.out.println( in2.readUTF() );

        stream.close();
        stream2.close();
    }

    private static void save() throws IOException {
        OutputStream stream = file.writeEntry( 1 );
        DataOutput out = new DataOutputStream( stream );
        out.writeUTF( "This is UTF-8 text that is spread over multiple sectors... If it's randomly spread it will not be readable..." );

        OutputStream stream2 = file.writeEntry( 2 );
        DataOutput out2 = new DataOutputStream( stream2 );
        out2.writeUTF( "This is a piece of text that may or may not be between the text of the other entry." );

        stream.close();
        stream2.close();

        OutputStream stream3 = file.writeEntry( 1 );
        DataOutput out3 = new DataOutputStream( stream3 );
        out3.writeUTF( "This is overwritten text with more sectors than it had before and it will terefore append sectors at the end of the file so the last part of the text is cut off and pasted at the end of the file..." );

        stream3.close();

        file.close();
    }
}
