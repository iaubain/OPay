package com.oltranz.opay.utilities.keys;

import android.os.Build;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/9/2017.
 */

public class KeyGen {
    public static final String genId() {
        String serialNumber = Build.SERIAL;
        if(serialNumber == null)
            return new Date().getTime()+"";
        try {
            UUID uuid = UUID.randomUUID();
            long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
            String generatedSeed = Long.toString(l, Character.MAX_RADIX);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(generatedSeed.getBytes());
            String generatedId = hexEncode(result);
            return serialNumber+"_"+generatedId;
        } catch (Exception e) {
            e.printStackTrace();
            return serialNumber+"_"+new Date().getTime();
        }
    }

    public static final String genUuId() {
        String serialNumber = Build.SERIAL;
        if(serialNumber == null)
            return new Date().getTime()+"";
        try {
            UUID uuid = UUID.randomUUID();
            return uuid.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return serialNumber+"_"+new Date().getTime();
        }
    }

    static private String hexEncode(byte[] aInput) {
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }
}
