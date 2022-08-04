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

    public int getQuestionCount(Long homeId, User user) {
        int questionCount = 0;
        Home findHome = em.find(Home.class, homeId);
        Home userHome = em.createQuery("select h from Home h where h.user=:user",Home.class)
                                    .setParameter("user",user)
                                    .getSingleResult();
        if(findHome.getQuestion1() && userHome.getQuestion1())questionCount++;
        if(findHome.getQuestion2() && userHome.getQuestion2())questionCount++;
        if(findHome.getQuestion3() && userHome.getQuestion3())questionCount++;
        if(findHome.getQuestion4() && userHome.getQuestion4())questionCount++;
        if(findHome.getQuestion5() && userHome.getQuestion5())questionCount++;
        if(findHome.getQuestion6() && userHome.getQuestion6())questionCount++;
        return questionCount;
    }
}
