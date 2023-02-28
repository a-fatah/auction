package io.freevariable.auktion.service

import io.freevariable.auktion.controller.*
import io.freevariable.auktion.model.Bid
import io.freevariable.auktion.model.Offer
import io.freevariable.auktion.repository.BidRepository
import io.freevariable.auktion.repository.OfferRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional


interface OfferService {
    fun closeOffer(offerId: Long, password: String, selectedBid: Long)
    fun getOffer(offerId: Long): Offer?
    fun createBid(offerId: Long, buyerName: String, bidPassword: String, amount: Int): Bid?
}

@Service
class OfferServiceImpl(
    private val offerRepository: OfferRepository,
    private val bidRepository: BidRepository
): OfferService {

    @Transactional
    override fun closeOffer(offerId: Long, password: String, selectedBid: Long) {
        val offer = offerRepository.findById(offerId).orElseThrow { OfferNotFoundException() }
        if (offer.password != password) {
            throw BidPasswordIncorrectException()
        }

        if (!offer.open) {
            throw BidAlreadyClosedException()
        }

        val selectedBid = bidRepository.findById(selectedBid).orElseThrow { BidNotFoundException() }

        offerRepository.closeOffer(offerId, selectedBid)
    }

    override fun getOffer(offerId: Long): Offer? {
        return offerRepository.findById(offerId).filter { it.open }.orElse(null)
    }

    override fun createBid(offerId: Long, buyerName: String, bidPassword: String, amount: Int): Bid? {
        val offer = offerRepository.findById(offerId).orElseThrow { OfferNotFoundException() }
        if (offer.password != bidPassword) {
            throw BidPasswordIncorrectException()
        }

        if (!offer.open) {
            throw OfferClosedException()
        }

        // TODO: check amount is within range

        val bid = Bid(offer = offer, buyerName = buyerName, amount = amount)
        return bidRepository.save(bid)
    }
}