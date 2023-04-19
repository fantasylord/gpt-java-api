package com.lord.local.gptjavaapi.uitls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VliadatorUtil {
    private VliadatorUtil() {

    }

    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$";

    private static final String Account_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * 必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间
     *
     * @param password
     * @return
     */
    public static boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * 字母开头，允许5-16字节，允许字母数字下划线
     *
     * @param account
     * @return
     */
    public static boolean validateAccount(String account) {
        Pattern pattern = Pattern.compile(Account_PATTERN);
        Matcher matcher = pattern.matcher(account);
        return matcher.matches();
    }
}
