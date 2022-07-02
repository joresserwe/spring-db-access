package me.dataaccess.repository.memory

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto
import org.springframework.stereotype.Repository

@Repository
class MemoryItemRepository : ItemRepository {

    companion object {
        private val store = hashMapOf<Long, Item>()
        private var sequence = 0L
    }

    override fun save(item: Item): Item {
        item.id = ++sequence
        store[item.id] = item
        return item
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        findById(itemId).apply {
            itemName = updateParam.itemName
            price = updateParam.price
            quantity = updateParam.quantity
        }
    }

    override fun findById(id: Long) = store[id] ?: throw NoSuchElementException(id.toString())

    override fun findAll(cond: ItemSearchCond): List<Item> {
        val itemName = cond.itemName
        val maxPrice = cond.maxPrice
        return store.values.filter {
            if (itemName.isNullOrBlank()) return@filter true
            it.itemName.contains(itemName)
        }.filter {
            if (maxPrice == null) return@filter true
            it.price <= maxPrice
        }.toMutableList()
    }

    fun clearStore() = store.clear()
}
