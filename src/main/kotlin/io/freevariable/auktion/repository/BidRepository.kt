package io.freevariable.auktion.repository

import io.freevariable.auktion.model.Bid
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource


@RepositoryRestResource
interface BidRepository: JpaRepository<Bid, Long> {
    fun findByOfferId(offerId: Long): List<Bid>
}