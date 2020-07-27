package space.ekza.fileservice.dto

import space.ekza.fileservice.dto.ProcessingStatus.ERROR
import space.ekza.fileservice.dto.ProcessingStatus.SUCCESS

data class ProcessingResponse (
    val status: ProcessingStatus,
    val message: String = ""
) {
    companion object {
        fun success() = ProcessingResponse(status = SUCCESS)
        fun error(message: String) = ProcessingResponse(status = ERROR, message = message)
    }
}
