package az.gaming_cafe.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class CommonUtils {

    public static long calcTokenExpiration(long time) {
        return time / 1000;
    }

    public static boolean isExpired(LocalDateTime expiryDate) {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
