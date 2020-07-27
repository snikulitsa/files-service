package space.ekza.fileservice.services

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.dto.ProcessingResponse

@Service
class FilesProcessingService(
    private val fileProcessingPreparer: FileProcessingPreparer,
    private val fileConverter: FileConverter
) {
    fun process(multipartFile: MultipartFile): ProcessingResponse = try {

        val processingMetadata = fileProcessingPreparer.prepare(multipartFile)
        val convertedFile = fileConverter.convert(multipartFile, processingMetadata)

        //TODO save to S3
        //TODO save to MongoDB
        //fileConverter.cleanUp(processingMetadata)

        ProcessingResponse.success()
    } catch (ex: Exception) {
        //TODO better failures processing
        ProcessingResponse.error(ex.message ?: "Unknown error")
    }
}
