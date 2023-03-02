package io.freevariable.auktion

import io.freevariable.auktion.model.Bid
import io.freevariable.auktion.model.Offer
import io.freevariable.auktion.repository.BidRepository
import io.freevariable.auktion.repository.OfferRepository
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class OffersApiTests {

    @Autowired
    private lateinit var offerRepository: OfferRepository

    @Autowired
    private lateinit var bidRepository: BidRepository

    private lateinit var mockMvc: MockMvc

    @RegisterExtension
    val restDocumentation = RestDocumentationExtension("build/generated-snippets")

    @BeforeEach
    fun setup(
        wacContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider
    ) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wacContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()


        offerRepository.deleteAll()
    }

    @Test
    fun `given offers, when list offers, then returns offers`() {
        val offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

        mockMvc.get("/offers").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$._embedded.offers", hasSize<Int>(1))
            jsonPath("$._embedded.offers[0].title") { value("Test Offer") }
            jsonPath("$._embedded.offers[0].description") { value("This is a test offer") }
            jsonPath("$._embedded.offers[0].price") { value(100) }
            jsonPath("$._embedded.offers[0].open") { value(true) }
            jsonPath("$._embedded.offers[0].password") { doesNotExist() }
        }.andDo {
            document("list-offers")
        }

    }

    @Test
    fun `given offers are open, when search for closed offers, then return no offers`() {
        val offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

        mockMvc.get("/offers/search/by-status?open=false").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$._embedded.offers", hasSize<Int>(0))
        }.andDo { document("search-closed-offers") }

    }

    @Test
    fun `given offers are open, when search for open offers, then return offers`() {
        val offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

        mockMvc.get("/offers/search/by-status?open=true").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$._embedded.offers", hasSize<Int>(1))
            jsonPath("$._embedded.offers[0].title") { value("Test Offer") }
            jsonPath("$._embedded.offers[0].description") { value("This is a test offer") }
            jsonPath("$._embedded.offers[0].price") { value(100) }
            jsonPath("$._embedded.offers[0].open") { value(true) }
            jsonPath("$._embedded.offers[0].password") { doesNotExist() }
        }.andDo { document("search-open-offers") }

    }

    @Test
    fun `given valid offer, when create offer, then return 201`() {
        val offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

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
        }.andDo { document("create-offer") }

    }

    @Test
    fun `given an offer exists, when get offer, then return offer`() {
        val offer = Offer(
            id = 1,
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

        // get first offer from repository and use its id
        val first = offerRepository.findAll().first()

        mockMvc.get("/offers/${first.id}").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$.title") { value("Test Offer") }
            jsonPath("$.description") { value("This is a test offer") }
            jsonPath("$.price") { value(100) }
            jsonPath("$.open") { value(true) }
            jsonPath("$._embedded.offers[0].password") { doesNotExist() }
        }

    }

    @Test
    fun `given a bid is made, when get offer, then return bid`() {
        var offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

        // create a Bid
        var bid = Bid(
            offer = offer,
            buyerName = "Test Buyer",
            amount = 100
        )

        bid = bidRepository.save(bid)

        // get first offer from repository and use its id
        offer = offerRepository.findAll().first()

        mockMvc.get("/offers/${offer.id}/bids").andExpect {
            status { isOk() }
            content { contentType("application/hal+json") }
            jsonPath("$._embedded.bids", hasSize<Int>(1))
            jsonPath("$._embedded.bids[0].buyerName") { value("Test Buyer") }
            jsonPath("$._embedded.bids[0].amount") { value(100) }
        }.andDo { document("get-offer-with-bids") }

    }


    @Test
    fun `given a bid is made, when close offer, then closed`() {
        var offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

        // create a Bid
        var bid = Bid(
            offer = offer,
            buyerName = "Test Buyer",
            amount = 100
        )

        bid = bidRepository.save(bid)

        // get first offer from repository and use its id
        offer = offerRepository.findAll().first()

        mockMvc.put("/offers/${offer.id}/close") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "selectedBid": ${bid.id},
                    "password": "password"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
        }.andDo { document("close-offer") }

        offer = offerRepository.findById(offer.id!!).get()
        assertEquals(false, offer.open)
    }


    @Test
    fun `given offer is closed, when get offer, then return 404`() {
        val offer = Offer(
            id = 1,
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = false
        )
        offerRepository.save(offer)

        // get first offer from repository and use its id
        val first = offerRepository.findAll().first()

        mockMvc.get("/offers/${first.id}").andExpect {
            status { isNotFound() }
        }.andDo { document("get-closed-offer") }

    }

    @Test
    fun `given valid bid, when create bid, then return 204`() {
        var offer = Offer(
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

        // get first offer from repository and use its id
        offer = offerRepository.findAll().first()

        mockMvc.post("/offers/${offer.id}/bids") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "buyerName": "Test Buyer",
                    "bidPassword": "password",
                    "amount": 100
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated() }
            header { string("Location", containsString("/offers/${offer.id}/bids/")) }
        }.andDo { document("create-bid") }

    }

    @Test
    fun `given invalid bid, when create bid, then return 400`() {
        var offer = Offer(
            id = 1,
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = true
        )
        offerRepository.save(offer)

        // get first offer from repository and use its id
        offer = offerRepository.findAll().first()

        mockMvc.post("/offers/${offer.id}/bids") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "buyerName": "Test Buyer",
                    "amount": 50
                }
            """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
        }

    }

    @Test
    fun `given offer is closed, when create bid, then return 400`() {
        var offer = Offer(
            id = 1,
            title = "Test Offer",
            description = "This is a test offer",
            price = 100,
            password = "password",
            open = false
        )
        offerRepository.save(offer)

        // get first offer from repository and use its id
        offer = offerRepository.findAll().first()

        mockMvc.post("/offers/${offer.id}/bids") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "buyerName": "Test Buyer",
                    "bidPassword": "password",
                    "amount": 100
                }
            """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
        }.andDo { document("create-bid-closed-offer") }

    }

    fun MockMvcResultHandlersDsl.document(identifier: String, vararg snippets: Snippet) {
        handle(MockMvcRestDocumentation.document(identifier, *snippets))
    }

}