package roommateproject.roommatebackend.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class EmailValidateDto {

    private boolean validate;
    private String message;
    private int code;

    public EmailValidateDto(boolean validate, String message, HttpStatus httpStatus) {
        this.validate = validate;
        this.message = message;
        this.code = httpStatus.value();
    }
}
