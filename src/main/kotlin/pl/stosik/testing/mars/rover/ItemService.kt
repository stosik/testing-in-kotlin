package pl.stosik.testing.mars.rover

open class ItemService(private val uuidGenerator: UUIDGenerator, private val itemRepository: ItemRepository) {

    fun createItem(name: String): Item {
        val item = Item(uuidGenerator.generate(), name)
        itemRepository.add(item)
        return item
    }
}