package webForZYG;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
    public static void close(Closeable...closeables){
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
