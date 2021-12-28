package com.winli.mcs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.winli.mcs.algorithm.RSA
import com.winli.mcs.databinding.ActivityRsaBinding

class RsaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRsaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRsaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Rivest Shamir Adleman"

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        with(binding) {
            btnEncrypt.setOnClickListener {
                val input = etInput.text.toString()
                if (isEmpty(input)) {
                    return@setOnClickListener
                } else {
                    val output = RSA.encrypt(input)
                    etModulus.setText(RSA.showModulus())
                    if (output.isEmpty()) {
                        Snackbar.make(
                            it,
                            "New Public and Private Key have been generated.\nHowever, Input Text is invalid for encryption.",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        etOutput.setText(output)
                    }
                }
            }

            btnDecrypt.setOnClickListener {
                val input = etInput.text.toString()
                if (isEmpty(input)) {
                    return@setOnClickListener
                } else {
                    val output = RSA.decrypt(input)
                    if (output.isEmpty()) {
                        Snackbar.make(
                            it,
                            "Input Text is invalid for decryption.",
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

    private fun isEmpty(input: String): Boolean {
        if (input.isEmpty()) {
            Snackbar.make(
                binding.root,
                "Input Text is empty.",
                Snackbar.LENGTH_SHORT
            )
                .show()
            return true
        }
        return false
    }
}