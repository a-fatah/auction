package io.freevariable.auktion.model

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Offer(
    @Id @GeneratedValue val id: Long,
    val title: String,
    val description: String,
    val price: Int,
    val password: String
)
