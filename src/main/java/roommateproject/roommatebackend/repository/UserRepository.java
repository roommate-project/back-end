package roommateproject.roommatebackend.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Value("${spring.encrypt.password}")
    private String encrypt;

    public User save(User user, UserImage userImage){
        List<UserImage> userImageList = new ArrayList<>();
        userImageList.add(userImage);
        user.setImages(userImageList);
        userImage.setUser(user);
        em.persist(user);
        return user;
    }

    public Optional<User> findByEmail(String requestEmail) {
        return em.createQuery("select u from User u where u.email=:id",User.class)
                .setParameter("id",requestEmail)
                .getResultList()
                .stream().findAny();
    }

    public long erase(User user) {
        em.remove(user);
        return user.getId();
    }

    public User find(Long id) {
        return em.find(User.class, id);
    }

    public User change(long id, String password, String nickName, String name, int age, String gender) {
        User user = new User();
        User findUser = find(id);
        user.setId(id);
        user.setEmail(findUser.getEmail());
        user.setGender(findUser.getGender());
        user.setRegister(findUser.getRegister());
        user.setAge(findUser.getAge());
        user.setPassword(password);
        user.setName(name);
        user.setNickName(nickName);
        user.setGender(gender);
        user.setAge(age);
        em.merge(user);
        return user;
    }

    public void changeAge(User loginUser, String age) {
        User findUser = em.find(User.class, loginUser.getId());
        findUser.setAge(Integer.parseInt(age));
        em.merge(findUser);
    }

    public void changeGender(User loginUser, String gender) {
        User findUser = em.find(User.class, loginUser.getId());
        findUser.setGender(gender);
        em.merge(findUser);
    }

    public void changeNickName(User loginUser, String nickName) {
        User findUser = em.find(User.class, loginUser.getId());
        findUser.setNickName(nickName);
        em.merge(findUser);
    }

    public void changeName(User loginUser, String name) {
        User findUser = em.find(User.class, loginUser.getId());
        findUser.setName(name);
        em.merge(findUser);
    }

    public void changePassword(User loginUser, String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
        md.update(encrypt.getBytes());
        byte[] digested = md.digest(password.getBytes());
        password = String.format("%064x",new BigInteger(1,digested));

        User findUser = em.find(User.class, loginUser.getId());
        findUser.setPassword(password);
        em.merge(findUser);
    }
}
