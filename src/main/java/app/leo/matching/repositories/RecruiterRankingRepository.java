package app.leo.matching.repositories;

import app.leo.matching.models.RecruiterRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RecruiterRankingRepository extends JpaRepository<RecruiterRanking,Long> {

    @Query(value = "select rr.* from recruiter_rankings rr where rr.position_id = ?1 and rr.user_id = ?2",nativeQuery = true)
    RecruiterRanking getRecruiterRankingbyPositionIdandUserId(long positionId,long userId);

    @Transactional
    void deleteRecruiterRankingByMatchIdAndPositionId(long matchId, long positionId);

    List<RecruiterRanking> getRecruiterRankingByMatchIdAndPositionIdOrderBySequenceAsc(long matchId,long positionId);
}
