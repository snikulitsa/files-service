package space.ekza.fileservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ekza-files")
data class RootFileEntity (

    @Id
    val uuid: String,
    val originalFile: FileEntity,
    val renderReadyFile: FileEntity
)

data class FileEntity (
    val uuid: String,
    val key: String,
    val url: String,
    val bucket: String
)
