package me.dataaccess.service

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto
import org.springframework.stereotype.Service

@Service
class ItemServiceV1(
    private val itemRepository: ItemRepository
) : ItemService {

    override fun save(item: Item) = itemRepository.save(item)

    override fun update(itemId: Long, updateParam: ItemUpdateDto) =
        itemRepository.update(itemId, updateParam)

    override fun findById(id: Long): Item = itemRepository.findById(id)

    override fun findItems(itemSearch: ItemSearchCond) = itemRepository.findAll(itemSearch)
}
