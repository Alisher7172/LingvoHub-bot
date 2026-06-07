package uz.lingvohub.bot.util;

public final class CallbackDataUtil {

    private CallbackDataUtil() {
    }

    public static boolean startsWith(String data, String prefix) {
        return data != null && data.startsWith(prefix);
    }

    public static Long extractLong(String[] parts, int index) {
        return Long.parseLong(parts[index]);
    }

    public static Integer extractInt(String[] parts, int index) {
        return Integer.parseInt(parts[index]);
    }
}
