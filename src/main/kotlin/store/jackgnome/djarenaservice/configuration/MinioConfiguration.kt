package store.jackgnome.djarenaservice.configuration

import io.minio.MinioClient
import org.hibernate.search.mapper.pojo.bridge.builtin.impl.DefaultDoubleBridge
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import store.jackgnome.djarenaservice.bridge.DoubleBridge

@Configuration
class MinioConfiguration {

    @Value("\${object-storage.endpoint.uri}")
    private lateinit var endpoint: String

    @Value("\${object-storage.credentials.access-key}")
    private lateinit var accessKey: String

    @Value("\${object-storage.credentials.secret-key}")
    private lateinit var secretKey: String

    @Bean
    fun minioClient(): MinioClient = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(accessKey, secretKey)
        .build()

    @Bean
    fun test(): DoubleBridge {
        return DoubleBridge()
    }
}