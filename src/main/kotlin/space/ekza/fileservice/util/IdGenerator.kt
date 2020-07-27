package space.ekza.fileservice.util

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class IdGenerator {
    fun generate(): String = UUID.randomUUID().toString().replace("-", "")
}
