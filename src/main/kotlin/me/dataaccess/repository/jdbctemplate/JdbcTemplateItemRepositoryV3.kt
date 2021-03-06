package me.dataaccess.repository.jdbctemplate

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemRepository
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto
import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import javax.sql.DataSource

/**
 * SimpleJdbcInsert
 */
class JdbcTemplateItemRepositoryV3(
    dataSource: DataSource

) : ItemRepository {

    private val log = KotlinLogging.logger {}

    //private val template = JdbcTemplate(dataSource)
    private val template = NamedParameterJdbcTemplate(dataSource)
    private val jdbcInsert = SimpleJdbcInsert(dataSource)
        .withTableName("item")
        .usingGeneratedKeyColumns("id")
        .usingColumns("item_name", "price", "quantity") // 생략 가능

    override fun save(item: Item): Item {
        val param = BeanPropertySqlParameterSource(item)
        log.info { "param=${param}" }
        val key = jdbcInsert.executeAndReturnKey(param)
        item.id = key.toLong()
        return item
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        val sql = """
            update item
               set item_name = :itemName
                 , price = :price
                 , quantity = :quantity
             where id = :id
        """.trimIndent()

        val param = MapSqlParameterSource()
            .addValue("itemName", updateParam.itemName)
            .addValue("price", updateParam.price)
            .addValue("quantity", updateParam.quantity)
            .addValue("id", itemId)
        template.update(sql, param)
    }

    override fun findById(id: Long): Item? {
        val sql = "select * from item where id = :id"
        return try {
            val param = mapOf("id" to id)
            template.queryForObject(sql, param, itemRowMapper())
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        var sql = "select id, item_name, price, quantity from item where 1=1"
        val itemName = cond.itemName
        val maxPrice = cond.maxPrice
        val param = BeanPropertySqlParameterSource(cond)
        log.info { "param=${param}" }

        if (!itemName.isNullOrBlank()) {
            sql += " and item_name like concat('%',:itemName,'%')"
        }
        if (maxPrice != null) {
            sql += " and price <= :maxPrice"
        }

        log.info { "sql=${sql}" }
        return template.query(sql, param, itemRowMapper())
    }

    //private fun itemRowMapper() = BeanPropertyRowMapper.newInstance(Item::class.java) // camel 변환 지원
    private fun itemRowMapper() = DataClassRowMapper.newInstance(Item::class.java)
}
