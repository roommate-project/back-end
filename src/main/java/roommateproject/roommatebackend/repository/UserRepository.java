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

    public void erase(String email) {
        User findUser = findByEmail(email).get();
        em.remove(findUser);
    }
}
