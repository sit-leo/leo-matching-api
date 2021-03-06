package app.leo.matching.services;


import app.leo.matching.models.ApplicantMatch;
import app.leo.matching.repositories.ApplicantMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantMatchService {

    @Autowired
    private ApplicantMatchRepository applicantMatchRepository;

    public ApplicantMatchService() {
    }

    public ApplicantMatchService(ApplicantMatchRepository applicantMatchRepository) {
        this.applicantMatchRepository =applicantMatchRepository;
    }

    public List<ApplicantMatch> getApplicantMatchesByMatchIdandPositionId(long matchId, long positionId){
        return applicantMatchRepository.getApplicantMatchByMatchIdAndPositionId(matchId,positionId);
    }

    public ApplicantMatch getApplicantMatchByParticipantId(long participantId){
        return applicantMatchRepository.getApplicantMatchByParticipantId(participantId);
    }

    public ApplicantMatch getApplicantMatchByApplicantIdAndMatchId(long applicantId, long matchId){
        return applicantMatchRepository.getApplicantMatchByApplicantIdAndMatchId(applicantId, matchId);
    }

    public List<ApplicantMatch> getApplicantMatchByMatchId(long matchId) {
        return this.applicantMatchRepository.getApplicantMatchByMatchId(matchId);
    }

    public List<ApplicantMatch> getApplicantMatchByApplicantId(long applicantId){
        return this.applicantMatchRepository.getApplicantMatchByApplicantId(applicantId);
    }

    public ApplicantMatch saveApplicantMatch(ApplicantMatch applicantMatch){
        return this.applicantMatchRepository.save(applicantMatch);
    }

    public int countApplicantMatchByMatchId(long matchId){
        return this.applicantMatchRepository.countApplciantMatchByMatchId(matchId);
    }
}
