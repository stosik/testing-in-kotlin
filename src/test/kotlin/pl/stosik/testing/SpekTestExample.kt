package pl.stosik.testing

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pl.stosik.testing.mars.rover.ItemRepository
import pl.stosik.testing.mars.rover.ItemService
import pl.stosik.testing.mars.rover.Publisher
import pl.stosik.testing.mars.rover.Subscriber
import pl.stosik.testing.mars.rover.UUIDGenerator
import java.util.*
import kotlin.math.max

class SimpleBddExamplesSpek : Spek({
    describe("ItemService") {
        val uuidGenerator = mockk<UUIDGenerator>()
        val itemService = ItemService(uuidGenerator, ItemRepository())

        it("should create item") {
            val uuid = UUID.randomUUID().toString()
            every { uuidGenerator.generate() } returns uuid

            val item = itemService.createItem("Iphone 6")

            item.name shouldBe "Iphone 6"
        }
    }
})

class DataDrivenExamplesSpek : Spek({
    data class MaxTest(val a: Int, val b: Int, val c: Int)
    describe("max") {
        listOf(
            MaxTest(a = 1, b = 3, c = 3),
            MaxTest(a = 7, b = 4, c = 7),
            MaxTest(a = 0, b = 0, c = 0)
        ).forEach { test ->
            it("calculates maximum of ${test.a} and ${test.b} as ${test.c}") {
                max(test.a, test.b) shouldBe test.c
            }
        }
    }
})

class InteractionsDrivenExamplesSpek : Spek({
    describe("Publisher") {
        it("should send messages to all subscribers") {
            val publisher = Publisher()
            val subscriber = mockk<Subscriber>()
            val subscriber2 = mockk<Subscriber>()
            publisher.subscribers.add(subscriber)
            publisher.subscribers.add(subscriber2)

            every { subscriber.receive("hello") } just runs
            every { subscriber2.receive("hello") } just runs

            publisher.send("hello")

            verify(exactly = 1) { subscriber.receive("hello") }
            verify(exactly = 1) { subscriber2.receive("hello") }
            publisher.messageCount shouldBe 1
        }

        it("should send multiple messages to one subscriber") {
            val publisher = Publisher()
            val subscriber = mockk<Subscriber>()
            val subscriber2 = mockk<Subscriber>()
            publisher.subscribers.add(subscriber)
            publisher.subscribers.add(subscriber2)

            every { subscriber.receive("hello") } just runs
            every { subscriber.receive("goodbye") } just runs
            every { subscriber2.receive("hello") } just runs
            every { subscriber2.receive("goodbye") } just runs

            publisher.send("hello")
            publisher.send("goodbye")

            verify(exactly = 1) { subscriber.receive("hello") }
            verify(exactly = 1) { subscriber.receive("goodbye") }
            publisher.messageCount shouldBe 2
        }

        it("should respond to message on subscription") {
            val publisher = Publisher()
            val subscriber = mockk<Subscriber>()
            publisher.subscribers.add(subscriber)

            every { subscriber.receive("hello") } just runs

            publisher.send("hello")

            verify(exactly = 1) { subscriber.receive("hello") }
            publisher.messageCount shouldBe 1
        }
    }
})