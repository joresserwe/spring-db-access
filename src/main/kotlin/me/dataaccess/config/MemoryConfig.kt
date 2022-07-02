package me.dataaccess.config

import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.memory.MemoryItemRepository
import me.dataaccess.service.ItemService
import me.dataaccess.service.ItemServiceV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MemoryConfig {
    @Bean
    fun itemService(): ItemService {
        return ItemServiceV1(itemRepository())
    }

    @Bean
    fun itemRepository(): ItemRepository {
        return MemoryItemRepository()
    }
}
