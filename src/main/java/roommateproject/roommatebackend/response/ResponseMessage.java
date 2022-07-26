package roommateproject.roommatebackend.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import roommateproject.roommatebackend.dto.EmailValidateDto;
import roommateproject.roommatebackend.entity.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Getter
public class ResponseMessage {

    private int code;
    private boolean status;
    private String message;
    private Date timestamp;
    private Long id;

    public ResponseMessage(){
    }

    public ResponseMessage(String token, Long id) {
        this.code = HttpStatus.OK.value();
        this.status = true;
        this.message = token;
        this.timestamp = new Date();
        this.id = id;
    }

    public ResponseMessage(int code, boolean status, String message, Date timestamp){
        this.code = code;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ResponseMessage(EmailValidateDto emailValidateDto){
        this.code = emailValidateDto.getCode();
        this.status = emailValidateDto.isValidate();
        this.message = emailValidateDto.getMessage();
        this.timestamp = new Date();

    }

    public ResponseMessage(User user) {
        this.code = HttpStatus.OK.value();
        this.status = true;
        this.message = "회원정보 수정 완료";
        this.timestamp = new Date();
    }

    public ResponseMessage(MethodArgumentNotValidException e) {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.status = false;
        this.message = "유효성 검사 실패 : " +  e.getBindingResult().getAllErrors().get(0);
        this.timestamp = new Date();
    }

    public ResponseMessage(NoSuchAlgorithmException e) {
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.status = false;
        this.message = "비밀번호 저장 실패 : " + e.getMessage();
        this.timestamp = new Date();
    }

    public ResponseMessage(IllegalArgumentException e) {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.status = false;
        this.message = "에러 : " + e.getMessage();
        this.timestamp = new Date();
    }

    public ResponseMessage(NullPointerException e) {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.status = false;
        this.message = "에러 : " + e.getMessage();
        this.timestamp = new Date();
    }

    public ResponseMessage(IOException e) {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.status = false;
        this.message = "에러 : " + e.getMessage();
        this.timestamp = new Date();
    }
}
