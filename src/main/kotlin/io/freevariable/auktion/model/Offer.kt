package io.freevariable.auktion.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Offer(
    @Id @GeneratedValue val id: Long? = null,
    val title: String,
    val description: String,
    val price: Int,
    val password: String,
    val open: Boolean
)
