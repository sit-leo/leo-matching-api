package app.leo.matching.services;

import app.leo.matching.models.*;
import app.leo.matching.repositories.ApplicantMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchingService {

    @Autowired
    private ApplicantMatchRepository applicantMatchRepository;

    public MatchingService(ApplicantMatchRepository applicantMatchRepository) {
        this.applicantMatchRepository = applicantMatchRepository;
    }

    public List<MatchResult> matchingByMatchId(long matchId) {
        List<ApplicantMatch> applicantMatches = applicantMatchRepository.getApplicantMatchByMatchId(matchId);
        return this.matching(applicantMatches);
    }

    public List<MatchResult> matching(List<ApplicantMatch> applicantMatches) {
        List<MatchResult> matchResults = new ArrayList<>();
        Map<Position, List<ApplicantMatch>> positionAccepted = new HashMap<>();
        while (!this.allApplicantMatched(applicantMatches)) {
            ApplicantMatch applicant = applicantMatches.remove(0);
            List<ApplicantRanking> applicantRanking = applicant.getApplicantRanking();
            while (!applicantRanking.isEmpty()) {
                Position position = applicantRanking.remove(0).getPosition();
                List<ApplicantMatch> acceptedApplicant = positionAccepted.get(position);
                if (acceptedApplicant == null) {
                    acceptedApplicant = new ArrayList<>();
                }
                if (this.isRecruiterAccepted(applicant, position.getRecruiterRankings())) {
                    if ((this.isRecruiterHasFullCapacity(position, acceptedApplicant))) {
                        int removalApplicantIndex = this.findRemovalApplicantInPositionRanking(position, applicant, positionAccepted);
                        if (this.isRankBetterThanPositionAccepted(removalApplicantIndex)) {
                            ApplicantMatch applicantMatch = acceptedApplicant.remove(removalApplicantIndex);
                            matchResults = this.removeAcceptedApplicantInMatchResult(matchResults, removalApplicantIndex);
                            positionAccepted.remove(position);
                            positionAccepted.put(position, acceptedApplicant);
                            matchResults.add(new MatchResult(applicant, position));
                            applicantMatches.add(applicantMatch);
                        } else if(applicantRanking.isEmpty()) {
                            matchResults.add(new MatchResult(applicant, null));
                            break;
                        }
                    } else {
                        matchResults.add(new MatchResult(applicant, position));
                        acceptedApplicant.add(applicant);
                        positionAccepted.put(position, acceptedApplicant);
                        break;
                    }
                } else if(applicantRanking.isEmpty()) {
                    matchResults.add(new MatchResult(applicant, null));
                    break;
                }
            }
        }
        return matchResults;
    }

    private boolean allApplicantMatched(List<ApplicantMatch> applicants){
        return applicants.isEmpty();
    }


    private boolean isRecruiterAccepted(ApplicantMatch applicant, List<RecruiterRanking> recruiterRanking){
        RecruiterRanking ranking = new RecruiterRanking(applicant);
        return recruiterRanking.contains(ranking);
    }

    private boolean isRecruiterHasFullCapacity(Position position, List<ApplicantMatch> applicantAccepted){
        int capacity = position.getCapacity();
        return applicantAccepted.size() == capacity || capacity == 0;
    }

    private boolean isRankBetterThanPositionAccepted (int indexOfApplicant) {
        return  indexOfApplicant > -1;
    }

    private boolean isIndexCorrected(int indexOfApplicant) {
        return  indexOfApplicant > -1;
    }

    private int findRemovalApplicantInPositionRanking(Position position, ApplicantMatch applicant, Map<Position, List<ApplicantMatch>> positionAccepted){
        int indexOfRemovalApplicant = -1;
        List<RecruiterRanking> recruiterRankings = new ArrayList<>(position.getRecruiterRankings());
        if (positionAccepted.get(position) != null) {
            List<ApplicantMatch> applicantAccepted = new ArrayList<>(positionAccepted.get(position));
            int indexOfNewApplicant = recruiterRankings.indexOf(applicant);
            if (this.isIndexCorrected(indexOfNewApplicant)) {
                int newApplicantSequence = recruiterRankings.get(indexOfNewApplicant).getSequence();
                for (ApplicantMatch applicantMatch: applicantAccepted) {
                    int indexOfOldApplicant = recruiterRankings.indexOf(applicantMatch);
                    int oldApplicantSequence = recruiterRankings.get(indexOfOldApplicant).getSequence();
                    if (oldApplicantSequence < newApplicantSequence) {
                        indexOfRemovalApplicant = indexOfOldApplicant;
                        break;
                    }
                }
            }
        }
        return indexOfRemovalApplicant;
    }

    private List<MatchResult> removeAcceptedApplicantInMatchResult(List<MatchResult> matchResults, int removalApplicantIndex) {
        matchResults.remove(removalApplicantIndex);
        return matchResults;
    }

}