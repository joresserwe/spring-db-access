package me.dataaccess

import me.dataaccess.config.SpringDatraJpaConfig
import me.dataaccess.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

//@Import(MemoryConfig::class)
//@Import(JdbcTemplateV1Config::class)
//@Import(JdbcTemplateV2Config::class)
//@Import(JdbcTemplateV3Config::class)
//@Import(JpaConfig::class)
@Import(SpringDatraJpaConfig::class)
@SpringBootApplication(scanBasePackages = ["me.dataaccess.web"])
class DataAccessApplication {

    private val log = KotlinLogging.logger {}

    @Bean
    @Profile("local")
    fun testDataInit(itemRepository: ItemRepository) = TestDataInit(itemRepository)

    /* @Bean
     @Profile("test")
     fun dataSource() = DriverManagerDataSource().apply {
         setDriverClassName("org.h2.Driver")
         url = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1" // Database Connection이 모두 끊어질 경우 DB도 종료된다. 방지
         username = "sa"
         password = ""
     }.also { log.info { "Memory Database 초기화!!" } }*/
}

fun main(args: Array<String>) {
    runApplication<DataAccessApplication>(*args)
}
