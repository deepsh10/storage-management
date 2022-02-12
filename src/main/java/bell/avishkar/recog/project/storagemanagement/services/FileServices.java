package bell.avishkar.recog.project.storagemanagement.services;

import bell.avishkar.recog.project.storagemanagement.domain.CollectionStats;
import bell.avishkar.recog.project.storagemanagement.domain.Individual;
import bell.avishkar.recog.project.storagemanagement.domain.S3BucketStats;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileServices {
    Individual saveIndividual(String firstName, String middleName, String lastName, String idType, String identification, String collectionType, MultipartFile multipartFile);

    Individual identifyIndividual(String collectionType,MultipartFile file) throws IOException;

    List<CollectionStats> fetchCollectionStats();

    String clearAll();
}
