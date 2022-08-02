package roommateproject.roommatebackend.repository;

import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.dto.UserHomeDto;
import roommateproject.roommatebackend.entity.Home;
import roommateproject.roommatebackend.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class HomeRepository {

    @PersistenceContext
    private EntityManager em;

    public Home find(User user){
        return em.createQuery("select h from Home h where h.user=:user",Home.class)
                .setParameter("user",user).getSingleResult();
    }

    public Long save(Home home) {
        em.persist(home);
        return home.getId();
    }

    public Long change(User user, Home home, UserHomeDto userHomeDto) {
        Home editHome = new Home(user, userHomeDto);
        editHome.setId(home.getId());
        em.merge(editHome);
        return editHome.getId();
    }
}
