package space.ekza.fileservice.model

data class AmazonS3File (
    val uuid: String,
    val key: String,
    val bucket: String,
    val url: String
)
