package space.ekza.fileservice.model

data class FileProcessingPreparationMetadata (
    val fileUUID: String,
    val originalFileName: String,
    val originalFileExtension: String,
    val pathToTemporaryOriginalFile: String,
    val pathToTemporaryConvertedFile: String
)
