package app.leo.matching.controllers;


import app.leo.matching.DTO.*;
import app.leo.matching.models.RecruiterRanking;
import app.leo.matching.services.RecruiterRankingService;
import app.leo.matching.validator.CreateRecruiterRankingRequest;
import app.leo.matching.validator.PutRecruiterRankingRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
public class RecruiterRankingController {

    @Autowired
    private RecruiterRankingService recruiterRankingService;

    @PostMapping("/matches/{matchId:[\\d]}/positions/{positionId:[\\d]}/ranking")
    public ResponseEntity<List<CreateRecruiterRankingRequest>>createRecruiterRanking(@PathVariable long matchId,@PathVariable long positionId,@Valid @RequestBody List<CreateRecruiterRankingRequest>createRecruiterRankingRequestList){
        for(CreateRecruiterRankingRequest recruiterRankingRequest:createRecruiterRankingRequestList) {
            recruiterRankingService.createRecruiterRanking(matchId,recruiterRankingRequest.getParticipantId(),positionId,recruiterRankingRequest.getSequence());
        }
        return  new ResponseEntity<>(createRecruiterRankingRequestList,HttpStatus.CREATED);
    }

    @PutMapping(path ="/matches/{matchId:[\\d]}/positions/{positionId:[\\d]}/ranking")
    public ResponseEntity<List<PutRecruiterRankingRequest>> updateRecruiterRanking(@PathVariable long matchId,@PathVariable long positionId ,@Valid @RequestBody List<PutRecruiterRankingRequest> recruiterRankingRequestList){
        recruiterRankingService.updateRecuiterRankingByMatchIdAndPositionId(matchId,positionId,recruiterRankingRequestList);
      return new ResponseEntity<>(recruiterRankingRequestList,HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/matches/{matchId:[\\d]}/recruiters/positions/{positionId:[\\d]}/ranking")
    public ResponseEntity<List<GetRankingResponse>> getPositionRankingByPositionId (@PathVariable long matchId,@PathVariable long positionId){
        List<RecruiterRanking> recruiterRankingList = recruiterRankingService.getRecruiterRankingByMatchIdAndPositionId(matchId,positionId);
       return new ResponseEntity<>(mapApplicantRankingtoResponse(recruiterRankingList),HttpStatus.OK);
    }


    private List<GetRankingResponse> mapApplicantRankingtoResponse(List<RecruiterRanking> recruiterRankingList){
        ModelMapper modelMapper =new ModelMapper();
        List<GetRankingResponse> responses = new ArrayList<>();

        //mock data
        for(RecruiterRanking recruiterRanking :recruiterRankingList){
            GetRankingResponse getRankingResponse = modelMapper.map(recruiterRanking,GetRankingResponse.class);
            GetPositionsByMatchIdResponse position  = getRankingResponse.getPosition();
            mockRecruiterInstall(position);
            GetApplicantsByMatchIdResponse applicant = getRankingResponse.getApplicantMatch();
            mockApplicantInstall(applicant, applicant.getApplicantId());
            getRankingResponse.setApplicantMatch(applicant);
            getRankingResponse.setPosition(position);
            responses.add(getRankingResponse);
        }
        return  responses;
    }

    private void mockRecruiterInstall(GetPositionsByMatchIdResponse position){//mock data method
        Recruiter recruiter = null;
        if(position.getId() < 3) {
            recruiter = new Recruiter(1L, "Microsoft word co., Ltd", "Phayathai, BKK");
        }else
            recruiter = new Recruiter(2L,"Facebook co,. Ltd","San francisco,USA");
        position.setRecruiter(recruiter);
    }

    private void mockApplicantInstall(GetApplicantsByMatchIdResponse applicant,long applicantId){//mock data method
        List<Education> educations = new ArrayList<>();
        educations.add(new Education(1, "School of Information Technology", "4.00"));
        Applicant[] mockApplicant ={
                new Applicant(1, "Tae Keerati", educations),
                new Applicant(2, "Volk Natchanon", educations),
                new Applicant(3,"Jill Jirapa",educations)
        };
        if(applicantId%2==0){
            applicant.setApplicant(mockApplicant[1]);
        }else if(applicantId%3 == 0){
            applicant.setApplicant(mockApplicant[2]);
        }else
            applicant.setApplicant(mockApplicant[0]);

    }

}
