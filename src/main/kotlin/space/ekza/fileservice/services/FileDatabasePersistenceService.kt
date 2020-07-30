package space.ekza.fileservice.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import space.ekza.fileservice.entity.RootFileEntity

@Service
class FileDatabasePersistenceService {

    fun save(file: RootFileEntity) {
        logger.info("Persisting file to database: $file")
        //TODO save to MongoDB
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FileDatabasePersistenceService::class.java)
    }
}
