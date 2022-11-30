package com.example.lab1


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class AddressBook(val id: Long, val name: String, val secName:String, val phone: String, val birthday: String)

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "adressbook"
        const val TABLE_NAME = "contacts"
        const val KEY_ID = "id"
        const val KEY_NAME = "NAME"
        const val KEY_SECONDNAME = "SECNAME"
        const val KEY_PHONE = "PHONE"
        const val KEY_BIRTHDAY = "BIRTHDAY"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_NAME (
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_NAME TEXT NOT NULL,
                $KEY_SECONDNAME TEXT NOT NULL,
                $KEY_BIRTHDAY DATE NOT NULL,
                $KEY_PHONE TEXT NOT NULL
            )""")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAll(): List<AddressBook> {
        val result = mutableListOf<AddressBook>()
        val database = this.writableDatabase
        val cursor: Cursor = database.query(
            TABLE_NAME, null, null, null,
            null, null, null
        )
        if (cursor.moveToFirst()) {
            val idIndex: Int = cursor.getColumnIndex(KEY_ID)
            val nameIndex: Int = cursor.getColumnIndex(KEY_NAME)
            val secNameIndex: Int = cursor.getColumnIndex(KEY_SECONDNAME)
            val phoneIndex: Int = cursor.getColumnIndex(KEY_PHONE)
            val birthdayIndex: Int = cursor.getColumnIndex(KEY_BIRTHDAY)
            do {
                val contact = AddressBook(
                    cursor.getLong(idIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(secNameIndex),
                    cursor.getString(phoneIndex),
                    cursor.getString(birthdayIndex)
                )
                result.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }

    fun addNote(name: String, secName:String, phone: String, birthday: String): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, name)
        contentValues.put(KEY_SECONDNAME, secName)
        contentValues.put(KEY_PHONE, phone)
        contentValues.put(KEY_BIRTHDAY, birthday)
        val id = database.insert(TABLE_NAME, null, contentValues)
        close()
        return id
    }

    fun updateNote(id: Int, name: String, secName: String, phone: String, birthday: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, name)
        contentValues.put(KEY_SECONDNAME, secName)
        contentValues.put(KEY_PHONE, phone)
        contentValues.put(KEY_BIRTHDAY, birthday)
        database.update(TABLE_NAME, contentValues, "$KEY_ID = ?", arrayOf(id.toString()))
        close()
    }

    fun removeNote(id: Int) {
        val database = this.writableDatabase
        database.delete(TABLE_NAME, "$KEY_ID = ?", arrayOf(id.toString()))
        close()
    }

}
