package bell.avishkar.recog.project.storagemanagement.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FileStore {

    private final AmazonS3 amazonS3;

    public void upload(String path, String filename, Optional<Map<String,String>> optionalMetaData, InputStream inputStream)
    {
        ObjectMetadata objectMetaData = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if(!map.isEmpty()){
                map.forEach(objectMetaData::addUserMetadata);
            }
        });

        try{
            amazonS3.putObject(path,filename,inputStream,objectMetaData);
        }
        catch (AmazonServiceException e)
        {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

}
