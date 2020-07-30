package space.ekza.fileservice.entity

data class RootFileEntity (
    val uuid: String,
    val originalFile: FileEntity,
    val renderReadyFile: FileEntity
)

data class FileEntity (
    val uuid: String,
    val url: String,
    val bucket: String
)
