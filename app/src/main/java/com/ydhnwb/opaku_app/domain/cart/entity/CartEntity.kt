package com.ydhnwb.opaku_app.domain.cart.entity

data class CartEntity(
    val id: Int,
    val product: CartProductEntity,
    val user: CartUserEntity
)