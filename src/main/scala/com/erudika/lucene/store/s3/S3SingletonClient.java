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
                            .accessKeyId("ASIA2E3GK2GH3NV2SO6E") //This is from E2E account
                            .secretAccessKey("FxjTfKJY6QfDyo3iYaG7oKg4hx0nqqRbJ53rhdMy")
                            .sessionToken("IQoJb3JpZ2luX2VjEA0aCXVzLXdlc3QtMiJHMEUCIQDEq/6ShmSHnXVPjINNeg1Gbfv1sic9rqF/xtQSe+nTmwIgRcH7qqqfJ79k28NMGnG+sG89KG80t1xWQENUT2QhzhMqoAMItv//////////ARAAGgw2OTc2MDk5MzMxOTkiDALDMrROyEiDFN4QBCr0ApoQMhYaD/zLX2NO4jF+Y3wXQy5Yr6IERyfHN1nOyeS69forL62SgVNaaKQlY7IjUyTlQQdfOxjCYNMIzOnzZnRXeQwqoB0YFl2ot9e80n8o9/vybPOFewtyI/VfBp4kHcqBKlL+6ZSvdHcrv5sNr87Hao9ztf7vDO8s8TGIvm74vbz901OaQLx77Jmt7n0j26CxWnBROIx9JqRQN5ZJQEcWpHD2bKnt4JqwIDfPH2EswcaAjNQut61hqUgQLqjoBrXBDVl3hztHJByQ72FDMTNq6VhkIZwrbTOGPpfFi+fr6xCCpmb+Nr1GSOjB3OwOUOS52No9Tz6vBJCIes3+Bh7J4o/GX1TUp5YewmIrKGCgIiWaGl+/Rl88KMzU9V+p/lzhNaUnT3OL91YDppfQgtqBB1nolfRIw4JEtObySweXpuf5zi514ygFmYp5X+pM7D51Rdc5cUAO0SXOjhn6Ig+wLWeUU+smsi96J7B6ReeSO+wmZTDHmumzBjqmAUrMHymm0/LKKQe579EaAo0hm3eSORXoH1BFFQmBX5LmaaHrAagczQqheXG+JkSR3AWXsenAZ3GpvcxKDcobFmTDi0DH6skyWZrXiy+TnTrGrTVlXlCaNuNp6uMuuhpHZF/eeczjj5nMgBtja9A8LWecSUMuS60Mz7pmT/C5CDM67+buEeu8YwqWeEmR1uZRNya8CTVyeb1X94XsVNlok1fAU/U1/D4=")
                            .build()
                    )
                    .region(Region.US_WEST_2)
                    .build();
        }
        return s3Client;
    }
}
