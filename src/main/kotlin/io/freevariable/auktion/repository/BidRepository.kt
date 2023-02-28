package io.freevariable.auktion.repository

import io.freevariable.auktion.model.Bid
import org.springframework.data.jpa.repository.JpaRepository


interface BidRepository: JpaRepository<Bid, Long> {
    fun findByOfferId(offerId: Long): List<Bid>
}