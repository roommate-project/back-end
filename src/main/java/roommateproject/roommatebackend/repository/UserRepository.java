package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public User save(User user){
        em.persist(user);
        return user;
    }

    public User findOne(Long id){
        return em.find(User.class,id);
    }

    public Optional<User> findByEmail(String requestEmail) {
        return em.createQuery("select u from User u where u.loginId=:id",User.class)
                .setParameter("id",requestEmail)
                .getResultList()
                .stream().findAny();
    }
}
