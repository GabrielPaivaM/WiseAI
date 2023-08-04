package com.example.wiseai.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wiseai.adapter.MessageAdapter
import com.example.wiseai.data.Message
import com.example.wiseai.databinding.ActivityMainBinding
import com.example.wiseai.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val llm = LinearLayoutManager(this)
        binding.recChat.layoutManager = llm

        noLimitScreen()
        observeMessageList()
        onSendButtonClick()

    }

    fun noLimitScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    fun onSendButtonClick() {
        binding.btnSend.setOnClickListener {
            val question = binding.messageEditText.text.toString().trim()
            binding.txtWelcome.setText("")

            if (question.isNotEmpty()) {
                mainViewModel.addToChat(question, Message.SENT_BY_ME, mainViewModel.getCurrentTimestamp())
                binding.messageEditText.setText("")
                mainViewModel.callApi(question)
            }

        }
    }

    fun observeMessageList() {
        mainViewModel.messageList.observe(this){ messages->
            var adapter = MessageAdapter(messages)
            binding.recChat.adapter = adapter
        }
    }
}