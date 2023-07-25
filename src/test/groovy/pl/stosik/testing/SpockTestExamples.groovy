package pl.stosik.testing

import pl.stosik.testing.mars.rover.*
import spock.lang.Specification
import spock.lang.Unroll

class SimpleBddExamples extends Specification {

    def 'should create item'() {
        given:
        def uuidGenerator = Stub(UUIDGenerator)
        def itemService = new ItemService(uuidGenerator, new ItemRepository())
        def uuid = UUID.randomUUID().toString()
        uuidGenerator.generate() >> uuid

        when:
        def item = itemService.createItem("Iphone 6")

        then:
        item.name == "Iphone 6"
    }
}

class DataDrivenExamples extends Specification {

    @Unroll
    def "maximum of two (#a and #b) numbers is #c"() {
        expect:
        Math.max(a, b) == c

        where:
        a | b || c
        1 | 3 || 3
        7 | 4 || 7
        0 | 0 || 0
    }
}

class InteractionsDrivenExamples extends Specification {

    def "should send messages to all subscribers"() {
        given:
        Publisher publisher = new Publisher()
        Subscriber subscriber = Mock()
        Subscriber subscriber2 = Mock()

        and:
        publisher.subscribers << subscriber // publisher.subscribers.addAll()
        publisher.subscribers << subscriber2

        when:
        publisher.send("hello")

        then:
        1 * subscriber.receive("hello")
        1 * subscriber2.receive("hello")
        publisher.messageCount == 1
    }

    def "should send multiple messages to one subscriber"() {
        given:
        Publisher publisher = new Publisher()
        Subscriber subscriber = Mock()

        and:
        publisher.subscribers << subscriber

        when:
        publisher.send("hello")

        and:
        publisher.send("goodbye")

        then:
        with(subscriber) {
            1 * receive("hello")
            1 * receive("goodbye")
        }
        publisher.messageCount == 2
    }

    def 'should respond to message on subscription'() {
        given:
        Publisher publisher = new Publisher()
        Subscriber subscriber = Mock()

        and:
        publisher.subscribers << subscriber
        subscriber.receive("message1") >> "ok"

        when:
        publisher.send("message1")

        then:
        1 * subscriber.receive("message1")
    }
}