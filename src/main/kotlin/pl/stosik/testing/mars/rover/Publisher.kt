package pl.stosik.testing.mars.rover

open class Publisher() {
    val subscribers = mutableListOf<Subscriber>()
    var messageCount = 0

    fun send(message: String) {
        subscribers.forEach { it.receive(message) }
        messageCount++
    }
}

interface Subscriber {
    fun receive(message: String)
}