package roommateproject.roommatebackend.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import roommateproject.roommatebackend.dto.EmailValidateDto;

import java.util.Date;

@Getter
public class ResponseMessage {

    private int code;
    private boolean status;
    private String message;
    private Date timestamp;

    public ResponseMessage(){
    }

    public ResponseMessage(EmailValidateDto emailValidateDto){
        this.code = emailValidateDto.getCode();
        this.status = emailValidateDto.isValidate();
        this.message = emailValidateDto.getMessage();
        this.timestamp = new Date();

    }
}
