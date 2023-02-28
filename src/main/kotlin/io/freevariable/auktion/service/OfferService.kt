package io.freevariable.auktion.service

import io.freevariable.auktion.model.Offer
import io.freevariable.auktion.repository.OfferRepository


interface OfferService {
    fun getAllOffers(): List<Offer>
    fun getOfferById(id: Int): Offer
    fun createNewOffer(offer: Offer): Offer
    fun updateOffer(id: Int, offer: Offer): Offer
    fun deleteOffer(id: Int)
}


class OfferServiceImpl(private val offerRepository: OfferRepository) {
    fun getAllOffers() = offerRepository.findAll()
    fun getOfferById(id: Long) = offerRepository.findById(id)
    fun createNewOffer(offer: Offer) = offerRepository.save(offer)
    fun updateOfferById(id: Long, offer: Offer) = offerRepository.save(offer)
    fun deleteOfferById(id: Long) = offerRepository.deleteById(id)
}