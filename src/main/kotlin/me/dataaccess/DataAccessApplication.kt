package me.dataaccess

import me.dataaccess.config.JdbcTemplateV2Config
import me.dataaccess.repository.ItemRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

//@Import(MemoryConfig::class)
//@Import(JdbcTemplateV1Config::class)
@Import(JdbcTemplateV2Config::class)
@SpringBootApplication(scanBasePackages = ["me.dataaccess.web"])
class DataAccessApplication {
    @Bean
    @Profile("local")
    fun testDataInit(itemRepository: ItemRepository) = TestDataInit(itemRepository)
}

fun main(args: Array<String>) {
    runApplication<DataAccessApplication>(*args)
}
