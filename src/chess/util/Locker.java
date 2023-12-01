package chess.util;

public class Locker {
    public static final String setLocker(String str){
        char[] ches = str.toCharArray();
        int len = ches.length;
        for (int i = 0; i < len; i++) {
            ches[i] += i%14;
        }
        String s = new String(ches);
        return s;
    }

    public static String getLocker(String str){
        char[] ches = str.toCharArray();
        int len = ches.length;
//        StringBuilder b = new StringBuilder();
        for (int i = 0; i < len; i++) {
            ches[i] -= i%14;
//            if(ches[i] != '\n') b.append(ches[i]);

        }
//        return b.toString();
        return new String(ches);
    }


    public static void main(String[] args) {
        String str = "你好呀！";
        System.out.println(setLocker(str));
        System.out.println(getLocker(setLocker(str)));
    }

}
