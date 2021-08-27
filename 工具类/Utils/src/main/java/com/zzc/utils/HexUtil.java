package com.zzc.utils;

/**
 * This class provides convenient functions to convert hex string to byte array
 * and vice versa.
 *
 * @author 99bill
 */
public final class HexUtil {

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private HexUtil() {
    }

    /**
     * Converts a byte array to hex string.
     *
     * @param bytes -
     *              the input byte array
     * @return hex string representation of b.
     */

    public static String toHexString(byte[] bytes) {
        final char[] chars = new char[bytes.length << 1];
        for (int i = 0; i < bytes.length; i++) {
            final byte b = bytes[i];
            chars[i << 1] = HEX_CHARS[b >>> 4 & 0X0F];
            chars[(i << 1) + 1] = HEX_CHARS[b & 0X0F];
        }
        return new String(chars);
    }

    /**
     * Converts a hex string into a byte array.
     *
     * @param s -
     *          string to be converted
     * @return byte array converted from s
     */
    public static byte[] toByteArray(String s) {
        byte[] buf = new byte[s.length() / 2];
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) ((Character.digit(s.charAt(j++), 16) << 4) | Character.digit(s.charAt(j++), 16));
        }
        return buf;
    }


}
