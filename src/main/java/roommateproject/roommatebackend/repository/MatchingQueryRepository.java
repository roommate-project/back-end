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

    public List<User> findFilter(User loginUser, int pageNumber, String gender, int wantLongMax, int wantLongMin, int ageMax, int ageMin, int costMax, int costMin, int roomMax, int roomMin) {
        if(gender.equals("all")){
            return em.createQuery("select u from User u" +
                                        " where u<>:user and u.age between :ageMin and :ageMax" +
                                        " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                        " and u.home.cost between :costMin and :costMax" +
                                        " and u.home.room between :roomMin and :roomMax", User.class)
                    .setParameter("user",loginUser)
                    .setParameter("ageMin",ageMin)
                    .setParameter("ageMax",ageMax)
                    .setParameter("wantLongMax",wantLongMax)
                    .setParameter("wantLongMin",wantLongMin)
                    .setParameter("costMax",costMax)
                    .setParameter("costMin",costMin)
                    .setParameter("roomMax",roomMax)
                    .setParameter("roomMin",roomMin)
                    .setFirstResult((pageNumber - 1) * 10)
                    .setMaxResults(10)
                    .getResultList();
        }
        return em.createQuery("select u from User u" +
                        " where u<>:user and u.age between :ageMin and :ageMax" +
                        " and u.home.want_long between :wantLongMin and :wantLongMax" +
                        " and u.home.cost between :costMin and :costMax" +
                        " and u.home.room between :roomMin and :roomMax" +
                        " and u.gender=:gender", User.class)
                .setParameter("user",loginUser)
                .setParameter("ageMin",ageMin)
                .setParameter("ageMax",ageMax)
                .setParameter("gender",gender)
                .setParameter("wantLongMax",wantLongMax)
                .setParameter("wantLongMin",wantLongMin)
                .setParameter("costMax",costMax)
                .setParameter("costMin",costMin)
                .setParameter("roomMax",roomMax)
                .setParameter("roomMin",roomMin)
                .setFirstResult((pageNumber - 1) * 10)
                .setMaxResults(10)
                .getResultList();
    }
}
