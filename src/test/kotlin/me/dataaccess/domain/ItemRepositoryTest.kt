package me.dataaccess.domain

import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto
import me.dataaccess.repository.memory.MemoryItemRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class ItemRepositoryTest {

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @AfterEach
    fun afterEach() {
        //MemoryItemRepository 의 경우 제한적으로 사용
        (itemRepository as? MemoryItemRepository)?.clearStore()
    }

    @Test
    fun save() {
        //given
        val item = Item("itemA", 10000, 10)

        //when
        val savedItem: Item = itemRepository.save(item)

        //then
        val findItem: Item = itemRepository.findById(item.id)
        Assertions.assertThat(findItem).isEqualTo(savedItem)
    }

    @Test
    fun updateItem() {
        //given
        val item = Item("item1", 10000, 10)
        val savedItem: Item = itemRepository.save(item)
        val itemId = savedItem.id

        //when
        val updateParam = ItemUpdateDto("item2", 20000, 30)
        itemRepository.update(itemId, updateParam)

        //then
        val (itemName, price, quantity) = itemRepository.findById(itemId)
        Assertions.assertThat(itemName).isEqualTo(updateParam.itemName)
        Assertions.assertThat(price).isEqualTo(updateParam.price)
        Assertions.assertThat(quantity).isEqualTo(updateParam.quantity)
    }

    @Test
    fun findItems() {
        //given
        val item1 = Item("itemA-1", 10000, 10)
        val item2 = Item("itemA-2", 20000, 20)
        val item3 = Item("itemB-1", 30000, 30)
        itemRepository.save(item1)
        itemRepository.save(item2)
        itemRepository.save(item3)

        //둘 다 없음 검증
        test(null, null, item1, item2, item3)
        test("", null, item1, item2, item3)

        //itemName 검증
        test("itemA", null, item1, item2)
        test("temA", null, item1, item2)
        test("itemB", null, item3)

        //maxPrice 검증
        test(null, 10000, item1)

        //둘 다 있음 검증
        test("itemA", 10000, item1)
    }

    fun test(itemName: String?, maxPrice: Int?, vararg items: Item?) {
        val result = itemRepository.findAll(ItemSearchCond(itemName, maxPrice))
        Assertions.assertThat(result).containsExactly(*items)
    }
}
