package bell.avishkar.recog.project.storagemanagement.services;

import bell.avishkar.recog.project.storagemanagement.config.BucketName;
import bell.avishkar.recog.project.storagemanagement.config.RekognitionClientFactory;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RekognitionCollectionServices {

    public void createMyCollection(String collectionId ) {

        CreateCollectionRequest request = new CreateCollectionRequest().withCollectionId(collectionId);

        AmazonRekognition rekognition = RekognitionClientFactory.createClient();
        CreateCollectionResult result = rekognition.createCollection(request);

    }

    public void deleteMyCollection(String collectionId) {

        DeleteCollectionRequest request = new DeleteCollectionRequest().withCollectionId(collectionId);
        DeleteCollectionResult deleteCollectionResult = RekognitionClientFactory.createClient().deleteCollection(request);

    }

    public void addToCollection(String collectionId, String bucket, String externalImageId, String filename) {

        AmazonRekognition rekognition = RekognitionClientFactory.createClient();

        Image image = new Image().withS3Object(new S3Object()
                .withBucket(bucket)
                .withName(filename));

        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(image)
                .withQualityFilter(QualityFilter.AUTO)
                .withMaxFaces(1)
                .withCollectionId(collectionId)
                .withExternalImageId(externalImageId)
                .withDetectionAttributes("DEFAULT");

        IndexFacesResult indexFacesResult = rekognition.indexFaces(indexFacesRequest);

    }

}
