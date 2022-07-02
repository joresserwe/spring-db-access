package me.dataaccess.repository.jpa

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager


@Repository
@Transactional
class JpaItemRepository(
    private val em: EntityManager
) : ItemRepository {

    private val log = KotlinLogging.logger {}

    override fun save(item: Item): Item {
        em.persist(item)
        return item
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        val findItem = findById(itemId)
        findItem?.apply {
            itemName = updateParam.itemName
            price = updateParam.price
            quantity = updateParam.quantity
        }
    }

    override fun findById(id: Long): Item? {
        return em.find(Item::class.java, id)
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        var jpql = "select i from Item i where 1=1"

        val param = arrayListOf<Any>()
        val maxPrice = cond.maxPrice
        val itemName = cond.itemName

        if (!itemName.isNullOrBlank()) {
            jpql += " and i.itemName like concat('%',:itemName,'%')"
            param.add(itemName)
        }
        if (maxPrice != null) {
            jpql += " and i.price <= :maxPrice"
            param.add(maxPrice)
        }

        log.info { "jpql=${jpql}" }

        val query = em.createQuery(jpql, Item::class.java)
        if (!itemName.isNullOrBlank()) {
            query.setParameter("itemName", itemName)
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice)
        }
        return query.resultList
    }
}
