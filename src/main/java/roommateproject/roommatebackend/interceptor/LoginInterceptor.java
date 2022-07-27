package roommateproject.roommatebackend.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqURI = request.getRequestURI();
        String[] requestToken = request.getHeader("authorization").split(" ");

        log.info("{} 인터셉터 인증 시작",reqURI);

        if(requestToken.length != 2){
            log.info("{} 인터셉터 인증 실패 : {}",reqURI,requestToken.length);
            response.resetBuffer();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Content-Type", "application/json");
            response.getOutputStream().print("{\"code\":\"401\",");
            response.getOutputStream().print("\"status\":\"false\",");
            response.getOutputStream().print("\"message\":\"login first\",");
            response.getOutputStream().print("\"timestamp\":\"");
            response.getOutputStream().print(new Date().toString());
            response.getOutputStream().print("\"}");
            response.flushBuffer();
            return false;
        }

        if(!jwtTokenProvider.validateToken(requestToken[1])){
            log.info("test : {}",requestToken[1]);
            response.resetBuffer();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Content-Type", "application/json");
            response.getOutputStream().print("{\"code\":\"401\",");
            response.getOutputStream().print("\"status\":\"false\",");
            response.getOutputStream().print("\"message\":\"login again : token is not validate\",");
            response.getOutputStream().print("\"timestamp\":\"");
            response.getOutputStream().print(new Date().toString());
            response.getOutputStream().print("\"}");
            response.flushBuffer();
            return false;
        }

        log.info("{} - {} = {}",jwtTokenProvider.getInformation(requestToken[1]).get("time"),new Date(), (long)jwtTokenProvider.getInformation(requestToken[1]).get("time") - new Date().getTime());

        log.info("test : {}",jwtTokenProvider.getInformation(requestToken[1]));
        return true;
    }
}
