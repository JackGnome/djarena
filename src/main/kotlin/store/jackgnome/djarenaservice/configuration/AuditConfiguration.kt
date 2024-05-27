package store.jackgnome.djarenaservice.configuration

import java.util.Optional
import java.util.UUID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import store.jackgnome.djarenaservice.utils.getCurrentUserId

@Configuration
@EnableJpaAuditing
class AuditConfiguration {

    @Bean
    fun auditor(): AuditorAware<UUID> = AuditorAware<UUID> {
        Optional.ofNullable(getCurrentUserId())
    }
}