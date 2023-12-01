package chess.style;

public enum Style {
    DEFAULT, DEEP_BLUE, DEEP_BLACK;
//    public static String default = "default";
    public static String getStyle(Style style){
        switch (style){
            case DEFAULT:
                return "default";
            case DEEP_BLUE:
                return "deepBlue";
            case DEEP_BLACK:
                return "deepBlack";
        }
        return null;
    }

}
