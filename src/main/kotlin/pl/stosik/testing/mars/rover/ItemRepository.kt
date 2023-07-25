package pl.stosik.testing.mars.rover

open class ItemRepository {

    private val items = mutableMapOf<String, Item>()

    fun add(item: Item) {
        items[item.id] = item
    }
}