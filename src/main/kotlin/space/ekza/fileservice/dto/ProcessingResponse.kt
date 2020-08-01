package space.ekza.fileservice.dto

import space.ekza.fileservice.dto.ProcessingStatus.ACCEPTED
import space.ekza.fileservice.dto.ProcessingStatus.ERROR
import space.ekza.fileservice.dto.ProcessingStatus.SUCCESS

data class ProcessingResponse (
    val status: ProcessingStatus,
    val fileUuid: String = "",
    val message: String = ""
) {
    companion object {
        fun success() = ProcessingResponse(status = SUCCESS)
        fun accepted(fileUuid: String) = ProcessingResponse(status = ACCEPTED, fileUuid = fileUuid)
        fun error(message: String) = ProcessingResponse(status = ERROR, message = message)
    }
}
