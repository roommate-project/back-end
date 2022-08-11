package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

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

    public User change(long id, String password, String nickName, String name) {
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
        em.merge(user);
        return user;
    }
}
