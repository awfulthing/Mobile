package com.example.lab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class ViewActivity : AppCompatActivity() {
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewSecname = findViewById<TextView>(R.id.textViewSecname)
        val textViewBirthday = findViewById<TextView>(R.id.textViewBirthday)
        val textViewPhone = findViewById<TextView>(R.id.textViewPhone)

        val buttonCancel = findViewById<Button>(R.id.buttonCancel)
        val buttonChange = findViewById<Button>(R.id.buttonChange)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        var id = intent.getStringExtra("ID")?.toInt()
        if (id == null) id = 0
        val name = intent.getStringExtra("NAME")
        val surname = intent.getStringExtra("SECNAME")
        val dateOfBirth = intent.getStringExtra("BIRTHDAY")
        val phoneNumber = intent.getStringExtra("PHONE")

        textViewName.setText("${textViewName.text} ${name}")
        textViewSecname.setText("${textViewSecname.text} ${surname}")
        textViewBirthday.setText("${textViewBirthday.text} ${dateOfBirth}")
        textViewPhone.setText("${textViewPhone.text} ${phoneNumber}")

        val returnIntent = Intent()
        buttonCancel.setOnClickListener {
            returnIntent.putExtra("RESULT", "NOTHING")
            setResult(RESULT_OK, returnIntent)
            this.finish()
        }
        buttonDelete.setOnClickListener {
            dbHelper.removeNote(id)
            returnIntent.putExtra("RESULT", "CHANGES")
            setResult(RESULT_OK, returnIntent)
            this.finish()
        }
        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val result = it?.data?.extras?.get("RESULT") as String
            returnIntent.putExtra("RESULT", result)
            setResult(RESULT_OK, returnIntent)
            this.finish()
        }
        buttonChange.setOnClickListener {
            val intent = Intent(this, AddChangeActivity::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("NAME", name)
            intent.putExtra("SECNAME", surname)
            intent.putExtra("BIRTHDAY", dateOfBirth)
            intent.putExtra("PHONE", phoneNumber)
            getResult.launch(intent)
        }
    }
}