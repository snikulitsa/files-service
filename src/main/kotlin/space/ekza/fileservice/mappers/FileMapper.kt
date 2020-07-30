package space.ekza.fileservice.mappers

import org.springframework.stereotype.Component
import space.ekza.fileservice.entity.FileEntity
import space.ekza.fileservice.entity.RootFileEntity
import space.ekza.fileservice.model.AmazonS3File

@Component
class FileMapper {

    fun toEntity(rootUuid: String, original: AmazonS3File, renderReady: AmazonS3File) = RootFileEntity(
        uuid = rootUuid,
        originalFile = FileEntity(
            uuid = original.key,
            url = original.url,
            bucket = original.bucket
        ),
        renderReadyFile = FileEntity(
            uuid = renderReady.key,
            url = renderReady.url,
            bucket = renderReady.bucket
        )
    )
}
