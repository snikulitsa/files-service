package space.ekza.fileservice.services

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.dto.ProcessingResponse

@Service
class FilesProcessingService(
    private val fileProcessingPreparer: FileProcessingPreparer,
    private val fileConverter: FileConverter,
    private val amazonS3Service: AmazonS3Service
) {
    fun process(multipartFile: MultipartFile): ProcessingResponse = try {

        val processingMetadata = fileProcessingPreparer.prepare(multipartFile)
        val convertedFile = fileConverter.convert(multipartFile, processingMetadata)
        fileConverter.cleanUp(processingMetadata)

        val originalS3File = amazonS3Service.savePublic(
            file = convertedFile.originalData,
            rootUuid = processingMetadata.fileUUID,
            extension = processingMetadata.originalFileExtension
        )
        val convertedS3File = amazonS3Service.savePublic(
            convertedFile.convertedData,
            processingMetadata.fileUUID,
            extension = processingMetadata.targetFileExtension
        )

        //TODO save to MongoDB

        ProcessingResponse.success()
    } catch (ex: Exception) {
        //TODO better failures processing
        ProcessingResponse.error(ex.message ?: "Unknown error")
    }
}
