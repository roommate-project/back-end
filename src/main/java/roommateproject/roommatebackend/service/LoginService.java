package roommateproject.roommatebackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.UserRepository;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@Transactional(readOnly = true)
public class LoginService {

    private final UserRepository userRepository;

    @Value("${spring.encrypt.password}")
    private String encrypt;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long login(String email, String password) throws NoSuchAlgorithmException {

        if(email == null){
            throw new IllegalArgumentException("이메일 항목이 없음");
        }
        if(password == null){
            throw new IllegalArgumentException("비밀번호 항목이 없음");
        }
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저 이메일"));
        if(!findUser.getRegister().equals("email")){
            throw new IllegalArgumentException("카카오, 네이버로 로그인");
        }
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(encrypt.getBytes());
        byte[] digested = md.digest(password.getBytes());
        String encryptPassword = String.format("%064x",new BigInteger(1,digested));
        if(!encryptPassword.equals(findUser.getPassword())){
            throw new IllegalArgumentException("비밀번호 불일치");
        }
        return findUser.getId();
    }

    public Long kakaoLogin(String email){
        return userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저 이메일"))
                    .getId();
    }

    public Long naverLogin(String email) {
        return userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저 이메일"))
                    .getId();
    }
}
