package com.juiceroll.domain.model

import kotlinx.serialization.Serializable

enum class RollType {
    STANDARD, FATE
}

@Serializable
sealed class RollResult {
    abstract val type: RollType
    abstract val timestamp: Long

    @Serializable
    data class StandardRollResult(
        val values: List<Int>,
        val sides: Int,
        val total: Int,
        val dropped: List<Int> = emptyList(),
        val advantage: Boolean = false,
        val disadvantage: Boolean = false,
        override val timestamp: Long = System.currentTimeMillis()
    ) : RollResult() {
        override val type: RollType get() = RollType.STANDARD
    }

    @Serializable
    data class FateRollResult(
        val values: List<Int>,
        val total: Int,
        override val timestamp: Long = System.currentTimeMillis()
    ) : RollResult() {
        override val type: RollType get() = RollType.FATE
    }

    @Serializable
    data class InvalidExpression(
        val expression: String,
        override val timestamp: Long = System.currentTimeMillis()
    ) : RollResult() {
        override val type: RollType get() = RollType.STANDARD
        val total: Int get() = 0
        val values: List<Int> get() = emptyList()
    }
}
