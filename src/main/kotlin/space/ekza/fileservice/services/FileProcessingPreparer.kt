package space.ekza.fileservice.services

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.config.FileConvertingProperties
import space.ekza.fileservice.model.FileProcessingPreparationMetadata
import java.util.UUID

@Component
class FileProcessingPreparer(
    private val fileConvertingProperties: FileConvertingProperties
) {
    fun prepare(multipartFile: MultipartFile): FileProcessingPreparationMetadata {

        val uuid = UUID.randomUUID().toString()
        val fullOriginalFileName = multipartFile.originalFilename!!
        val originalFileName = fullOriginalFileName.substring(0, fullOriginalFileName.lastIndexOf('.'))
        val originalFileExtension = fullOriginalFileName.substring(fullOriginalFileName.lastIndexOf('.') + 1)
        val pathToTemporaryOriginalFile = with(fileConvertingProperties) {
            "$rawFilesFolder/$originalFileName-$uuid.$originalFileExtension"
        }
        val pathToTemporaryConvertedFile = with(fileConvertingProperties) {
            "$convertedFilesFolder/$originalFileName-result-$uuid.glb"
        }
        return FileProcessingPreparationMetadata(
            fileUUID = uuid,
            originalFileName = originalFileName,
            originalFileExtension = originalFileExtension,
            pathToTemporaryOriginalFile = pathToTemporaryOriginalFile,
            pathToTemporaryConvertedFile = pathToTemporaryConvertedFile
        )
    }
}
