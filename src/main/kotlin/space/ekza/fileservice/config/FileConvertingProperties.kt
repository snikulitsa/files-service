package space.ekza.fileservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "file-converting")
@ConstructorBinding
data class FileConvertingProperties (
    val converterPath: String,
    val rawFilesFolder: String,
    val convertedFilesFolder: String
)

@Configuration
@EnableConfigurationProperties(FileConvertingProperties::class)
class FileConvertingPropertiesConfig
