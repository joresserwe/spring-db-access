package me.dataaccess.service

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto

interface ItemService {
    fun save(item: Item): Item
    fun update(itemId: Long, updateParam: ItemUpdateDto)
    fun findById(id: Long): Item?
    fun findItems(itemSearch: ItemSearchCond): List<Item>
}
