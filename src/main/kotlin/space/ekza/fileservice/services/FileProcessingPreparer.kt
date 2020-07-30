package space.ekza.fileservice.services

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.config.FileConvertingProperties
import space.ekza.fileservice.model.FileProcessingPreparationMetadata
import space.ekza.fileservice.util.IdGenerator

@Component
class FileProcessingPreparer(
    private val fileConvertingProperties: FileConvertingProperties,
    private val idGenerator: IdGenerator
) {
    fun prepare(multipartFile: MultipartFile): FileProcessingPreparationMetadata {

        val uuid = idGenerator.generate()
        val fullOriginalFileName = multipartFile.originalFilename!!
        val originalFileName = fullOriginalFileName.substring(0, fullOriginalFileName.lastIndexOf('.'))
        val originalFileExtension = fullOriginalFileName.substring(fullOriginalFileName.lastIndexOf('.') + 1)
        val targetExtension = "glb"
        val pathToTemporaryOriginalFile = with(fileConvertingProperties) {
            "$rawFilesFolder/$originalFileName-$uuid.$originalFileExtension"
        }
        val pathToTemporaryConvertedFile = with(fileConvertingProperties) {
            "$convertedFilesFolder/$originalFileName-result-$uuid.$targetExtension"
        }
        return FileProcessingPreparationMetadata(
            fileUUID = uuid,
            originalFileName = originalFileName,
            originalFileExtension = originalFileExtension,
            targetFileExtension = targetExtension,
            pathToTemporaryOriginalFile = pathToTemporaryOriginalFile,
            pathToTemporaryConvertedFile = pathToTemporaryConvertedFile
        )
    }
}
