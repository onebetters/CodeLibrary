package com.zzc.utils.aes256;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Administrator
 */
public class Aes256 extends AbstractAes256 {

    private final static Base64.Encoder encoder = Base64.getEncoder();
    private final static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * 加密
     */
    public static String encrypt(String input, String passphrase)
            throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        return encoder.encodeToString(_encrypt(input.getBytes(UTF_8), passphrase.getBytes(UTF_8)));
    }

    /**
     * 加密
     */
    public static byte[] encrypt(byte[] input, byte[] passphrase)
            throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        return encoder.encode(_encrypt(input, passphrase));
    }

    /**
     * 解密
     */
    public static String decrypt(String crypted, String passphrase)
            throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        return new String(_decrypt(decoder.decode(crypted.getBytes(UTF_8)), passphrase.getBytes(UTF_8)), UTF_8);
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] crypted, byte[] passphrase)
            throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        return _decrypt(decoder.decode(crypted), passphrase);
    }
}