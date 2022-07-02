package me.dataaccess.config

import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.jdbctemplate.JdbcTemplateItemRepositoryV1
import me.dataaccess.service.ItemService
import me.dataaccess.service.ItemServiceV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class JdbcTemplateV1Config(
    private val dataSource: DataSource
) {
    @Bean
    fun itemService(): ItemService {
        return ItemServiceV1(itemRepository())
    }

    @Bean
    fun itemRepository(): ItemRepository {
        return JdbcTemplateItemRepositoryV1(dataSource)
    }
}
