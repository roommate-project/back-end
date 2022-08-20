package roommateproject.roommatebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

  //  private String register;
    private int age;

}
