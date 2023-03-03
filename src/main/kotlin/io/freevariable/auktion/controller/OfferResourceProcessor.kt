package io.freevariable.auktion.controller

import io.freevariable.auktion.controller.OfferController.CloseOfferRequest
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelProcessor
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component


@Component
class OfferResourceProcessor: RepresentationModelProcessor<EntityModel<OfferView>> {

    override fun process(model: EntityModel<OfferView>): EntityModel<OfferView> {
        val offerId = model.content?.getId()
        val isOpen = model.content?.getOpen()

        // if isOpen is true, add a link to close the offer
        if (isOpen == true) {
            val closeLink = linkTo<OfferController> {
                closeOffer(CloseOfferRequest("", 0L), offerId!!)
            }.withRel("close").withTitle("Close Offer")

            model.add(closeLink)
        }

        return model
    }


}
