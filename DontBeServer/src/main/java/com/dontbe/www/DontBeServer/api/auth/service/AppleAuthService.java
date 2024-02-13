package com.dontbe.www.DontBeServer.api.auth.service;

import com.dontbe.www.DontBeServer.api.auth.dto.SocialInfoDto;
import com.dontbe.www.DontBeServer.common.exception.BaseException;
import com.dontbe.www.DontBeServer.common.exception.UnAuthorizedException;
import com.dontbe.www.DontBeServer.common.response.ErrorStatus;
import com.google.gson.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleAuthService {
    public SocialInfoDto login(String socialAccessToken, String userName) {
        return getAppleSocialData(socialAccessToken, userName);
    }

    private JsonArray getApplePublicKeys() {
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
            return (JsonArray) keys.get("keys"); // 1. 공개키 가져오기
        } catch (IOException e) {
            throw new UnAuthorizedException(ErrorStatus.FAILED_TO_VALIDATE_APPLE_LOGIN.getMessage());
        }
    }

    private SocialInfoDto getAppleSocialData(String socialAccessToken, String userName) {
        try {
            JsonArray publicKeyList = getApplePublicKeys();
            PublicKey publicKey = makePublicKey(socialAccessToken, publicKeyList);

            Claims userInfo = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(socialAccessToken.substring(7))
                    .getBody();

            JsonObject userInfoObject = (JsonObject) JsonParser.parseString(new Gson().toJson(userInfo));
            String appleId = userInfoObject.get("sub").getAsString();
            String email = userInfoObject.get("email").getAsString();

            return new SocialInfoDto(appleId, userName, null, email);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "애플 계정 데이터 가공 실패");
        }
    }

    private PublicKey makePublicKey(String identityToken, JsonArray publicKeyList) throws NoSuchAlgorithmException, InvalidKeySpecException {
        JsonObject selectedObject = null;

        String[] decodeArray = identityToken.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0].substring(7)));

        JsonElement kid = ((JsonObject) JsonParser.parseString(header)).get("kid");
        JsonElement alg = ((JsonObject) JsonParser.parseString(header)).get("alg");

        for (JsonElement publicKey : publicKeyList) {
            JsonObject publicKeyObject = publicKey.getAsJsonObject();
            JsonElement publicKid = publicKeyObject.get("kid");
            JsonElement publicAlg = publicKeyObject.get("alg");

            if (Objects.equals(kid, publicKid) && Objects.equals(alg, publicAlg)) {
                selectedObject = publicKeyObject;
                break;
            }
        }

        if (selectedObject == null) {
            throw new InvalidKeySpecException("공개키를 찾을 수 없습니다.");
        }

        return getPublicKey(selectedObject);
    }

    private PublicKey getPublicKey(JsonObject object) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);


        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return publicKey;
    }

}
