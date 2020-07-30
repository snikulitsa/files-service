package space.ekza.fileservice.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.dto.ProcessingResponse
import space.ekza.fileservice.mappers.FileMapper
import java.util.concurrent.CompletableFuture.supplyAsync

@Service
class FilesProcessingService(
    private val fileProcessingPreparer: FileProcessingPreparer,
    private val fileConverter: FileConverter,
    private val amazonS3Service: AmazonS3Service,
    private val databasePersistenceService: FileDatabasePersistenceService,
    private val fileMapper: FileMapper
) {

    fun process(multipartFile: MultipartFile): ProcessingResponse = try {

            val processingMetadata = fileProcessingPreparer.prepare(multipartFile)
            val convertedFile = fileConverter.convert(multipartFile, processingMetadata).get()
            fileConverter.cleanUp(processingMetadata)

            val originalS3File = supplyAsync {
                amazonS3Service.publish(
                    file = convertedFile.originalData,
                    rootUuid = processingMetadata.fileUUID,
                    extension = processingMetadata.originalFileExtension
                )
            }
            val convertedS3File = supplyAsync {
                amazonS3Service.publish(
                    file = convertedFile.convertedData,
                    rootUuid = processingMetadata.fileUUID,
                    extension = processingMetadata.targetFileExtension
                )
            }

            supplyAsync {
                val fileEntity = fileMapper.toEntity(
                    rootUuid = processingMetadata.fileUUID,
                    original = originalS3File.get(),
                    renderReady = convertedS3File.get()
                )
                databasePersistenceService.save(fileEntity)
            }

        ProcessingResponse.accepted()
    } catch (ex: Exception) {
        logger.error(ex.message, ex)
        ProcessingResponse.error(message = ex.message ?: "")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FilesProcessingService::class.java)
    }
}
