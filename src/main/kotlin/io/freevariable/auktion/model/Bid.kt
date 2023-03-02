package io.freevariable.auktion.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne


@Entity
data class Bid(
    @Id @GeneratedValue val id: Long? = null,
    @ManyToOne val offer: Offer,
    val buyerName: String,
    val amount: Int
) {
    constructor(): this(null, Offer(), "", 0)
}
