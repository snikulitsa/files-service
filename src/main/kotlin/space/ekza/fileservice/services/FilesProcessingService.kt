package space.ekza.fileservice.services

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.dto.ProcessingResponse
import space.ekza.fileservice.entity.RootFileEntity
import space.ekza.fileservice.mappers.FileMapper
import space.ekza.fileservice.model.AmazonS3File
import space.ekza.fileservice.model.ConvertedFile
import space.ekza.fileservice.model.FileProcessingMetadata

@Service
class FilesProcessingService(
    private val fileProcessingPreparer: FileProcessingPreparer,
    private val fileConverter: FileConverter,
    private val amazonS3Service: AmazonS3Service,
    private val databasePersistenceService: FileDatabasePersistenceService,
    private val fileMapper: FileMapper
) {

    fun process(multipartFile: MultipartFile): ProcessingResponse = try {

        //TODO process file that don't need to be converted
        //TODO adding files to existing file
        val processingMetadata = fileProcessingPreparer.prepare(multipartFile)
        val convertedFile = fileConverter.convert(multipartFile, processingMetadata)

        GlobalScope.launch {
            val originalS3File = async {
                publishOriginal(convertedFile, processingMetadata)
            }
            val convertedS3File = async {
                publishConverted(convertedFile, processingMetadata)
            }
            async {
                val fileEntity = fileMapper.toEntity(
                    rootUuid = processingMetadata.fileUUID,
                    original = originalS3File.await(),
                    renderReady = convertedS3File.await()
                )
                saveToDatabase(fileEntity)
            }.join()
        }

        ProcessingResponse.accepted(fileUuid = processingMetadata.fileUUID)
    } catch (ex: Exception) {
        logger.error(ex.message, ex)
        ProcessingResponse.error(message = "Error occurred while processing a file")
    }

    private suspend fun saveToDatabase(fileEntity: RootFileEntity) = databasePersistenceService.save(fileEntity)

    private suspend fun publishConverted(
        convertedFile: ConvertedFile,
        processingMetadata: FileProcessingMetadata
    ): AmazonS3File = amazonS3Service.publish(
        file = convertedFile.convertedData,
        rootUuid = processingMetadata.fileUUID,
        extension = processingMetadata.targetFileExtension
    )

    private suspend fun publishOriginal(
        convertedFile: ConvertedFile,
        processingMetadata: FileProcessingMetadata
    ): AmazonS3File = amazonS3Service.publish(
        file = convertedFile.originalData,
        rootUuid = processingMetadata.fileUUID,
        extension = processingMetadata.originalFileExtension
    )

    companion object {
        private val logger = LoggerFactory.getLogger(FilesProcessingService::class.java)
    }
}
