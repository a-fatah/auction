package io.freevariable.auktion.service

import io.freevariable.auktion.model.Offer
import io.freevariable.auktion.repository.BidRepository
import io.freevariable.auktion.repository.OfferRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional


interface OfferService {
    fun closeOffer(offerId: Long, password: String, selectedBid: Long)
    fun getOffer(offerId: Long): Offer?
}


@Service
class OfferServiceImpl(
    private val offerRepository: OfferRepository,
    private val bidRepository: BidRepository
): OfferService {

    @Transactional
    override fun closeOffer(offerId: Long, password: String, selectedBid: Long) {
        val offer = offerRepository.findById(offerId).orElseThrow { Exception("Offer not found") }
        if (offer.password != password) {
            throw Exception("Invalid password")
        }

        val selectedBid = bidRepository.findById(selectedBid).orElseThrow { Exception("Bid not found") }

        offerRepository.closeOffer(offerId, selectedBid)
    }

    override fun getOffer(offerId: Long): Offer? {
        return offerRepository.findById(offerId).filter { it.open }.orElse(null)
    }
}