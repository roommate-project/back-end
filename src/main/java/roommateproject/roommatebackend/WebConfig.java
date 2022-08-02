package roommateproject.roommatebackend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roommateproject.roommatebackend.interceptor.LoginInterceptor;
import roommateproject.roommatebackend.token.JwtTokenProvider;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/api/login/**","/api/user/**","/oauth/kakao","/favicon.ico");
    }
}
