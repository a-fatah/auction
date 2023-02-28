package io.freevariable.auktion.repository

import io.freevariable.auktion.model.Offer
import org.springframework.data.jpa.repository.JpaRepository

interface OfferRepository : JpaRepository<Offer, Long> {
    fun findByOpen(open: Boolean): List<Offer>
}
