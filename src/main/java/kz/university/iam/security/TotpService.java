
package kz.university.iam.security;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class TotpService {

    private static final int TIME_STEP = 30;
    private static final int CODE_DIGITS = 6;

    public String generateSecret() {
        byte[] secret = new byte[20];
        new SecureRandom().nextBytes(secret);

        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(secret);
    }

    public boolean verifyCode(String secret, String code) {
        if (secret == null || code == null
                || !code.matches("\\d{6}")) {
            return false;
        }

        long currentStep =
                System.currentTimeMillis() / 1000 / TIME_STEP;

        for (long step = currentStep - 1;
             step <= currentStep + 1; step++) {

            if (generateCode(secret, step).equals(code)) {
                return true;
            }
        }

        return false;
    }

    private String generateCode(String secret, long step) {
        try {
            byte[] key = Base64.getDecoder().decode(secret);

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));

            byte[] hash = mac.doFinal(
                    ByteBuffer.allocate(8).putLong(step).array()
            );

            int offset = hash[hash.length - 1] & 0x0F;

            int binary =
                    ((hash[offset] & 0x7F) << 24)
                    | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8)
                    | (hash[offset + 3] & 0xFF);

            int otp = binary % (int) Math.pow(10, CODE_DIGITS);

            return String.format("%06d", otp);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "TOTP кодын жасау мүмкін болмады", e
            );
        }
    }
}
