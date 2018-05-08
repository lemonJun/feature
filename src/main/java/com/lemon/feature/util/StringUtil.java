package com.lemon.feature.util;

/**
 * 
 *
 *
 * @author WuTong
 * @version 1.0
 * @date  2015年6月11日 下午4:28:30
 * @see 
 * @since
 */
public abstract class StringUtil {

    public static boolean isNullOrEmpty(String value) {
        if (null == value || value.trim().length() < 1) {
            return true;
        }
        return false;
    }

}
