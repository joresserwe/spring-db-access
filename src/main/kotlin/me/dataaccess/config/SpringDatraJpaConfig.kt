package me.dataaccess.config

import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.jpa.JpaItemRepositoryV2
import me.dataaccess.repository.jpa.SpringDataJpaRepository
import me.dataaccess.service.ItemService
import me.dataaccess.service.ItemServiceV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDatraJpaConfig(
    private val springDataJpaRepository: SpringDataJpaRepository
) {
    @Bean
    fun itemService(): ItemService {
        return ItemServiceV1(itemRepository())
    }

    @Bean
    fun itemRepository(): ItemRepository {
        return JpaItemRepositoryV2(springDataJpaRepository)
    }
}
