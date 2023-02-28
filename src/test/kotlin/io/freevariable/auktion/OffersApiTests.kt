package io.freevariable.auktion

import io.freevariable.auktion.model.Offer
import io.freevariable.auktion.repository.OfferRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OffersApiTests {

    @Autowired
    private lateinit var repository: OfferRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun listOffers() {
        val offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        repository.save(offer)

        mockMvc.get("/offers").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$._embedded.offers", hasSize<Int>(1))
            jsonPath("$._embedded.offers[0].title") { value("Test Offer") }
            jsonPath("$._embedded.offers[0].description") { value("This is a test offer") }
            jsonPath("$._embedded.offers[0].price") { value(100) }
            jsonPath("$._embedded.offers[0].open") { value(true) }
            jsonPath("$._embedded.offers[0].password") { doesNotExist() }
        }

    }

    @Test
    fun searchOpenOffers() {
        val offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        repository.save(offer)

        mockMvc.get("/offers/search/by-status?open=true").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$._embedded.offers", hasSize<Int>(1))
            jsonPath("$._embedded.offers[0].title") { value("Test Offer") }
            jsonPath("$._embedded.offers[0].description") { value("This is a test offer") }
            jsonPath("$._embedded.offers[0].price") { value(100) }
            jsonPath("$._embedded.offers[0].open") { value(true) }
            jsonPath("$._embedded.offers[0].password") { doesNotExist() }
        }

    }

    @Test
    fun createOffer() {
        val offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        repository.save(offer)

        mockMvc.post("/offers") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "title": "Test Offer",
                    "description": "This is a test offer",
                    "price": 100,
                    "password": "password",
                    "open": true
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
        }

    }

    @Test
    fun getOffer() {
        val offer = Offer(
            id = 1,
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        repository.save(offer)

        // get first offer from repository and use its id
        val first = repository.findAll().first()

        mockMvc.get("/offers/${first.id}").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$.title") { value("Test Offer") }
            jsonPath("$.description") { value("This is a test offer") }
            jsonPath("$.price") { value(100) }
            jsonPath("$.open") { value(true) }
            jsonPath("$._embedded.offers[0].password") { doesNotExist() }
        }.andDo { print() }

    }

}