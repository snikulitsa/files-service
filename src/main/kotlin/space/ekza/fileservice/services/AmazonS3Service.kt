package space.ekza.fileservice.services

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GroupGrantee
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.Permission
import com.amazonaws.services.s3.model.PutObjectResult
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import space.ekza.fileservice.config.AmazonS3Properties
import space.ekza.fileservice.model.AmazonS3File
import space.ekza.fileservice.util.IdGenerator
import java.io.ByteArrayInputStream

@Service
class AmazonS3Service(
    private val awsS3Client: AmazonS3,
    private val properties: AmazonS3Properties,
    private val idGenerator: IdGenerator
) {

    fun savePublic(file: ByteArray, rootUuid: String, extension: String): AmazonS3File {
        val key = "${rootUuid}_${idGenerator.generate()}.$extension"
        save(key, file)
        allowReadAccessToAll(key)
        val url = awsS3Client.getUrl(properties.bucket, key).toExternalForm()
        val amazonS3File = AmazonS3File(
            key = key,
            bucket = properties.bucket,
            url = url
        )
        logger.info("File saved to amazon: $amazonS3File")
        return amazonS3File
    }

    private fun save(key: String, file: ByteArray): PutObjectResult = awsS3Client.putObject(
        properties.bucket,
        key,
        ByteArrayInputStream(file),
        ObjectMetadata()
    )

    private fun allowReadAccessToAll(key: String) {
        val objectAcl = awsS3Client.getObjectAcl(properties.bucket, key)
        objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read)
        awsS3Client.setObjectAcl(properties.bucket, key, objectAcl)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AmazonS3Service::class.java)
    }
}
