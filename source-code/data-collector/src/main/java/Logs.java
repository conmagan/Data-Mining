public class Logs {
    public static void infoLn(Object o) {
        info(o);
        info("\n");
    }

    public static void info(Object o) {
        System.out.print(toStr(o));
    }

    private static String toStr(Object o) {
        return o == null ? "null" : o.toString();
    }
}