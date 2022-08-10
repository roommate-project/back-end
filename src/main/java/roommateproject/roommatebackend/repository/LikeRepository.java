package roommateproject.roommatebackend.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.dto.LikeDto;
import roommateproject.roommatebackend.entity.LikeIt;
import roommateproject.roommatebackend.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository @Slf4j
public class LikeRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(LikeIt likeIt){
        em.persist(likeIt);
        return likeIt.getId();
    }

    public List<LikeIt> find(User user){
        return em.createQuery("select l from LikeIt l where l.sender=:user",LikeIt.class)
                .setParameter("user", user)
                .getResultList();
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

    public int getReceiverCount(User user) {
        return em.createQuery("select l from LikeIt l where l.receiver=:user")
                .setParameter("user",user)
                .getResultList().size();
    }

    public List<LikeDto> getAllLike(User user, int start) {
        return em.createQuery("select new roommateproject.roommatebackend.dto.LikeDto(ui.storeFileName, l.receiver, u.home.id, u.home.location)" +
                                    " from User u join u.images ui join u.likeSender l" +
                                    " where u.id=:user")
                .setParameter("user",user.getId())
                .setFirstResult((start - 1) * 10)
                .setMaxResults(10)
                .getResultList();
    }
}
