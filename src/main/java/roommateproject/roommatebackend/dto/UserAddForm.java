package roommateproject.roommatebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class UserAddForm {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    private String nickName;
    @NotNull
    private String gender;

    private String register;
//    @NotBlank
  //  private MultipartFile representImage;
 //   @Nullable
   // private List<MultipartFile> images;

}
