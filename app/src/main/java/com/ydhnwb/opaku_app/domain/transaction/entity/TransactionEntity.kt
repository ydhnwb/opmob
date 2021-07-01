package com.ydhnwb.opaku_app.domain.transaction.entity

data class TransactionEntity(
    val id : Int, val name : String, val price: Int, val image : String, val user : TransactionUserEntity
)