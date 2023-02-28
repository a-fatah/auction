package io.freevariable.auktion.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


class OfferClosedException : Exception("Offer is closed")
class OfferNotFoundException : Exception("Offer not found")
class BidNotFoundException : Exception("Bid not found")
class BidPasswordIncorrectException : Exception("Bid password incorrect")
class BidAmountTooLowException : Exception("Bid amount too low")
class BidAmountTooHighException : Exception("Bid amount too high")
class BidAlreadyClosedException : Exception("Bid already closed")


@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(OfferClosedException::class)
    fun handleOfferClosedException(e: OfferClosedException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
    }

    @ExceptionHandler(OfferNotFoundException::class)
    fun handleOfferNotFoundException(e: OfferNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }

    @ExceptionHandler(BidNotFoundException::class)
    fun handleBidNotFoundException(e: BidNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }

    @ExceptionHandler(BidPasswordIncorrectException::class)
    fun handleBidPasswordIncorrectException(e: BidPasswordIncorrectException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
    }

    @ExceptionHandler(BidAlreadyClosedException::class)
    fun handleBidAlreadyClosedException(e: BidAlreadyClosedException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
    }


}