package bell.avishkar.recog.project.storagemanagement.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RekognitionClientFactory {

    @Bean
    public static AmazonRekognition createClient() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(30000);
        clientConfig.setRequestTimeout(60000);
        clientConfig.setProtocol(Protocol.HTTPS);

        AWSCredentials awsCredentials = new BasicAWSCredentials();

        return AmazonRekognitionClientBuilder
                .standard()
                .withClientConfiguration(clientConfig)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

}
