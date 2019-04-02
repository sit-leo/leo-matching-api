package app.leo.matching.controllers;

import app.leo.matching.models.Position;
import app.leo.matching.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PositionController {

    @Autowired
    private PositionService positionService;


    @GetMapping(path= "match/{matchId:[\\d]}/position")
    public List<Position> getPositionsByMatchId(@PathVariable long matchId){
        return positionService.getPositionByMatchId(matchId);
    }
}