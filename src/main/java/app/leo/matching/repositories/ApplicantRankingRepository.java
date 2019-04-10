package app.leo.matching.repositories;


import app.leo.matching.models.ApplicantRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRankingRepository extends JpaRepository<ApplicantRanking,Long> {

    @Query(value= "select ar.* from applicant_rankings ar where ar.applicant_match_id = ?1 and ar.position_id = ?2" , nativeQuery = true)
    ApplicantRanking getApplicantRankingByApplicantIdandPositionId(long applicantId,long positionId);

    public List<ApplicantRanking> getApplicantRankingByApplicantMatchId(long applicantMatchId);
}
