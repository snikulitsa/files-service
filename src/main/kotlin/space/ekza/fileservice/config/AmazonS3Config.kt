package space.ekza.fileservice.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AmazonS3Properties::class)
class AmazonS3Config(
    private val amazonS3Properties: AmazonS3Properties
) {

    @Bean
    fun awsS3Client(): AmazonS3Client {
        val credentials = BasicAWSCredentials(
            amazonS3Properties.accessKey,
            amazonS3Properties.secretKey
        )
        val credentialsProvider = AWSStaticCredentialsProvider(credentials)

        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(credentialsProvider)
            .withRegion(Regions.EU_WEST_3)
            .build() as AmazonS3Client
    }
}

@ConfigurationProperties(prefix = "amazon.s3")
@ConstructorBinding
data class AmazonS3Properties(
    val bucket: String,
    val accessKey: String,
    val secretKey: String
)
