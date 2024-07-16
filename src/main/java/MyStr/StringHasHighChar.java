package MyStr;

/**
 * 高代理项字符串处理类
 */
public class StringHasHighChar {
    /**
     * 将字符串中的代理项转换为BMP字符 '\u0E06'
     * @param originalString
     * @return
     */
    public static String convertSurrogatePairsToBMP(String originalString) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < originalString.length(); i++) {
            char c = originalString.charAt(i);
            if (Character.isHighSurrogate(c) && i + 1 < originalString.length()) {
                char d = originalString.charAt(i + 1);
                if (Character.isLowSurrogate(d)) {
                    // 这是一个有效的代理对，进行替换
                    sb.append('\u0E06');
                    i++; // 跳过下一个字符
                } else {
                    // 不是代理对，保留原样
                    sb.append(c);
                }
            } else {
                // 不是高代理项字符，保留原样
                sb.append(c);
            }
        }
        sb.substring(0, sb.length() - 1);
        return sb.toString();
    }

    /**
     * 获取高代理项字符串的子串
     * @param originalString
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String subStringForHasHighChar(String originalString, int startIndex, int endIndex) {
        String s = convertSurrogatePairsToBMP(originalString);

        String subString1 = s.substring(0, startIndex);
        // 计算 subString 中 ฆ 字符的数量
        Long count1 = subString1.codePoints()
                .filter(cp -> cp == '\u0E06') // 'ฆ' 的 Unicode 编码
                .count();

        String subString = s.substring(startIndex, endIndex);
        // 计算 subString 中 ฆ 字符的数量
        Long count = subString.codePoints()
                .filter(cp -> cp == '\u0E06') // 'ฆ' 的 Unicode 编码
                .count();

        return originalString.substring(startIndex + count1.intValue(), endIndex + count.intValue() + count1.intValue());
    }

}
