package store.jackgnome.djarenaservice.storage

import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ProductPreviewStorage {

    @Autowired
    private lateinit var minioClient: MinioClient

    @Value("\${object-storage.buckets.product-preview.name}")
    private lateinit var bucketName: String

    @Value("\${object-storage.endpoint.uri}")
    private lateinit var endpoint: String

    @Value("\${object-storage.buckets.product-preview.default-image}")
    private lateinit var defaultImageName: String

    fun save(preview: MultipartFile, productId: String): String = try {
        val previewName = buildImageName(preview, productId)
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(previewName)
                .stream(preview.inputStream, preview.size, -1)
                .build()
        )
        buildImageUrl(previewName)
    } catch (e: Exception) {
        throw e
    }

    fun getDefaultImageUrl(): String = "${endpoint}/${bucketName}/${defaultImageName}"

    private fun buildImageName(preview: MultipartFile, productId: String): String {
        val extension = preview.originalFilename?.split(".")?.last()
            ?: throw Exception("Incorrect image format: unknown extension")
        return "product-preview-$productId.$extension"
    }

    private fun buildImageUrl(imageName: String): String = "${endpoint}/${bucketName}/${imageName}"
}