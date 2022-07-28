package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserImageRepository {

    @PersistenceContext
    private EntityManager em;

    public UserImage save(UserImage userImage){
        em.persist(userImage);
        return userImage;
    }

    public UserImage findByUser(User user){
        return em.find(UserImage.class, user);
    }
}
