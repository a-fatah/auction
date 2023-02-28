package io.freevariable.auktion.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Bid(
    @Id @GeneratedValue val id: Long? = null,
    @ManyToOne val offer: Offer,
    val buyerName: String,
    val amount: Int
)
