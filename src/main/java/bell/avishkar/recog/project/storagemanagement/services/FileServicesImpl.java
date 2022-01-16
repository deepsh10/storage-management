package bell.avishkar.recog.project.storagemanagement.services;

import bell.avishkar.recog.project.storagemanagement.config.AwsRekognitionWhitelistCollection;
import bell.avishkar.recog.project.storagemanagement.config.BucketName;
import bell.avishkar.recog.project.storagemanagement.config.RekognitionClientFactory;
import bell.avishkar.recog.project.storagemanagement.domain.Individual;
import bell.avishkar.recog.project.storagemanagement.domain.S3BucketStats;
import bell.avishkar.recog.project.storagemanagement.repository.IndividualRepository;
import com.amazonaws.services.kafka.model.S3;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

@Service
@AllArgsConstructor
public class FileServicesImpl implements FileServices {

    private final IndividualRepository individualRepository;
    private final FileStore fileStore;
    private final RekognitionCollectionServices rekognitionCollectionServices;

    @Override
    public Individual saveIndividual(String firstName, String middleName, String lastName, String idType, String identification, MultipartFile multipartFile) {

        if(multipartFile.isEmpty()){
            throw new IllegalStateException("Cannot upload empty file");
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type",multipartFile.getContentType());
        metadata.put("Content-Length",String.valueOf(multipartFile.getSize()));
        metadata.put("firstName",firstName);
        metadata.put("middleName",middleName);
        metadata.put("lastName",lastName);
        metadata.put("idType",idType);
        metadata.put("identification",identification);
        String path = String.format("%s", BucketName.INDIVIDUAL_IMAGE.getBucketName());
        String fileName = String.format("%s", multipartFile.getOriginalFilename());

        try{
            fileStore.upload(path,fileName,Optional.of(metadata), multipartFile.getInputStream());
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }

        try{
            rekognitionCollectionServices.createMyCollection(AwsRekognitionWhitelistCollection.INDIVIDUAL_WHITELIST_COLLECTION.getCollectionName());
        }
        catch (ResourceAlreadyExistsException e){
            rekognitionCollectionServices.deleteMyCollection(AwsRekognitionWhitelistCollection.INDIVIDUAL_WHITELIST_COLLECTION.getCollectionName());
            rekognitionCollectionServices.createMyCollection(AwsRekognitionWhitelistCollection.INDIVIDUAL_WHITELIST_COLLECTION.getCollectionName());
            System.out.println("Collection "+ AwsRekognitionWhitelistCollection.INDIVIDUAL_WHITELIST_COLLECTION.getCollectionName() + " already exists!");
        }

        String collectionId = AwsRekognitionWhitelistCollection.INDIVIDUAL_WHITELIST_COLLECTION.getCollectionName();
        String externalImageId = UUID.randomUUID().toString();
        rekognitionCollectionServices.addToCollection(collectionId, BucketName.INDIVIDUAL_IMAGE.getBucketName(),externalImageId, fileName);

        System.out.println("Full path: "+fileName);

        Individual individual = Individual
                                    .builder()
                                    .firstName(firstName)
                                    .middleName(middleName)
                                    .lastName(lastName)
                                    .identifier(identification)
                                    .identifierType(idType)
                                    .fileName(fileName)
                                    .externalImageId(externalImageId)
                                    .build();
        individualRepository.save(individual);
        return individualRepository.findByIdentifierAndIdentifierType(individual.getIdentifier(),individual.getIdentifierType());
    }

    @Override
    public Individual identifyIndividual(MultipartFile multipartFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Get an image object from S3 bucket.
        Image image=new Image().withBytes(ByteBuffer.wrap(multipartFile.getInputStream().readAllBytes()));

        // Search collection for faces similar to the largest face in the image.
        SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
                .withCollectionId(AwsRekognitionWhitelistCollection.INDIVIDUAL_WHITELIST_COLLECTION.getCollectionName())
                .withImage(image)
                .withFaceMatchThreshold(70F)
                .withMaxFaces(2);

        SearchFacesByImageResult searchFacesByImageResult =
                RekognitionClientFactory.createClient().searchFacesByImage(searchFacesByImageRequest);

        System.out.println("Faces matching largest face in image from" + image);
        List < FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();

        Individual result = new Individual();
        for (FaceMatch face: faceImageMatches) {
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
            result = individualRepository.findByExternalImageId(face.getFace().getExternalImageId());
        }

        return result;
    }

    @Override
    public List<S3BucketStats> fetchWhitelistCollectionStats() {

        AmazonRekognition rekognitionClient = RekognitionClientFactory.createClient();

        int limit = 10;
        ListCollectionsResult listCollectionsResult = null;

        List<S3BucketStats> s3BucketStatsList = new ArrayList<>();

        String paginationToken = null;
        do {
            if (listCollectionsResult != null) {
                paginationToken = listCollectionsResult.getNextToken();
            }
            ListCollectionsRequest listCollectionsRequest = new ListCollectionsRequest()
                    .withMaxResults(limit)
                    .withNextToken(paginationToken);
            listCollectionsResult=rekognitionClient.listCollections(listCollectionsRequest);

            List < String > collectionIds = listCollectionsResult.getCollectionIds();

            for(String collectionId : collectionIds){
                DescribeCollectionRequest request = new DescribeCollectionRequest()
                        .withCollectionId(collectionId);

                DescribeCollectionResult describeCollectionResult = rekognitionClient.describeCollection(request);

                S3BucketStats result = new S3BucketStats();

                result.setBucketName(AwsRekognitionWhitelistCollection.INDIVIDUAL_WHITELIST_COLLECTION.getCollectionName());
                result.setNumberOfImages(describeCollectionResult.getFaceCount());

                s3BucketStatsList.add(result);
            }

        } while (listCollectionsResult != null && listCollectionsResult.getNextToken() != null);

        return s3BucketStatsList;
    }
}