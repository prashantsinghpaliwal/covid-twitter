package com.bigsteptech.covidtracker.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("author_id")
    val authorId: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("entities")
    val entities: Entities? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("in_reply_to_user_id")
    val inReplyToUserId: String? = null,
    @SerialName("lang")
    val lang: String? = null,
    @SerialName("possibly_sensitive")
    val possiblySensitive: Boolean? = null,
    @SerialName("referenced_tweets")
    val referencedTweets: List<ReferencedTweet>? = null,
    @SerialName("source")
    val source: String? = null,
    @SerialName("text")
    val text: String? = null
)