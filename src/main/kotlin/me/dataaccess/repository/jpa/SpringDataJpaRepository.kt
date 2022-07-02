package me.dataaccess.repository.jpa

import me.dataaccess.domain.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SpringDataJpaRepository : JpaRepository<Item, Long> {

    fun findByItemNameContaining(itemName: String): List<Item>

    fun findByPriceLessThanEqual(price: Int): List<Item>

    // Query Method (아래와 같은 기능)
    fun findByItemNameLikeAndPriceLessThanEqual(itemName: String, price: Int): List<Item>

    // Query 직접 실행
    @Query("select i from Item i where i.itemName like %:itemName% and i.price = :price")
    fun findItems(itemName: String, price: Int): List<Item>

}
