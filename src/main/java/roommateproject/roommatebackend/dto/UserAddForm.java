package roommateproject.roommatebackend.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserAddForm {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    private String nickName;
//    @NotBlank
//    private MultipartFile representImage;
//    @Nullable
//    private List<MultipartFile> images;

}
