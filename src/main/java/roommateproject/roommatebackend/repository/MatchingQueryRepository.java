package roommateproject.roommatebackend.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    public List<MatchingDto> findAllUser(Long id, int pageNumber){

        return em.createQuery("select new roommateproject.roommatebackend.dto.MatchingDto(ui.storeFileName, u.nickName, h.info, h.location, u.gender, u.age ,h.id, u.id, ui.id)" +
                                    " from User u join u.images ui join u.home h" +
                                    " where u.id<>:id", MatchingDto.class)
                .setParameter("id",id)
                .setFirstResult((pageNumber - 1) * 10)
                .setMaxResults(10)
                .getResultList();

    }
}
