package com.example.lab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AddChangeActivity : AppCompatActivity() {
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_change)

        val id = intent.getIntExtra("ID", -1)

        val editTextName = findViewById<TextInputEditText>(R.id.editTextName)
        val editTextSurname = findViewById<TextInputEditText>(R.id.editTextSecname)
        val editTextDate = findViewById<TextInputEditText>(R.id.editTextBirthday)
        val editTextPhone = findViewById<TextInputEditText>(R.id.editTextPhone)
        val buttonConfirm = findViewById<Button>(R.id.buttonConfirm)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        if (id != -1) {
            editTextName.setText(intent.getStringExtra("NAME"))
            editTextSurname.setText(intent.getStringExtra("SURNAME"))
            editTextDate.setText(intent.getStringExtra("DATEOFBIRTH"))
            editTextPhone.setText(intent.getStringExtra("PHONENUMBER"))
        }

        val returnIntent = Intent()

        buttonBack.setOnClickListener {
            returnIntent.putExtra("RESULT", "NOTHING")
            setResult(RESULT_OK, returnIntent)
            this.finish()
        }

        buttonConfirm.setOnClickListener {
            if (!editTextName.text.isNullOrEmpty() &&
                !editTextSurname.text.isNullOrEmpty() &&
                !editTextDate.text.isNullOrEmpty() &&
                !editTextPhone.text.isNullOrEmpty()) {
                val name = editTextName.text.toString()
                val secname = editTextSurname.text.toString()
                val birthday = editTextDate.text.toString()
                val phone = editTextPhone.text.toString()
                if (id == -1) {
                    dbHelper.addNote(name, secname, birthday, phone)
                    returnIntent.putExtra("RESULT", "CHANGES")
                    setResult(RESULT_OK, returnIntent)
                }
                else {
                    dbHelper.updateNote(id, name, secname, birthday, phone)
                    returnIntent.putExtra("RESULT", "CHANGES")
                    setResult(RESULT_OK, returnIntent)
                }
                this.finish()
            }
            else Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }
}