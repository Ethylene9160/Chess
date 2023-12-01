package chess.util;

import java.io.Closeable;
import java.io.IOException;

//io关闭类工具包，参考自B站
public class CloseUtil {
    public static void closeAll(Closeable...able) {
        for(Closeable c : able){
            if(c != null){
                try{
                    c.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}

