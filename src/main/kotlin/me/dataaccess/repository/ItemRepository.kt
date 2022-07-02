package me.dataaccess.repository

import me.dataaccess.domain.Item

interface ItemRepository {
    fun save(item: Item): Item
    fun update(itemId: Long, updateParam: ItemUpdateDto)
    fun findById(id: Long): Item
    fun findAll(cond: ItemSearchCond): List<Item>
}
