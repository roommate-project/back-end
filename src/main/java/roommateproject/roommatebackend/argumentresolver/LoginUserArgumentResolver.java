package roommateproject.roommatebackend.argumentresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.service.UserService;
import roommateproject.roommatebackend.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public LoginUserArgumentResolver(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("LoginUserArgumentResolver supportsParameter 실행");

        return parameter.hasParameterAnnotation(Login.class)
                && User.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest req = (HttpServletRequest)webRequest.getNativeRequest();
        String[] requestToken = req.getHeader("authorization").split(" ");
        if(requestToken == null || requestToken.length != 2){
            return null;
        }
        Long id = Long.parseLong(jwtTokenProvider.getInformation(requestToken[1]).get("jti").toString());

        return userService.find(id);
    }
}
