package space.ekza.fileservice.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.config.FileConvertingProperties
import space.ekza.fileservice.model.ConvertedFile
import space.ekza.fileservice.model.FileProcessingMetadata
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@Component
class FileConverter(
    private val fileConvertingProperties: FileConvertingProperties
) {

    fun convert(
        multipartFile: MultipartFile,
        metadata: FileProcessingMetadata
    ): CompletableFuture<ConvertedFile> = CompletableFuture.supplyAsync {
        saveOriginalTemporaryFileToConvertingFolder(metadata, multipartFile)
        convertFile(metadata)
        logger.info("Converting file ${metadata.pathToTemporaryOriginalFile} process FINISHED")
        ConvertedFile(
            uuid = metadata.fileUUID,
            originalData = multipartFile.bytes,
            convertedData = Files.readAllBytes(Path.of(metadata.pathToTemporaryConvertedFile))
        )
    }

    fun cleanUp(metadata: FileProcessingMetadata) {
        logger.info("Cleanup: $metadata")
        Files.delete(Path.of(metadata.pathToTemporaryOriginalFile))
        Files.delete(Path.of(metadata.pathToTemporaryConvertedFile))
    }

    private fun saveOriginalTemporaryFileToConvertingFolder(
        metadata: FileProcessingMetadata,
        multipartFile: MultipartFile
    ) {
        val path = Path.of(metadata.pathToTemporaryOriginalFile)
        Files.createFile(path)
        multipartFile.transferTo(path)
        logger.info("Temporary file: ${metadata.pathToTemporaryOriginalFile} saved to converting folder")
    }

    private fun convertFile(metadata: FileProcessingMetadata) {
        val nativeProcessBuilder = ProcessBuilder(
            fileConvertingProperties.converterPath,
            INPUT_COMMAND_ARG, metadata.pathToTemporaryOriginalFile,
            OUTPUT_COMMAND_ARG, metadata.pathToTemporaryConvertedFile
        )
        val startedProcess = nativeProcessBuilder.start()

        val convertingLog = "${String(startedProcess.errorStream.readAllBytes())} " +
            String(startedProcess.inputStream.readAllBytes())

        logger.info("Native process converting log:\n$convertingLog")
    }

    companion object {
        private const val INPUT_COMMAND_ARG = "--input"
        private const val OUTPUT_COMMAND_ARG = "--output"
        private val logger = LoggerFactory.getLogger(FileConverter::class.java)
    }
}
