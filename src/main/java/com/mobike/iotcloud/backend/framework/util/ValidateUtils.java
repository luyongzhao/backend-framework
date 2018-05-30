package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.lang.StringUtils;

public class ValidateUtils {

    private static String regExNumber = "\\d*";
    private static String regExFloat = "[-+]?[0-9]*\\.?[0-9]+";
    private static String regExAlphabet = "\\D\\w*";
    private static String regExDate = "^\\d{1,2}[-/.]\\d{1,2}[-/.]\\d{1,4}$";
    private static String regExEmail = "^(.+)@(.+)\\.(.+)$";
    private static String regExPhone = "^([0-9\\(\\)\\/\\+ \\-]*)$";
    private static String regExAlphaNumeric = "[A-Za-z0-9]+";
    private static String regExID = "[A-Za-z0-9\\-]+";
    private static String regExMobile = "^((\\+)?86)?(13|14|15|16|17|18|19)\\d{9}$";
    private static String regPositiveFloat = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
    private static String regExURL = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";


    public static void main(String[] args) {// (http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?

        System.out.println(ValidateUtils.isEmail("luyongzhao@mobike.com"));
    }

    public static boolean isLength(String key, int min, int max) {
        return key != null && (key.length() >= min && key.length() <= max);
    }

    public static boolean isLength(String key, int length) {
        return key != null && key.length() == length;
    }

    /**
     * Checks whether given String key matches with defined custom pattern.
     *
     * @param key String to be checked.
     * @return boolean representing successful match with pattern.
     */
    public static boolean isPattern(String key, String regExPattern) {
        return key.matches(regExPattern);
    }

    /**
     * Checks whether given String key is a valid number.
     *
     * @param key String to be checked.
     * @return boolean representing successful match.
     */
    public static boolean isNumber(String key) {
        return key.matches(regExNumber);
    }

    /**
     * Checks whether given String key is a valid floating point number.
     *
     * @param key String to be checked.
     * @return boolean representing successful match.
     */
    public static boolean isFloat(String key) {
        return key.matches(regExFloat);
    }

    /**
     * Checks whether given String key is a valid Alphabet.
     *
     * @param key String to be checked.
     * @return boolean representing successful match.
     */
    public static boolean isAlphabet(String key) {
        return key.matches(regExAlphabet);
    }

    /**
     * Checks whether given String key is a valid Date. Valid format for Date is
     * standard Indian date format, i.e. DD-MM-YYYY (where, allowed separators
     * are - or . or /)
     *
     * @param key String to be checked.
     * @return boolean representing successful match.
     */
    public static boolean isDate(String key) {
        return key.matches(regExDate);
    }

    /**
     * Checks whether given String key is a valid Date as per given format
     * pattern regex.
     *
     * @param key           String to be checked.
     * @param formatpattern String Regular Expression for Custom Date format.
     * @return boolean representing successful match.
     */
    public static boolean isDate(String key, String formatpattern) {
        return key.matches(formatpattern);
    }

    /**
     * Checks whether given String key is a valid Email address.
     *
     * @param key String to be checked.
     * @return boolean representing successful match.
     */
    public static boolean isEmail(String key) {
        return key.matches(regExEmail);
    }

    /**
     * Checks whether given String key is a valid Phone number. Valid phone
     * number contains only numbers, a dash (-) and a plus sign (+), with no
     * limits in length of number.
     *
     * @param key String to be checked.
     * @return boolean representing successful match.
     */
    public static boolean isPhone(String key) {
        return key.matches(regExPhone);
    }

    /**
     * Checks whether given String key is a valid Password. Valid Password must
     * contain a minimum of one lower case character, one upper case character,
     * one digit, one special character, and at least 8 characters long.
     *
     * @param key String to be checked.
     * @return boolean representing successful match.
     */
    public static boolean isAlphaNumeric(String key) {
        return key.matches(regExAlphaNumeric);
    }

    public static boolean isIdentify(String key) {
        return key.matches(regExID);
    }

    /**
     * Checks whether given String key is a valid Sentence. Valid sentence
     * contains alphabets, numbers, a space, a comma and full-stop only.
     *
     * @param key String to be checked.
     * @return boolean representing successful match.
     */
    public static boolean isMobile(String key) {
        if (StringUtils.isNotBlank(key)) {
            return key.matches(regExMobile);
        } else {
            return false;
        }
    }

    public static boolean isURL(String key) {
        return key.matches(regExURL);
    }

    public static boolean isPositiveFloat(String key) {
        return key.matches(regPositiveFloat);
    }

    public static boolean isLengthNotIn(String str, int min, int max) {
        if (StringUtils.isBlank(str)) {
            return true;
        } else {
            int len = str.length();
            return len < min || len > max;
        }
    }

    public static boolean isWithin(int i, int min, int max) {
        return i >= min && i <= max;
    }



}
