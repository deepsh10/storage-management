package bell.avishkar.recog.project.storagemanagement.controller;


import bell.avishkar.recog.project.storagemanagement.domain.CollectionStats;
import bell.avishkar.recog.project.storagemanagement.domain.Individual;
import bell.avishkar.recog.project.storagemanagement.domain.S3BucketStats;
import bell.avishkar.recog.project.storagemanagement.services.FileServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/individual")
@AllArgsConstructor
@CrossOrigin("*")
public class FileController {

    FileServices fileServices;

    @PostMapping(path="",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Individual> saveIndividual(@RequestParam("firstName") String firstName,
                                                     @RequestParam("middleName") String middleName,
                                                     @RequestParam("lastName") String lastName,
                                                     @RequestParam("identification") String identification,
                                                     @RequestParam("collectionType") String collectionType,
                                                     @RequestBody MultipartFile file){
        return new ResponseEntity<Individual>(fileServices.saveIndividual(firstName,middleName,lastName,"companyId",identification,collectionType,file), HttpStatus.ACCEPTED);
    }

    @PostMapping(path="/verify")
    public ResponseEntity<Individual> identifyIndividual(@RequestBody MultipartFile file,
                                                         @RequestParam("collectionType") String collectionType) throws IOException {
        return new ResponseEntity<Individual>(fileServices.identifyIndividual(collectionType,file), HttpStatus.OK);
    }

    @GetMapping(path="/white-collection-stat",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CollectionStats>> fetchCollectionStats()
    {
        return new ResponseEntity<>(fileServices.fetchCollectionStats(), HttpStatus.OK);
    }

    @GetMapping(path="/clear-every-thing")
    public ResponseEntity<String> clearEverything()
    {
        return new ResponseEntity<String>(fileServices.clearAll(),HttpStatus.OK);
    }

}
