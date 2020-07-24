package space.ekza.fileservice.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import space.ekza.fileservice.services.FilesProcessingService

@RestController
class FilesProcessingController(
    private val service: FilesProcessingService
) {

    @PostMapping(value = ["/v1/process"])
    fun process(@RequestPart("file") file: MultipartFile) = service.process(file)
}
