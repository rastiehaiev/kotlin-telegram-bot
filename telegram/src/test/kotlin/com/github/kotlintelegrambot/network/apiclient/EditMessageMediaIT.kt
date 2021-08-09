package com.github.kotlintelegrambot.network.apiclient

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ReplyKeyboardRemove
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.entities.inputmedia.InputMediaVideo
import com.github.kotlintelegrambot.testutils.decode
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.Test

class EditMessageMediaIT : ApiClientIT() {

    @Test
    fun `sendMessage with chat id and mandatory params is properly sent`() {
        givenAnySendMessageResponse()

        val media = InputMediaVideo(
            media = TelegramFile.ByFileId("fileidfortest")
        )
        sut.editMessageMedia(
            chatId = ChatId.fromId(ANY_CHAT_ID),
            messageId = ANY_MESSAGE_ID,
            inlineMessageId = null,
            media = media,
            replyMarkup = ReplyKeyboardRemove(),
        ).execute()

        val request = mockWebServer.takeRequest()
        val decodedRequest = request.body.readUtf8().decode()
        println(decodedRequest)
        // val expectedRequestBody = "chat_id=$ANY_CHAT_ID&text=$ANY_TEXT"
        //assertEquals(expectedRequestBody, decodedRequest)
    }

    private fun givenAnySendMessageResponse() {
        val sendMessageResponse = """
            {
                "ok": true,
                "result": {
                    "message_id": 7,
                    "chat": {
                        "id": -1001367429635,
                        "title": "[Channel] Test Telegram Bot API",
                        "username": "testtelegrambotapi",
                        "type": "channel"
                    },
                    "date": 1604158404,
                    "text": "I'm part of a test :)",
                    "author_signature": "incognito"
                }
            }
        """.trimIndent()
        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .setBody(sendMessageResponse)
        mockWebServer.enqueue(mockedResponse)
    }

    private companion object {
        const val ANY_CHAT_ID = 235235235L
        const val ANY_CHANNEL_USERNAME = "@testtelegrambotapi"
        const val ANY_MESSAGE_ID = 35235423L
        const val ANY_TEXT = "Mucho texto"
        const val ANY_URL = "https://www.github.com/vjgarciag96"
    }
}
