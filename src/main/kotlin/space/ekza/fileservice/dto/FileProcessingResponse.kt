package space.ekza.fileservice.dto

import space.ekza.fileservice.dto.ProcessingStatus.SUCCESS

data class FileProcessingResponse (
    val status: ProcessingStatus,
    val message: String = ""
) {
    companion object {
        fun success() = FileProcessingResponse(status = SUCCESS)
    }
}
