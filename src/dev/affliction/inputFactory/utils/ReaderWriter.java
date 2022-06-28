package dev.affliction.inputFactory.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.EnumSet;

public class ReaderWriter {


    public static void write(String loc, String name, int[] data) { //MEMORY MAPPED
        File directory = new File(loc);
        File f = new File(loc+"/"+name);
        try {
            if(!directory.exists()) directory.mkdirs();
            if(!f.exists()) f.createNewFile();
            try (FileChannel fileChannel = (FileChannel) Files
                    .newByteChannel(Paths.get(f.getPath()), EnumSet.of(
                            StandardOpenOption.READ,
                            StandardOpenOption.WRITE,
                            StandardOpenOption.TRUNCATE_EXISTING))) {

                MappedByteBuffer mappedByteBuffer = fileChannel
                        .map(FileChannel.MapMode.READ_WRITE, 0, data.length * 4);

                if (mappedByteBuffer != null) {
                    IntBuffer ib = mappedByteBuffer.asIntBuffer();
                    ib.put(data);
                }
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    public static void read(String loc) {
        long current = System.currentTimeMillis();
        try (RandomAccessFile raf = new RandomAccessFile(loc,"r");
             FileChannel fileChannel = raf.getChannel()) {
            MappedByteBuffer mappedByteBuffer = fileChannel
                    .map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            if (mappedByteBuffer != null) {
                IntBuffer ib = mappedByteBuffer.asIntBuffer();
                int[] ii = new int[ib.capacity()];
                ib.get(ii);
                System.out.println(Arrays.toString(ii));
                //for(int i = 0; i < ib.capacity(); i++) ib.get(i);
                mappedByteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Reading took : "+(System.currentTimeMillis() - current));
    }


}
