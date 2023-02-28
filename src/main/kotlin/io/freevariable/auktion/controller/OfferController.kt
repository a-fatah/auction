package io.freevariable.auktion.controller

import io.freevariable.auktion.model.Offer

import io.freevariable.auktion.repository.OfferRepository

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/offers")
class OfferController(private val offerRepository: OfferRepository) {

    @GetMapping
    fun getAllOffers() = offerRepository.findByOpen(true)

    @GetMapping("/{id}")
    fun getOfferById(@PathVariable id: Long) = offerRepository.findById(id)

    @PostMapping
    fun createNewOffer(@RequestBody offer: Offer) = offerRepository.save(offer)

    @PutMapping("/{id}")
    fun updateOfferById(@PathVariable id: Long, @RequestBody offer: Offer) = offerRepository.save(offer)

    @DeleteMapping("/{id}")
    fun deleteOfferById(@PathVariable id: Long) = offerRepository.deleteById(id)
}