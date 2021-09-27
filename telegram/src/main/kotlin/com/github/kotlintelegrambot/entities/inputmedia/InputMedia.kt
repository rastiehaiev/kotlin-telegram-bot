package com.github.kotlintelegrambot.entities.inputmedia

import com.github.kotlintelegrambot.entities.TelegramFile
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/**
 * Represents the content of a media message to be sent.
 * https://core.telegram.org/bots/api#inputmedia
 */
sealed class InputMedia {
    abstract val type: String
    abstract val media: TelegramFile
    abstract val caption: String?
    abstract val parseMode: String?
}

/**
 * Interface to mark all the media types that can be sent within a group of media for
 * operations like `sendMediaGroup`.
 */
interface GroupableMedia

class MediaGroup private constructor(val medias: Array<out GroupableMedia>) {
    init {
        if (!(2..10).contains(medias.size)) {
            throw IllegalArgumentException("media groups must include 2-10 items")
        }
    }

    companion object {
        fun from(vararg media: GroupableMedia): MediaGroup = MediaGroup(media)
    }
}

/**
 * Represents a photo to be sent. Can be sent as part of a group of media.
 * https://core.telegram.org/bots/api#inputmediaphoto
 */
data class InputMediaPhoto(
    @SerializedName(InputMediaFields.MEDIA) override val media: TelegramFile,
    @SerializedName(InputMediaFields.CAPTION) override val caption: String? = null,
    @SerializedName(InputMediaFields.PARSE_MODE) override val parseMode: String? = null,
) : InputMedia(), GroupableMedia {
    override val type: String
        get() = InputMediaTypes.PHOTO

    override fun toString(): String {
        return JsonObject().also {
            val media = this.media
            val fileId = media as? TelegramFile.ByFileId
            val fileUrl = media as? TelegramFile.ByUrl
            it.addProperty("media", fileId?.fileId ?: fileUrl?.url ?: "")
            it.addProperty("type", type)
            if (caption != null) {
                it.addProperty("caption", caption)
            }
        }.toString()
    }
}

/**
 * Represents a video to be sent. Can be sent as part of a group of media.
 * https://core.telegram.org/bots/api#inputmediavideo
 */
data class InputMediaVideo(
    @SerializedName(InputMediaFields.MEDIA) override val media: TelegramFile,
    @SerializedName(InputMediaFields.CAPTION) override val caption: String? = null,
    @SerializedName(InputMediaFields.PARSE_MODE) override val parseMode: String? = null,
    @SerializedName(InputMediaFields.THUMB) val thumb: TelegramFile.ByFile? = null,
    @SerializedName(InputMediaFields.WIDTH) val width: Int? = null,
    @SerializedName(InputMediaFields.HEIGHT) val height: Int? = null,
    @SerializedName(InputMediaFields.DURATION) val duration: Int? = null,
    @SerializedName(InputMediaFields.SUPPORTS_STREAMING) val supportsStreaming: Boolean? = null,
) : InputMedia(), GroupableMedia {

    override fun toString(): String {
        return JsonObject().also {
            val media = this.media
            val fileId = media as? TelegramFile.ByFileId
            val fileUrl = media as? TelegramFile.ByUrl
            it.addProperty("media", fileId?.fileId ?: fileUrl?.url ?: "")
            it.addProperty("type", type)
        }.toString()
    }

    override val type: String
        get() = InputMediaTypes.VIDEO
}

/**
 * Represents an animation file (GIF or H.264/MPEG-4 AVC video without sound) to be sent.
 * https://core.telegram.org/bots/api#inputmediaanimation
 */
data class InputMediaAnimation(
    @SerializedName(InputMediaFields.MEDIA) override val media: TelegramFile,
    @SerializedName(InputMediaFields.CAPTION) override val caption: String? = null,
    @SerializedName(InputMediaFields.PARSE_MODE) override val parseMode: String? = null,
    @SerializedName(InputMediaFields.THUMB) val thumb: TelegramFile.ByFile? = null,
    @SerializedName(InputMediaFields.WIDTH) val width: Int? = null,
    @SerializedName(InputMediaFields.HEIGHT) val height: Int? = null,
    @SerializedName(InputMediaFields.DURATION) val duration: Int? = null,
) : InputMedia() {
    override val type: String
        get() = InputMediaTypes.ANIMATION
}

/**
 * Represents an audio file to be treated as music to be sent.
 * https://core.telegram.org/bots/api#inputmediaaudio
 */
data class InputMediaAudio(
    @SerializedName(InputMediaFields.MEDIA) override val media: TelegramFile,
    @SerializedName(InputMediaFields.CAPTION) override val caption: String? = null,
    @SerializedName(InputMediaFields.PARSE_MODE) override val parseMode: String? = null,
    @SerializedName(InputMediaFields.THUMB) val thumb: TelegramFile.ByFile? = null,
    @SerializedName(InputMediaFields.DURATION) val duration: Int? = null,
    @SerializedName(InputMediaFields.PERFORMER) val performer: String? = null,
    @SerializedName(InputMediaFields.TITLE) val title: String? = null,
) : InputMedia(), GroupableMedia {
    override val type: String
        get() = InputMediaTypes.AUDIO
}

/**
 * Represents a general file to be sent.
 * https://core.telegram.org/bots/api#inputmediadocument
 */
data class InputMediaDocument(
    @SerializedName(InputMediaFields.MEDIA) override val media: TelegramFile,
    @SerializedName(InputMediaFields.CAPTION) override val caption: String? = null,
    @SerializedName(InputMediaFields.PARSE_MODE) override val parseMode: String? = null,
    @SerializedName(InputMediaFields.THUMB) val thumb: TelegramFile.ByFile? = null,
) : InputMedia(), GroupableMedia {
    override val type: String
        get() = InputMediaTypes.DOCUMENT
}
