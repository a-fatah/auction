package io.freevariable.auktion.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class Offer(
    @Id @GeneratedValue val id: Long? = null,
    val title: String,
    val description: String,
    val price: Int,
    val password: String,
    val open: Boolean,
    @OneToOne val selectedBid: Bid? = null,
    @OneToMany(mappedBy = "offer") val bids: List<Bid> = emptyList()
)
