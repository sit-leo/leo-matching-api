package app.leo.matching.controllers;

import app.leo.matching.DTO.*;
import app.leo.matching.models.DocumentPosition;
import app.leo.matching.models.Position;
import app.leo.matching.services.DocumentPositionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DocumentController {

    @Autowired
    private DocumentPositionService documentPositionService;

    @PostMapping("/positions/documents")
    public ResponseEntity<List<DocumentPosition>> createDocumentPositions(@RequestBody CreateDocumentPositionRequest[] createDocumentPositionRequests,
                                                                          @RequestAttribute("user") User user){
        List<DocumentPosition> documentPositions = new ArrayList<>();
        for(CreateDocumentPositionRequest request:createDocumentPositionRequests){
            DocumentPosition documentPosition = new DocumentPosition();
            List<Long> filesId = extractFileIdFromFiles(request.getFiles());
            documentPosition.setFilesId(filesId);
            documentPosition.setPosition(convertDTOToPosition(request.getPosition()));
            documentPosition.setUserId(user.getUserId());
            documentPositions.add(documentPosition);
        }
        return new ResponseEntity<>( documentPositionService.createDocumentPositions(documentPositions), HttpStatus.ACCEPTED);
    }

    private Position convertDTOToPosition(PositionDTO positionDTO){
        ModelMapper modelMapper = new ModelMapper();
        Position position =modelMapper.map(positionDTO,Position.class);
        return position;
    }

    private List<Long> extractFileIdFromFiles(List<FilesDTO> files){
        List<Long> filesId = new ArrayList<>();
        for(FilesDTO file:files){
            filesId.add(file.getId());
        }
        return filesId;
    }
}
