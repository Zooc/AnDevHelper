package an.devhp.util;

/**
 * @description:
 * @author: Kenny
 * @date: 2017-06-23 01:18
 * @version: 1.0
 */

public class StringUtil {

    /**
     * 字符时候为空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim()) || string == "null";
    }

}
