package indi.yugj.test.sitemap.utils;

/**
 *
 * Created by yugj on 17/3/20.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    /**
     * @param path
     * @param text
     * @throws IOException
     */
    public static void write(String path, String text) throws IOException {
        File file = new File(path);
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.append(text);
        bw.flush();
        bw.close();
        fw.close();
    }

    /**
     * @param path
     * @param text
     * @throws IOException
     */
    public static void writeln(String path, String text) throws IOException {
        write(path, text + "\r\n");
    }

}
