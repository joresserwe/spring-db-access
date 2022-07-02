package me.dataaccess.repository.jdbctemplate

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto
import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import javax.sql.DataSource

class JdbcTemplateItemRepositoryV1(
    private val dataSource: DataSource
) : ItemRepository {

    private val log = KotlinLogging.logger {}
    private val template = JdbcTemplate(dataSource)

    override fun save(item: Item): Item {
        val sql = "insert into item(item_name, price, quantity) values (?,?,?)"
        val keyHolder = GeneratedKeyHolder()
        template.update({ conn ->
            // 자동 증가 키
            conn.prepareStatement(sql, arrayOf("id")).apply {
                setString(1, item.itemName)
                setInt(2, item.price)
                setInt(3, item.quantity)
            }
        }, keyHolder)
        val key = keyHolder.key!!.toLong()
        item.id = key
        return item
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        val sql = """
            update item
               set item_name = ?
                 , price = ?
                 , quantity = ?
             where id = ?
        """.trimIndent()
        template.update(sql, updateParam.itemName, updateParam.price, updateParam.quantity, itemId)
    }

    override fun findById(id: Long): Item? {
        val sql = "select * from item where id = ?"
        return try {
            template.queryForObject(sql, itemRowMapper(), id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    private fun itemRowMapper() = RowMapper { rs, _ ->
        Item(
            itemName = rs.getString("item_name"),
            price = rs.getInt("price"),
            quantity = rs.getInt("quantity")
        ).apply { id = rs.getLong("id") }
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        var sql = "select id, item_name, price, quantity from item where 1=1"
        val param = arrayListOf<Any>()
        val itemName = cond.itemName
        val maxPrice = cond.maxPrice
        if (!itemName.isNullOrBlank()) {
            sql += " and item_name like concat('%',?,'%')"
            param.add(itemName)
        }
        if (maxPrice != null) {
            sql += " and price <= ?"
            param.add(maxPrice)
        }

        log.info { "sql=${sql}" }
        return if (param.isEmpty())
            template.query(sql, itemRowMapper())
        else
            template.query(sql, itemRowMapper(), *param.toArray())
    }
}
