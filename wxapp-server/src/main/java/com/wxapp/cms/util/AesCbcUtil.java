package com.wxapp.cms.util;

import com.jfinal.core.Const;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.codehaus.xfire.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;

/**
 * AES-128-CBC 加密方式
 * AES-128-CBC可以自己定义“密钥”和“偏移量“。
 * AES-128是jdk自动生成的“密钥”。
 *
 * @author zxy
 */
public final class AesCbcUtil {

    static {
        // BouncyCastle 是一个开源的加解密解决方案，主页在 http://www.bouncycastle.org/ 
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * AES解密
     *
     * @param sessionKey    秘钥
     * @param encryptedData 密文数据
     * @param iv            偏移量
     * @return String
     */
    public static String decrypt(String sessionKey, String encryptedData, String iv) {
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 秘钥不足16位补足
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, Const.DEFAULT_ENCODING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
