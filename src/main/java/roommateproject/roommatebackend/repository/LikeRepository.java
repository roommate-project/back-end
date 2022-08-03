package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class LikeRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(LikeIt likeIt){
        em.persist(likeIt);
        return likeIt.getId();
    }

    public LikeIt find(User user){
        return em.createQuery("select l from LikeIt l where l.sender=:user",LikeIt.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public void erase(LikeIt likeIt) {
        LikeIt findLike = em.createQuery("select l from LikeIt l where l.sender=:sender and l.receiver=:receiver",LikeIt.class)
                .setParameter("sender",likeIt.getSender())
                .setParameter("receiver",likeIt.getReceiver())
                .getSingleResult();
        em.remove(findLike);
    }

    public boolean exists(LikeIt likeIt) {
        return !em.createQuery("select l from LikeIt l where l.sender=:sender and l.receiver=:receiver",LikeIt.class)
                    .setParameter("sender",likeIt.getSender())
                    .setParameter("receiver",likeIt.getReceiver())
                    .getResultList().isEmpty();
    }
}
