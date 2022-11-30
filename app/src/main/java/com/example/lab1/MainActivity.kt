package com.example.lab1

import android.content.Intent
import android.media.VolumeShaper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*

class MainActivity : AppCompatActivity() {
    private val listMain = mutableListOf<AddressBook>()
    private val listShow = mutableListOf<AddressBook>()
    private lateinit var adapter: RecyclerAdapter
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAdd = findViewById<Button>(R.id.button)
        val textInput = findViewById<TextInputEditText>(R.id.textInputEditText)
        val buttonSearch = findViewById<Button>(R.id.buttonSearch)

        listMain.addAll(dbHelper.getAll())
        listShow.addAll(listMain)

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val result = it?.data?.extras?.get("RESULT") as String
            if (result == "CHANGES") {
                textInput.setText("")
                for (line in listMain) {
                    adapter.notifyItemRemoved(listShow.indexOf(line))
                    listShow.remove(line)
                }
                listMain.clear()
                listMain.addAll(dbHelper.getAll())
                for (line in listMain){
                    listShow.add(line)
                    adapter.notifyItemInserted(listShow.lastIndex)
                }
            }
        }

        adapter = RecyclerAdapter(listShow, {
            val intent = Intent(this, ViewActivity::class.java)
            intent.putExtra("ID", listMain[it].id.toString())
            intent.putExtra("NAME", listMain[it].name)
            intent.putExtra("SECNAME", listMain[it].secName)
            intent.putExtra("BIRTHDAY", listMain[it].birthday)
            intent.putExtra("PHONE", listMain[it].phone)
            getResult.launch(intent)
        }, {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" + listMain[it].phone)
            startActivity(callIntent)
        })

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        buttonAdd.setOnClickListener {
            val intent = Intent(this, AddChangeActivity::class.java)
            intent.putExtra("ID", -1)
            getResult.launch(intent)
        }

        textInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            var text = textInput.text.toString()
            if (text.isEmpty()) text = " "
            for (line in listMain)
                if (!"${line.name} ${line.secName} ${line.birthday} ${line.phone}".contains(text,true)) {
                    if (listShow.contains(line)){
                        adapter.notifyItemRemoved(listShow.indexOf(line))
                        listShow.remove(line)
                    }
                } else if (!listShow.contains(line)){
                    listShow.add(line)
                    adapter.notifyItemInserted(listShow.lastIndex)
                }
        }
    })
}}

