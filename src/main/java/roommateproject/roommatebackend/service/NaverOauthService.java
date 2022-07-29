package roommateproject.roommatebackend.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roommateproject.roommatebackend.entity.User;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service @Slf4j
public class NaverOauthService {

    @Value("${spring.password.naver}")
    private String password;
    @Value("${spring.naver.client}")
    private String clientId;
    @Value("${spring.naver.secret}")
    private String clientSecret;

    public String getNaverAccessToken(String code, String redirectURL) {
        String accessToken = "";
        String refreshToken = "";
        String requestURL = "https://nid.naver.com/oauth2.0/token";
        SecureRandom rd = new SecureRandom();
        String state = new BigInteger(130, rd).toString(32);

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + clientId);
            sb.append("&client_secret=" + clientSecret);
            sb.append("&redirect_uri=" + redirectURL);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();
            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            br.close();
            bw.close();

            JsonElement element = JsonParser.parseString(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public Map<String, Object> createNaverUser(String naverToken) {
        String header = "Bearer " + naverToken; // Bearer 다음에 공백 추가

        String apiURL = "https://openapi.naver.com/v1/nid/me";
        Map<String, Object> naverUser = new HashMap<>();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);
        JsonElement element = JsonParser.parseString(responseBody);

        String email = element.getAsJsonObject().get("response").getAsJsonObject().get("email").toString();
        String nickName = element.getAsJsonObject().get("response").getAsJsonObject().get("nickname").toString();
        String name = element.getAsJsonObject().get("response").getAsJsonObject().get("name").toString();
        String profileURL = element.getAsJsonObject().get("response").getAsJsonObject().get("profile_image").toString();
        String gender = element.getAsJsonObject().get("response").getAsJsonObject().get("gender").toString();
        email = email.substring(1,email.length() - 1);
        nickName = nickName.substring(1,nickName.length() - 1);
        name = name.substring(1,name.length() - 1);
        profileURL = profileURL.substring(1, profileURL.length() - 1);
        gender = gender.substring(1, gender.length() - 1);

        User user = new User(email,name,password,nickName,gender,"naver");

        naverUser.put("user",user);
        naverUser.put("image",profileURL);

        return naverUser;
    }

    public String findNaverEmail(String naverToken){

        String header = "Bearer " + naverToken; // Bearer 다음에 공백 추가

        String apiURL = "https://openapi.naver.com/v1/nid/me";
        Map<String, Object> naverUser = new HashMap<>();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);
        JsonElement element = JsonParser.parseString(responseBody);

        String email = element.getAsJsonObject().get("response").getAsJsonObject().get("email").toString();

        if(email.isEmpty() || email == null){
            return null;
        }
        return  email.substring(1,email.length() - 1);
    }

    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
