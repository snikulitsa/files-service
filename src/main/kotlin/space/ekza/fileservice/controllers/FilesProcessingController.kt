package space.ekza.fileservice.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import space.ekza.fileservice.services.FilesProcessingService

@RestController
class FilesProcessingController(
    private val service: FilesProcessingService
) {

    @GetMapping("/v1/process")
    fun process() {
        service.process()
    }
}
