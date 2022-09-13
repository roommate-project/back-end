package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
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

    public List<User> findFilter(User loginUser, int pageNumber, String gender, int wantLongMax, int wantLongMin, int ageMax, int ageMin, int costMax, int costMin, boolean room0,boolean room1,boolean room2,boolean room3,boolean room4) {
        if(gender.equals("all")){
            if(room4){
                return em.createQuery("select u from User u" +
                                " where u<>:user and u.age between :ageMin and :ageMax" +
                                " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                " and u.home.cost between :costMin and :costMax" +
                                " and u.home.room = 0", User.class)
                        .setParameter("user",loginUser)
                        .setParameter("ageMin",ageMin)
                        .setParameter("ageMax",ageMax)
                        .setParameter("wantLongMax",wantLongMax)
                        .setParameter("wantLongMin",wantLongMin)
                        .setParameter("costMax",costMax)
                        .setParameter("costMin",costMin)
                        .setFirstResult((pageNumber - 1) * 10)
                        .setMaxResults(10)
                        .getResultList();           //all, room4
            }else if(room0){
                if(room1 && room2 && room3){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room <> 0", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room1 && room2){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room = 1 or u.home.room = 2 or u.home.room = -1", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room1 && room3){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room = 1 or u.home.room >= 3 or u.home.room = -1", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room2 && room3){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room >= 2 or u.home.room = -1", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room1){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room = 1 or u.home.room = -1", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room2){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room = 2 or u.home.room = -1", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room3){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room >= 3 or u.home.room = -1", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();               //all, room0
                }
            }else{
                if(room1 && room2 && room3){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room > 0", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room1 && room2){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room = 1 or u.home.room = 2", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room1 && room3){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room = 1 or u.home.room >= 3", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room2 && room3){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room >= 2", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room1){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room = 1", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room2){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room = 2", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }else if(room3){
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.home.room >= 3", User.class)
                            .setParameter("user",loginUser)
                            .setParameter("ageMin",ageMin)
                            .setParameter("ageMax",ageMax)
                            .setParameter("wantLongMax",wantLongMax)
                            .setParameter("wantLongMin",wantLongMin)
                            .setParameter("costMax",costMax)
                            .setParameter("costMin",costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }
            }                   //all, room0 X
        }else {
            if (room4) {
                return em.createQuery("select u from User u" +
                                " where u<>:user and u.age between :ageMin and :ageMax" +
                                " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                " and u.home.cost between :costMin and :costMax" +
                                " and u.gender=:gender and u.home.room = 0", User.class)
                        .setParameter("user", loginUser)
                        .setParameter("ageMin", ageMin)
                        .setParameter("ageMax", ageMax)
                        .setParameter("gender", gender)
                        .setParameter("wantLongMax", wantLongMax)
                        .setParameter("wantLongMin", wantLongMin)
                        .setParameter("costMax", costMax)
                        .setParameter("costMin", costMin)
                        .setFirstResult((pageNumber - 1) * 10)
                        .setMaxResults(10)
                        .getResultList();           // all X, room 4
            } else if (room0) {
                if (room1 && room2 && room3) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room <> 0", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room1 && room2) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room = 1 or u.home.room = 2 or u.home.room = -1", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room1 && room3) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room = 1 or u.home.room >= 3 or u.home.room = -1", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room2 && room3) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room >= 2 or u.home.room = -1", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room1) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room = 1 or u.home.room = -1", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room2) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room = 2 or u.home.room = -1", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room3) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room >= 3 or u.home.room = -1", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                }                       // all X, room0
            } else {
                if (room1 && room2 && room3) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room > 0", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room1 && room2) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room = 1 or u.home.room = 2", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room1 && room3) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room = 1 or u.home.room >= 3", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room2 && room3) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room >= 2", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room1) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room = 1", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room2) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room = 2", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();
                } else if (room3) {
                    return em.createQuery("select u from User u" +
                                    " where u<>:user and u.age between :ageMin and :ageMax" +
                                    " and u.home.want_long between :wantLongMin and :wantLongMax" +
                                    " and u.home.cost between :costMin and :costMax" +
                                    " and u.gender=:gender and u.home.room >= 3", User.class)
                            .setParameter("user", loginUser)
                            .setParameter("ageMin", ageMin)
                            .setParameter("ageMax", ageMax)
                            .setParameter("gender", gender)
                            .setParameter("wantLongMax", wantLongMax)
                            .setParameter("wantLongMin", wantLongMin)
                            .setParameter("costMax", costMax)
                            .setParameter("costMin", costMin)
                            .setFirstResult((pageNumber - 1) * 10)
                            .setMaxResults(10)
                            .getResultList();           //all X, room0 X
                    
                }
            }
        }
        return null;
    }
}
