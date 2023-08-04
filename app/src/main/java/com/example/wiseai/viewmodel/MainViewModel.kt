package com.example.wiseai.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wiseai.data.CompletionRequest
import com.example.wiseai.data.CompletionResponse
import com.example.wiseai.data.Message
import com.example.wiseai.data.Message.Companion.SENT_BY_BOT
import com.example.wiseai.service.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel: ViewModel() {
    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList :MutableLiveData<MutableList<Message>> get() = _messageList

    init {
        _messageList.value = mutableListOf()
    }

    fun addToChat(message: String, sentBy: String, timestamp: String) {
        val currentList = _messageList.value?: mutableListOf()
        currentList.add(Message(message, sentBy, timestamp))
        _messageList.postValue(currentList)
    }

    fun addResponse(response: String) {
        _messageList.value?.removeAt(_messageList.value?.size?.minus(1) ?: 0)
        addToChat(response,Message.SENT_BY_BOT,getCurrentTimestamp())
    }

    fun callApi(question: String) {
        addToChat("Typing....", Message.SENT_BY_BOT, getCurrentTimestamp())

        val completionResquest = CompletionRequest(
            model = "text-davinci-003",
            prompt = question,
            max_tokens = 4000
        )

        viewModelScope.launch {
            try {
                val response = APIService.apiService.getResponse(completionResquest)
                handleApiResponse(response)
            } catch (e : SocketTimeoutException) {
                addResponse("Timeout : $e")
            }
        }
    }

    private suspend fun handleApiResponse(response: Response<CompletionResponse>) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.text

                    if (result != null) {
                        addResponse(result.trim())
                    } else {
                        addResponse("No choices found")
                    }
                }
            } else {
                addResponse("Failed to get response ${response.errorBody()}")
            }
        }
    }

    fun getCurrentTimestamp(): String {
        return  SimpleDateFormat("hh mm a", Locale.getDefault()).format(Date())
    }
}