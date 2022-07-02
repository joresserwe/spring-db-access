package me.dataaccess.web

import me.dataaccess.domain.Item
import me.dataaccess.repository.ItemSearchCond
import me.dataaccess.repository.ItemUpdateDto
import me.dataaccess.service.ItemService
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/items")
class ItemController(
    private val itemService: ItemService
) {

    private val log = KotlinLogging.logger {}

    @GetMapping
    fun items(@ModelAttribute("itemSearch") itemSearch: ItemSearchCond, model: Model): String {
        log.info { "itemcond=${itemSearch}" }
        val items = itemService.findItems(itemSearch)
        log.info { "items=${items}" }
        model.addAttribute("items", items)
        return "items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item = itemService.findById(itemId)
        model.addAttribute("item", item)
        return "item"
    }

    @GetMapping("/add")
    fun addForm() = "addForm"

    @PostMapping("/add")
    fun addItem(@ModelAttribute item: Item, redirectAttributes: RedirectAttributes): String {
        val savedItem = itemService.save(item)
        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item = itemService.findById(itemId)
        model.addAttribute("item", item)
        return "editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(@PathVariable itemId: Long, @ModelAttribute updateParam: ItemUpdateDto): String {
        itemService.update(itemId, updateParam)
        return "redirect:/items/{itemId}"
    }
}
