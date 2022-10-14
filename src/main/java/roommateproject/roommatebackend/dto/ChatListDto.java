package roommateproject.roommatebackend.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatListDto {

    private UserInfo userInfo;
    private List<ChatDto> chats;

    public ChatListDto(UserInfo userInfo,
            List<ChatDto> chats) {
        this.userInfo = userInfo;
        this.chats = chats;
    }
}
