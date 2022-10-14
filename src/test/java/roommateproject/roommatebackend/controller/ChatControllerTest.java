package roommateproject.roommatebackend.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import roommateproject.roommatebackend.dto.Message.MessageType;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, String> request = new HashMap<>();
    Map<String, Object> message = new HashMap<>();
    String token;

    @BeforeEach
    void setBody() throws Exception {
        request.put("email", "admin@admin.com");
        request.put("password", "123AAAAA12");

        MvcResult mvcResult = this.mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // 토큰 추출
        String responseBody = mvcResult.getResponse().getContentAsString();
        token = responseBody.split("\"")[7].split(" ")[0];
        System.out.println("token = " + token);

    }

    @Test
    public void messageImageTeset() throws Exception {
        File file = new File("src/test/resources/chatImage.jpg");
        byte[] image = Files.readAllBytes(file.toPath());
        message.put("type", MessageType.IMAGE);
        message.put("roomId", 434L);
        message.put("senderId", 26L);
        message.put("message", null);
        message.put("image", image);

        this.mockMvc.perform(post("/api/chat/image")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk());

    }
}