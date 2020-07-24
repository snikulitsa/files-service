package space.ekza.fileservice.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.config.FileConvertingProperties
import space.ekza.fileservice.dto.FileProcessingResponse
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

@Service
class FilesProcessingService(
    private val fileConvertingProperties: FileConvertingProperties
) {

    companion object {
        private val logger = LoggerFactory.getLogger(FilesProcessingService::class.java)
    }

    fun process(file: MultipartFile): FileProcessingResponse {

        val suffix = UUID.randomUUID().toString()
        val rawFileName = "${file.name}-$suffix.fbx"
        val convertedFileName = "${file.name}-result-$suffix.glb"

        val path = Path.of("${fileConvertingProperties.rawFilesFolder}/$rawFileName")
        Files.createFile(path)
        file.transferTo(path)

        val nativeProcessBuilder = ProcessBuilder(
            fileConvertingProperties.converterPath,
            "--input", "$path",
            "--output", "${fileConvertingProperties.convertedFilesFolder}/$convertedFileName"
        )
        val startedProcess = nativeProcessBuilder.start()

        val result = "${String(startedProcess.errorStream.readAllBytes())} " +
            String(startedProcess.inputStream.readAllBytes())

        logger.info("Converting log:\n$result")

        //Files.delete(Path.of("/Users/rh/Downloads/$fileName"))

        return FileProcessingResponse.success();
    }
}
