package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserImage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ImageRepository {

    @PersistenceContext
    private EntityManager em;

    public UserImage getRepresentImage(User user){
        return em.createQuery("select ui from UserImage ui where ui.user = :user and ui.represent = true",UserImage.class)
                .setParameter("user",user).getSingleResult();
    }

    public List<UserImage> getRestImage(User user){
        return em.createQuery("select ui from UserImage  ui where ui.user = :user and ui.represent = false", UserImage.class)
                .setParameter("user", user).getResultList();
    }

    public List<UserImage> getAllImage(User user){
        return em.createQuery("select ui from UserImage  ui where ui.user = :user", UserImage.class)
                .setParameter("user", user).getResultList();
    }

    @Transactional
    public UserImage save(UserImage userImage){
        em.persist(userImage);
        return userImage;
    }

    @Transactional
    public void change(User user, UserImage userImage) {
        UserImage findImage = em.createQuery("select ui from UserImage ui where ui.user = :user and ui.represent=true",UserImage.class)
                                    .setParameter("user", user).getSingleResult();
        userImage.setId(findImage.getId());
        em.merge(userImage);
    }

    @Transactional
    public void remove(Long id) {
        UserImage userImage = em.find(UserImage.class, id);
        em.remove(userImage);
    }
}
