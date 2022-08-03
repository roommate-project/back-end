package roommateproject.roommatebackend.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import roommateproject.roommatebackend.dto.MatchingDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MatchingQueryRepository {

    @PersistenceContext
    private EntityManager em;

    @Value("${spring.image.represent}")
    private String fileDir;

    private String location;
    private String gender;
    private int likeNumber;
    private int questionCount;
    private Long homeId;

    public List<MatchingDto> findAllUser(){
        return em.createQuery("select new roommateproject.roommatebackend.dto.MatchingDto(ui.storeFileName, u.nickName, h.info, h.location, u.gender ,h.id)" +
                                    " from User u join fetch UserImage ui join fetch Home h" +
                                    " where ui.represent = true")
                .getResultList();
    }
}
