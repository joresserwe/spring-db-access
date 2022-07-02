package me.dataaccess

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener


class TestDataInit(
    private val itemRepository: ItemRepository
) {

    private val log = KotlinLogging.logger {}

    // 확인용 초기 데이터 추가
    @EventListener(ApplicationReadyEvent::class)
    fun initData() {
        log.info("test data init")
        itemRepository.save(Item("itemA", 10000, 10))
        itemRepository.save(Item("itemB", 20000, 20))
    }
}
