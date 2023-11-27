package tk.mcsog

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val Secret: String,
    val Type: String,
    val Bot: Long,
    val Number: Long,
    val Message: String
)
