package io.freevariable.auktion.controller

import io.freevariable.auktion.model.Offer
import org.springframework.data.rest.core.config.Projection


@Projection(name = "default", types = [Offer::class])
interface OfferView {
    fun getId(): Long
    fun getTitle(): String
    fun getDescription(): String
    fun getPrice(): Int
    fun getOpen(): Boolean
}
