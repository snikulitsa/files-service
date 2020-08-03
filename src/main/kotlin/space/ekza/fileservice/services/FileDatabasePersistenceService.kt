package space.ekza.fileservice.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import space.ekza.fileservice.entity.RootFileEntity
import space.ekza.fileservice.repository.FilesRepository

@Service
class FileDatabasePersistenceService(
    private val filesRepository: FilesRepository
) {

    // TODO move MongoDB integration to other microservice with REST/RABBIT/KAFKA api
    fun save(file: RootFileEntity) {
        logger.info("Persisting file to database: $file")
        filesRepository.save(file)
        logger.info("File persisted to database:  $file")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FileDatabasePersistenceService::class.java)
    }
}
