package me.dataaccess.repository.jpa

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class JpaItemRepositoryV2(
    private val repository: SpringDataJpaRepository
) : ItemRepository {

    override fun save(item: Item): Item {
        return repository.save(item)
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        findById(itemId)?.apply {
            itemName = updateParam.itemName
            price = updateParam.price
            quantity = updateParam.quantity
        }
    }

    override fun findById(id: Long): Item? {
        return repository.findByIdOrNull(id)
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        val maxPrice = cond.maxPrice
        val itemName = cond.itemName

        return if (!itemName.isNullOrBlank() && maxPrice != null) {
            repository.findItems(itemName, maxPrice)
        } else if (!itemName.isNullOrBlank()) {
            repository.findByItemNameContaining(itemName)
        } else if (maxPrice != null) {
            repository.findByPriceLessThanEqual(maxPrice)
        } else {
            repository.findAll()
        }
    }
}
