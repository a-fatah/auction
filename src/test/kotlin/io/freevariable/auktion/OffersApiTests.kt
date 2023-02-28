package io.freevariable.auktion

import io.freevariable.auktion.model.Offer
import io.freevariable.auktion.repository.OfferRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OffersApiTests {

    @Autowired
    private lateinit var repository: OfferRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun listOffers() {
        val offer = Offer(title = "Test Offer", description = "This is a test offer", price = 100, password="password", open = true)
        repository.save(offer)

        mockMvc.get("/offers").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$._embedded.offers", hasSize<Int>(1))
            jsonPath("$._embedded.offers[0].title") { value("Test Offer") }
            jsonPath("$._embedded.offers[0].description") { value("This is a test offer") }
            jsonPath("$._embedded.offers[0].price") { value(100) }
            jsonPath("$._embedded.offers[0].open") { value(true) }
        }

    }

}