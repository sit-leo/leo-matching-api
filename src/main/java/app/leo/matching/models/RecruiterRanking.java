package app.leo.matching.models;


import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "recruiter_rankings")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RecruiterRanking extends Ranking {

    @ManyToOne
    @JoinColumn(name = "position_id")
    @JsonBackReference
    private Position position;

    @ManyToOne
    @JoinColumn(name = "applicant_match_id")
    @JsonBackReference(value = "applicantMatch-RecruiterRanking")
    private ApplicantMatch applicantMatch;

    public RecruiterRanking() {
    }

    public RecruiterRanking(ApplicantMatch applicantMatch,Position position) {
        this.position = position;
        this.applicantMatch = applicantMatch;
    }

    public RecruiterRanking(@NotNull int sequence,long matchId, Position position, ApplicantMatch applicantMatch) {
        super(sequence, matchId);
        this.position = position;
        this.applicantMatch = applicantMatch;
    }

    public RecruiterRanking(ApplicantMatch applicantMatch) {
        this.applicantMatch = applicantMatch;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ApplicantMatch getApplicantMatch() {
        return applicantMatch;
    }

    public void setApplicantMatch(ApplicantMatch applicantMatch) {
        this.applicantMatch = applicantMatch;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof RecruiterRanking) {
            RecruiterRanking recruiterRanking = (RecruiterRanking) o;
            ApplicantMatch applicant = recruiterRanking.getApplicantMatch();
            return applicant.equals(this.getApplicantMatch());
        }
        return false;
    }

    @Override
    public String toString() {
        return "RecruiterRanking{" +
                "position=" + position +
                ", applicantMatch=" + applicantMatch +
                '}';
    }
}
