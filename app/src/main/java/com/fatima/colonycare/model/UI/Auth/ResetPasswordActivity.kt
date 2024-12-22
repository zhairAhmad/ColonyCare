package com.fatima.colonycare.model.UI.Auth

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.fatima.colonycare.R
import com.fatima.colonycare.databinding.ActivityResetPasswordBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    lateinit var binding: ActivityResetPasswordBinding
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = AuthViewModel()


        progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)


        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                progressDialog.dismiss()
                if (it!=null) {
                    Toast.makeText(this@ResetPasswordActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.resetResponse.collect {
                progressDialog.dismiss()
                if (it != null) {
                    val builder = MaterialAlertDialogBuilder(this@ResetPasswordActivity)
                    builder.setMessage("Password reset link sent to your email")
                    builder.setCancelable(false)
                    builder.setPositiveButton(
                        "Ok",
                        DialogInterface.OnClickListener { dialog, which ->
                            finish()
                        })
                    builder.show()
                }
            }
        }

        binding.signupTxt.setOnClickListener {
            finish()
        }


        binding.loginbtn.setOnClickListener {
            val email = binding.email.editText?.text.toString()


            if (!email.contains("@")) {
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressDialog.show()

            viewModel.resetPassword(email)



        }


    }
}