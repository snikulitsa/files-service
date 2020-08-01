package space.ekza.fileservice.repository

import org.springframework.data.mongodb.repository.MongoRepository
import space.ekza.fileservice.entity.RootFileEntity

interface FilesRepository : MongoRepository<RootFileEntity, String>
