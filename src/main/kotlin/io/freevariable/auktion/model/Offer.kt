package io.freevariable.auktion.model

import jakarta.persistence.*


@Entity
data class Offer(
    @Id @GeneratedValue val id: Long? = null,
    val title: String,
    val description: String,
    val price: Int,
    val password: String,
    val open: Boolean,
    @OneToOne(cascade = [CascadeType.ALL]) val selectedBid: Bid? = null,
    @OneToMany(mappedBy = "offer", cascade = [CascadeType.ALL]) val bids: List<Bid> = emptyList()
) {
    constructor(): this(null, "", "", 0, "", true, null, emptyList())
}
