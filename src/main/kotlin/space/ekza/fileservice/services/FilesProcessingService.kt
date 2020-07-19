package space.ekza.fileservice.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FilesProcessingService {

    companion object {
        private val logger = LoggerFactory.getLogger(FilesProcessingService::class.java)
    }

    fun process() {

        val suffix = UUID.randomUUID().toString()
        val fileName = "test-result-$suffix.glb"

        val nativeProcessBuilder = ProcessBuilder(
            "/Users/rh/Downloads/FBX2glTF-darwin-x64",
            "--input", "/Users/rh/Downloads/test.fbx",
            "--output", "/Users/rh/Downloads/$fileName"
        )
        val startedProcess = nativeProcessBuilder.start()

        val result = "${String(startedProcess.errorStream.readAllBytes())} " +
            String(startedProcess.inputStream.readAllBytes())

        logger.info("Converting log:\n$result")

        //Files.delete(Path.of("/Users/rh/Downloads/$fileName"))

    }
}
