package bell.avishkar.recog.project.storagemanagement.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AwsRekognitionWhitelistCollection {

    INDIVIDUAL_WHITELIST_COLLECTION("aws-whitelist-collection"),INDIVIDUAL_BLACKLIST_COLLECTION("aws-blacklist-collection");
    private final String collectionName;

}