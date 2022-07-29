package roommateproject.roommatebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.dto.EmailValidateDto;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.UserRepository;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Value("${spring.encrypt.password}")
    private String encrypt;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public long join(User user) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(encrypt.getBytes());
        byte[] digested = md.digest(user.getPassword().getBytes());
        user.setPassword(String.format("%064x",new BigInteger(1,digested)));
        User saveUser = userRepository.save(user);
        return saveUser.getId();
    }


    public EmailValidateDto validateEmail(String requestEmail) {
        if(requestEmail.length() > 50){
            return new EmailValidateDto(false,"이메일 길이는 50자 이하",HttpStatus.BAD_REQUEST);
        }
        String regx = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regx);
        if(!pattern.matcher(requestEmail).matches()){
            return new EmailValidateDto(false,"이메일 형식 오류",HttpStatus.BAD_REQUEST);
        }

        Optional<User> findUser = userRepository.findByEmail(requestEmail);
        if(findUser.isPresent()){
            return new EmailValidateDto(false,"이메일 중복", HttpStatus.BAD_REQUEST);
        }
        return new EmailValidateDto(true, "정상 처리", HttpStatus.OK);
    }

}
