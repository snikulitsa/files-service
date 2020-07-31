package space.ekza.fileservice.model

data class FileProcessingMetadata (
    val fileUUID: String,
    val originalFileName: String,
    val originalFileExtension: String,
    val targetFileExtension: String,
    val pathToTemporaryOriginalFile: String,
    val pathToTemporaryConvertedFile: String
)
