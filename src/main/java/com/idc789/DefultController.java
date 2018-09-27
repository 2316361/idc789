package com.idc789;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@RestController
public class DefultController {

    private static final String RSA_ALGORITHM = "RSA";

    private RSAPrivateKey rsaPrivateKey;

    @PostConstruct
    private void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        String privateKey = "MIIJQgIBADANBgkqhkiG9w0BAQEFAASCCSwwggkoAgEAAoICAQCqP3oeQhSpgswo\n" +
                "KOHfEfH9ti28el3wWwp0RlDcpSc6pdhcueNlzCPMvWXNR+faShoICNdGfo3KqILO\n" +
                "sLRttP3OYD++ecjTUkRhnU8D5sN3QLB4vnz0gJy8/lu0xB9rRtPrT9bRF6r9ez31\n" +
                "fVjYyoBUSbionLl+e1pt+ZmQ7oMkirUSMvfJQFEUYsoyKe+a3exQyQSkbAuRHUub\n" +
                "bkFMPf6HOCdicDhVOie/7QP3ErkAbysotHULaUMk1NXBwKV0ULmLRsNASTTZSPdn\n" +
                "fMCEg0nnDd+63cXlvFLHDHqEL2O44Pfqg5df0/YyCuckYm/RNslfS0G4XZhSSuKx\n" +
                "tUs6N/Qn/qlkOxDCLhu1Z6GEWK8GpAuaOoPCCxT1WpIOL+jpsca/1MhUHJixN7vz\n" +
                "MWLsaYCS8ANChGjGd56p2QURy3T+qhtwrUNsaQQT2r8/Hog3+HBeNACHlW3OGbpV\n" +
                "pOlDjpJyFFAMjgMixBj7NfifIJECdacSwchdvF5rEV+Vz3sAAQc1D8YIU3s2XUV8\n" +
                "RSewLxZwRFstZiWmAZLzK6iRAr2dCJ4fleC3X13z3ZvdJFUw5oZNITT9tA9IjNQL\n" +
                "8wXpo0tCk6oLkoXYxSlyI4R7UYuNMsctfh/wFNZOJyPawzUherEgCZk3GxvC5/+o\n" +
                "jevylPGrjN3+2cJVE78x4dyDTDdQqwIDAQABAoICAE3KDUlwFjLR8ejTrUkY0QKF\n" +
                "UGaTXwLpBmDUlDQNPlx81Nyhjza/TQhK7g4bMeQ7AaP0LjEmuDEGSCaa9QY/scyv\n" +
                "i9DDorB0IYycrTLyKRP/OeMh3sR1U6jDbDGvf0+sID6fz17gup9xxYC7EnKULIjW\n" +
                "2oooelVBlbM0Xier4745pry3DyMkwSdYcFz1bmbLyiOrhfR+7dYTla1idnGYzgkI\n" +
                "7m37KrI+jVUTvkERi/iX0xeZRPW+1hApM7aBCNiZ3f22ipaAHKa/wVPG28xwlL7/\n" +
                "RuACX182anhKylxFrwIVwRGlqO//ULnS44Rref4VU3O4VkIgee34C5b5aHMkh+lT\n" +
                "bOen6TrLJRoH4PknEQ1f924WVyodT6FsvbnftQpeCVpIwhDVpqyvy4Bl0RWeT/aT\n" +
                "yjM8j8jUTowGKB6eOdnTy+EbP3x5l8+3eiCsqzKEdMegfuqCsFtKKhSNuly2ntCb\n" +
                "UgJAfNoMkqymKq/Kvp6FvcVHR69Blbjeusoc4pdCMuILuXaRaWSrkQawMmTGk0aG\n" +
                "cck6pzlAe2pE+eSAznLfLduIXRu/i3SGrPLEOz2Gh4PGj2b2tj4mT+2lYL2fOdaw\n" +
                "26SuLfKiB1zn9/UBsp/Zkx2IuLYrRTnwFKrn7wBNHF2loWG08YcPq9b9eutN2UvO\n" +
                "cn7qaSZIxJEEIRpHtmVZAoIBAQDZ80IfkoNyUI4+GKScgwDDlAHeMNbmf1/7KsJk\n" +
                "NxF5Lkfu2F6qNEUsTRFeL1DEJKBteojF8uPdS9CNF0tLW9gGy4zbLWVsx6rB73P8\n" +
                "BiTg6r0oJGjuVZ9U1Dk/t7ynoU1M6Ci1LLLQiaCh24J/UyPc6mGJAqAo24n2q9VI\n" +
                "60/B3q1mzmZJQC0CgY685Vkn/r4qeKYE8FdWpy3xXosnZFG+svGkSbDfYCXUwXjC\n" +
                "9gb/QAtIzqtbv8uG4J1Joy58K/51BCuaGZAWUpYiCymOF8S9/l711CfvomXpzRuc\n" +
                "hC6z3NqFsI7W66OxSXwT4w/7F86kh3003OudisisGtbYNMu1AoIBAQDH+EjlU97C\n" +
                "XC90d3fJjtZPW6XjGvp+K+lWfFAVZXv31Ki/C3+k2kr5HGqxHwJ8sfOFUq2zUioA\n" +
                "23EntsgUOQ4sd168t435MtmgAcubUuHpk7v2/LD/e9jhoQDPLRyli81C98gx/o2f\n" +
                "Eo/kO21Nmm1lajYXCdH6+uUZ8+e3kRSqRrKAoPXsg7yRVhRZ9P9QrpW2VmwE5pa/\n" +
                "MYp+H8c0KAMG9OTDJ6GBRaQ6lB/BhbjGwPD/KMxsfjFZ6yg9qjWVW43tBtMWrCwY\n" +
                "ZNeDww4GLAA6a653aw4Vu/Z8+PSlS1MFOzHyVgn4XWW9xzktg2lPRd2POqIw5OSg\n" +
                "w1Fpgt6AKSbfAoIBACeq9qX4bKQptTnoEk3KKiFulshx6ysYk8eLCMZkn3GG0uDN\n" +
                "0Kz5xn1BqZ+SzQhmAizq2GnuwzpLyjFjw/h7Rt41vfVGVeduj82sX1fHZn7pOnRL\n" +
                "7C00uEMgPSszrcp0uTT+LCqNcB40bmAHK8EwyJ40qnvdVnkZPAsBS1hyO5n1hWno\n" +
                "twUuAQMrwWbv90IoQ0RHoS/U7pB+7QW5R2pr/9TNjN1x7Bc70Kbgb7JMRQnABk3C\n" +
                "D74rMkfiKb51TMic2TtC/wyVCnyFm32munF6MfzCh1YGvx4GWLYs9pGOHVHvKHac\n" +
                "o+LKEIj77vFPqmDw/FNFtawhaWDLGar5V5ESSuECggEAVoPi9eTdYXWEynzNhJSZ\n" +
                "v+fE04bejSvuXg6Wj0tC4SxOfak55lbTTQewWUUzTEOpOitVH1oS9h/6ytXXpngw\n" +
                "26ghgsBfWktXwFRGhMLNHsNd9TPKWB567NM9iGTaGf1sJucQ5CBfoUzkmDflyEdh\n" +
                "wRi8oicLg+x8pkfSHqdUK38+x0vcWiF8udxmHa1TsNJ3z1WAknY5Dise4gZZpwUQ\n" +
                "mjflqVFCHK3GqdlgC9gQ41yiZ6J9HwtRyrdkx5kWabIcq0nildKTmVI3s7f2H1F2\n" +
                "H9BqwRu+EkUqFVOYdNMr7f14VIJI+f1egUbqQ/iw3S06+DbD6Sd+itc2J9II9aXY\n" +
                "PQKCAQEAoKkBrmoTSIQnUkHTOQfDbe7YAMaWPonEE961oJPrDtaDUChUhxJX6NPR\n" +
                "zqTJG0fXIEs17Xoe8RDjCqHfN5AK85k/YskTplaXD7p86DM0kMCfU6KRWI6uNZlz\n" +
                "ehS5hdwiQsGjtTPp1p1l5A4NDVeSVJIC1W8uX+XVaKn7/XpwNb0dGHg69rsKb1uJ\n" +
                "3TEPHlh8kOipKv2M7b1o9VfcGuCL82X5h5zRzlKewkLnBGfBf6LWMTa4P4GNRE9T\n" +
                "QlzuQAeo33CPNKc/Xo6PRz/18d1EHy8yqQD9JRRRMN0JX4vAeni6QX7R+oYEYT3z\n" +
                "6XJwAOLpqC8pruDyXe+PxGqdlyInmA==";
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        this.rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    @GetMapping("/generate")
    public String operate(String jqm) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.rsaPrivateKey);
            result = new String(Base64.encodeBase64(cipher.doFinal(jqm.getBytes())));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return result;
    }
}
