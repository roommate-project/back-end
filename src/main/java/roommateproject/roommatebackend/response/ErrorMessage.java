package roommateproject.roommatebackend.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorMessage {

    private int code;
    private String errorMessage;
    private String referredUrl;

    public ErrorMessage(int code, String referredUrl){
        this.code = code;
        this.errorMessage = "no error";
        this.referredUrl = referredUrl;
    }

    public ErrorMessage(boolean check, int code, String errorMessage, String referredUrl){
        this.code = code;
        this.errorMessage = errorMessage;
        this.referredUrl = referredUrl;
    }
}
