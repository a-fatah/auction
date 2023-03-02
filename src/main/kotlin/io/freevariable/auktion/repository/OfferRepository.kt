package io.freevariable.auktion.repository

import io.freevariable.auktion.controller.OfferView
import io.freevariable.auktion.model.Bid
import io.freevariable.auktion.model.Offer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.transaction.annotation.Transactional

@RepositoryRestResource(excerptProjection = OfferView::class)
interface OfferRepository : JpaRepository<Offer, Long> {

    @RestResource(path = "by-status", rel = "by-status")
    fun findByOpen(open: Boolean = true): List<Offer>

    @Modifying
    @Transactional
    @Query("UPDATE Offer o SET o.selectedBid = ?2, o.open = false WHERE o.id = ?1")
    fun closeOffer(offerId: Long, selectedBid: Bid)
}
