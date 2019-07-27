//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cc.moq.gc.utils;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class StrUtils extends StringUtils {
    public StrUtils() {
    }

    public static Double toDouble(Object val) {
        if (val == null) {
            return 0.0D;
        } else {
            try {
                return Double.valueOf(trim(val.toString()));
            } catch (Exception var2) {
                return 0.0D;
            }
        }
    }

    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    public static String camelhumpToUnderline(String str) {
        int size;
        char[] chars;
        StringBuilder sb = new StringBuilder((size = (chars = str.toCharArray()).length) * 3 / 2 + 1);

        for(int i = 0; i < size; ++i) {
            char c = chars[i];
            if (isLowercaseAlpha(c)) {
                sb.append(toUpperAscii(c));
            } else {
                sb.append('_').append(c);
            }
        }

        return sb.charAt(0) == '_' ? sb.substring(1) : sb.toString();
    }

    public static String underlineToCamelhump(String name) {
        if (!name.contains("_")) {
            return name;
        } else {
            char[] buffer = name.toCharArray();
            int count = 0;
            boolean lastUnderscore = false;

            for(int i = 0; i < buffer.length; ++i) {
                char c = buffer[i];
                if (c == '_') {
                    lastUnderscore = true;
                } else {
                    c = lastUnderscore && count != 0 ? toUpperAscii(c) : toLowerAscii(c);
                    buffer[count++] = c;
                    lastUnderscore = false;
                }
            }

            if (count != buffer.length) {
                buffer = subarray(buffer, 0, count);
            }

            return new String(buffer);
        }
    }

    public static char[] subarray(char[] src, int offset, int len) {
        char[] dest = new char[len];
        System.arraycopy(src, offset, dest, 0, len);
        return dest;
    }

    public static boolean isLowercaseAlpha(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static char toUpperAscii(char c) {
        if (isLowercaseAlpha(c)) {
            c = (char)(c - 32);
        }

        return c;
    }

    public static char toLowerAscii(char c) {
        if (c >= 'A' && c <= 'Z') {
            c = (char)(c + 32);
        }

        return c;
    }

    public static String firstCharToUpperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String typeCovert(String s) {
        s = s.toLowerCase();
        if ("string".equals(s)) {
            return "java.lang.String";
        } else if ("long".equals(s)) {
            return "java.lang.Long";
        } else if (!"int".equals(s) && !"integer".equals(s)) {
            return "date".equals(s) ? "java.util.Date" : "java.lang.String";
        } else {
            return "java.lang.Integer";
        }
    }

    public static Long[] toLongArray(String idStr, String separator) {
        if (StringUtils.isNotBlank(idStr)) {
            String[] ids = idStr.split(separator);
            List<Long> values = Lists.newArrayList();
            String[] var4 = ids;
            int var5 = ids.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String id = var4[var6];
                if (StringUtils.isNotBlank(id)) {
                    values.add(Long.valueOf(id));
                }
            }

            return (Long[])values.toArray(new Long[0]);
        } else {
            return null;
        }
    }

    public static String firstCharToUpper(String str) {
        StringBuffer sb = new StringBuffer(str);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public static String firstCharToLower(String str) {
        StringBuffer sb = new StringBuffer(str);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    public static boolean isSameType(char str1, char str2) {
        if (str1 == str2) {
            return true;
        } else if (Character.isDigit(str1) && Character.isDigit(str2)) {
            return true;
        } else if (isContainChinese(str1 + "") && isContainChinese(str2 + "")) {
            return true;
        } else {
            return isEnglish(str1 + "") && isEnglish(str2 + "");
        }
    }

    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }

    public static boolean isContainChinese(String str) {
        String regEx = "[\\u4E00-\\u9FA5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
}
