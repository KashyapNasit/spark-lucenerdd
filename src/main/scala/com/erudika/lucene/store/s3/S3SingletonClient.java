package com.erudika.lucene.store.s3;

import com.amazonaws.ClientConfiguration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3SingletonClient {
    private static S3Client s3Client;

    private S3SingletonClient(){

    }

    public static S3Client getS3Client(){
        if(s3Client == null){

            ClientConfiguration clientConfiguration = new ClientConfiguration()
                    .withMaxErrorRetry(10)
                    .withMaxConnections(1000);

            s3Client = S3Client.builder()
                    .credentialsProvider(() -> new AwsSessionCredentials
                            .Builder()
//                            .accessKeyId("") //This is from E2E account
//                            .secretAccessKey("")
//                            .sessionToken("")
                            .build()
                    )
                    .region(Region.US_WEST_2)
                    .build();
        }
        return s3Client;
    }
}
