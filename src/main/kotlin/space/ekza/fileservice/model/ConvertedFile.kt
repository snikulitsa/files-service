package space.ekza.fileservice.model

data class ConvertedFile (
    val uuid: String,
    val originalData: ByteArray,
    val convertedData: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConvertedFile

        if (uuid != other.uuid) return false
        if (!originalData.contentEquals(other.originalData)) return false
        if (!convertedData.contentEquals(other.convertedData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + originalData.contentHashCode()
        result = 31 * result + convertedData.contentHashCode()
        return result
    }
}
