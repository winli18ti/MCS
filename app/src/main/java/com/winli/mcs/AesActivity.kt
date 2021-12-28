package com.winli.mcs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.winli.mcs.algorithm.AES
import com.winli.mcs.databinding.ActivityAesBinding

class AesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Advanced Encryption Standard"

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        with(binding) {
            btnEncrypt.setOnClickListener {
                val input = etInput.text.toString()
                val key = etKey.text.toString()
                if (isEmpty(input, key)) {
                    return@setOnClickListener
                } else {
                    val output = AES.encrypt(input, key)
                    etOutput.setText(output)
                }
            }

            btnDecrypt.setOnClickListener {
                val input = etInput.text.toString()
                val key = etKey.text.toString()
                if (isEmpty(input, key)) {
                    return@setOnClickListener
                } else {
                    val output = AES.decrypt(input, key)
                    if (output.isEmpty()) {
                        Snackbar.make(
                            it,
                            "Input Text or Key is invalid for decryption.",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        etOutput.setText(output)
                    }
                }
            }

            btnCopy.setOnClickListener {
                val text = etOutput.text
                if (text.isNullOrEmpty()) {
                    Snackbar.make(
                        it,
                        "Output Text is empty.",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                } else {
                    val clip = ClipData.newPlainText("Copied Text", text)
                    clipboard.setPrimaryClip(clip)
                    Snackbar.make(
                        it,
                        "Output Text has been copied. Press Paste to put it on Input Text.",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }

            }

            btnPaste.setOnClickListener {
                val clipData: ClipData? = clipboard.primaryClip

                clipData?.apply {
                    val text = this.getItemAt(0).text.toString().trim()
                    etInput.setText(text)
                }
            }

            btnClear.setOnClickListener {
                etInput.setText("")
                etKey.setText("")
                etOutput.setText("")
            }

            titleEncrypt.setOnClickListener {
                TransitionManager.beginDelayedTransition(clEncrypt, AutoTransition())
                TransitionManager.beginDelayedTransition(clDecrypt, AutoTransition())
                if (expandEncrypt.visibility == View.VISIBLE) {
                    expandEncrypt.visibility = View.GONE
                    ibEncrypt.setImageResource(R.drawable.ic_down)
                } else {
                    expandEncrypt.visibility = View.VISIBLE
                    ibEncrypt.setImageResource(R.drawable.ic_up)
                }
            }

            titleDecrypt.setOnClickListener {
                TransitionManager.beginDelayedTransition(clDecrypt, AutoTransition())
                if (expandDecrypt.visibility == View.VISIBLE) {
                    expandDecrypt.visibility = View.GONE
                    ibDecrypt.setImageResource(R.drawable.ic_down)
                } else {
                    expandDecrypt.visibility = View.VISIBLE
                    ibDecrypt.setImageResource(R.drawable.ic_up)
                }
            }
        }
    }

    private fun isEmpty(input: String, key: String): Boolean {
        if (input.isEmpty()) {
            Snackbar.make(
                binding.root,
                "Input Text is empty.",
                Snackbar.LENGTH_SHORT
            )
                .show()
            return true
        }
        if (key.isEmpty()) {
            Snackbar.make(
                binding.root,
                "Key is empty.",
                Snackbar.LENGTH_SHORT
            )
                .show()
            return true
        }
        return false
    }
}