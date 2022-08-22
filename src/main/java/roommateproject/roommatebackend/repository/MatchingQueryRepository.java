package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.dto.MatchingDto;
import roommateproject.roommatebackend.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MatchingQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<User> findAllUserPagination(User user, int pageNumber){

        return em.createQuery("select u from User u where u.id<>:id", User.class)
                .setParameter("id",user.getId())
                .setFirstResult((pageNumber - 1) * 10)
                .setMaxResults(10)
                .getResultList();

    }

    public List<User> findFilter(User loginUser, int pageNumber, String gender, int experienceMax, int experienceMin, int ageMax, int ageMin) {
        if(gender.equals("all")){
            return em.createQuery("select u from User u" +
                                        " where u<>:user and u.age between :ageMin and :ageMax" +
                                        " and u.home.experience between :experienceMin and :experienceMax", User.class)
                    .setParameter("user",loginUser)
                    .setParameter("ageMin",ageMin)
                    .setParameter("ageMax",ageMax)
                    .setParameter("experienceMax",experienceMax)
                    .setParameter("experienceMin",experienceMin)
                    .setFirstResult((pageNumber - 1) * 10)
                    .setMaxResults(10)
                    .getResultList();
        }
        return em.createQuery("select u from User u" +
                        " where u<>:user and u.age between :ageMin and :ageMax" +
                        " and u.home.experience between :experienceMin and :experienceMax" +
                        " and u.gender=:gender", User.class)
                .setParameter("user",loginUser)
                .setParameter("ageMin",ageMin)
                .setParameter("ageMax",ageMax)
                .setParameter("gender",gender)
                .setParameter("experienceMax",experienceMax)
                .setParameter("experienceMin",experienceMin)
                .setFirstResult((pageNumber - 1) * 10)
                .setMaxResults(10)
                .getResultList();
    }
}
