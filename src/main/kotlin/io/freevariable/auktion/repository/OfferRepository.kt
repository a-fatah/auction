package io.freevariable.auktion.repository

import io.freevariable.auktion.model.Offer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource

@RepositoryRestResource
interface OfferRepository : JpaRepository<Offer, Long> {
    @RestResource(path = "by-status", rel = "by-status")
    fun findByOpen(open: Boolean = true): List<Offer>
}
