package roommateproject.roommatebackend.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roommateproject.roommatebackend.entity.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service @Slf4j
public class KakaoOauthService {

    @Value("${spring.password.kakao}")
    private String password;
    @Value(("${spring.kakao.client}"))
    private String client;

    public String getKakaoAccessToken(String code, String redirectURL){
        String accessToken = "";
        String refreshToken = "";
        String requestURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + client);
            sb.append("&redirect_uri="+redirectURL);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();

            log.info("responseCode in Access Token : {}",responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonElement element = JsonParser.parseString(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Kakao Access token : {}",accessToken);
        return accessToken;
    }

    public Map<String, Object> createKakaoUser(String token){

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> kakaoUser = new HashMap<>();

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            log.info("responsCode in create User : {}",conn.getResponseCode());

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            boolean hasGender = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_gender").getAsBoolean();

            if(!hasEmail || !hasGender){
                return null;
            }

            String email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            String name = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();
            String nickName = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();
            String profileURL = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("profile_image_url").getAsString();
            String gender = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("gender").getAsString();
            br.close();
            User user = new User(email,name,password,nickName,gender,"kakao",26);
            kakaoUser.put("user",user);
            kakaoUser.put("image",profileURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kakaoUser;
    }


    public String findKakaoEmail(String token){

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> kakaoUser = new HashMap<>();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            int responseCode = conn.getResponseCode();

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            if(!hasEmail){
                return null;
            }

            return element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}