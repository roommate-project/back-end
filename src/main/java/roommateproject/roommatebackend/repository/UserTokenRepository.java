package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.entity.UserToken;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserTokenRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long save(UserToken userToken){
        em.persist(userToken);
        return userToken.getId();
    }

    @Transactional(readOnly = true)
    public Optional<UserToken> find(User user){
        return em.createQuery("select ut from UserToken ut where ut.userId = :id",UserToken.class)
                    .setParameter("id",user.getId())
                .getResultList().stream().findAny();
    }

    @Transactional
    public UserToken update(Long id, String[] splitToken) {
        UserToken findUserToken = em.createQuery("select ut from UserToken ut where ut.userId = :id",UserToken.class)
                                .setParameter("id",id)
                                .getSingleResult();
        UserToken updateToken = new UserToken();
        updateToken.setId(findUserToken.getId());
        updateToken.setUserId(id);
        updateToken.setAccessToken(splitToken[0]);
        updateToken.setRefreshToken(splitToken[1]);
        em.merge(updateToken);
        return updateToken;
    }

    public Optional<UserToken> findByToken(String accessToken, String refreshToken) {
        return em.createQuery("select ut from UserToken ut where ut.accessToken = :access and ut.refreshToken = :refresh",UserToken.class)
                .setParameter("access",accessToken)
                .setParameter("refresh",refreshToken)
                .getResultList().stream().findAny();
    }

    @Transactional
    public void remove(String accessToken, String refreshToken) {
        em.createQuery("select ut from UserToken ut where ut.accessToken = :accessToken or ut.refreshToken = :refreshToken",UserToken.class)
                .setParameter("accessToken",accessToken)
                .setParameter("refreshToken",refreshToken)
                .getResultList()
                .forEach(ut -> em.remove(ut));
    }
}
