package bell.avishkar.recog.project.storagemanagement.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {

    INDIVIDUAL_IMAGE("bellavishkar-s3master");
    private final String bucketName;
}
