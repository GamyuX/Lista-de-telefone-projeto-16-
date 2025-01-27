package dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.projeto_16.Model.ContactModel
import com.example.projeto_16.Model.userModel

object DBHelper {

    private const val DATABASE_NAME = "myDatabase.db"
    private const val DATABASE_VERSION = 1

    private var instance: DatabaseHelper? = null

    fun getInstance(): DatabaseHelper {
        if (instance == null) {
            instance = DatabaseHelper()
        }
        return instance!!
    }
    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        val sql = arrayOf (

            "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)",
            "INSERT INTO users (username, password) VALUES ('ADM', 'qwe123')",
            "CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, email TEXT, phone INTEGER, imageId INTEGER)",
            "INSERT INTO contact (name, address, email, phone, imageId) VALUES ('Biel','address gabriel','gabriel.br@gmail.com', 55 - 50265123, -1)",
            "INSERT INTO contact (name, address, email, phone, imageId) VALUES ('pedro','address pedro','pedro.br@gmail.com', 55 - 12349876, -1)",

            )
        override fun onCreate(db: SQLiteDatabase) {
            sql.forEach {
                db.execSQL (it)
            }
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            TODO("Not yet implemented")
        }

        /* ----------------------------------------------------------------------------------------
                                                CRUD USERS
        ------------------------------------------------------------------------------------------- */

        fun insertUser (username: String, password: String): Long {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("username", username)
            contentValues.put("password", password)
            val res = db.insert("users", null, contentValues)
            db.close()
            return res
        }

        fun updateUser(id: Int, username: String, password: String): Int {

            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("username", username)
            contentValues.put("password", password)
            val res = db.update("users", contentValues, "id = ?", arrayOf(id.toString()))
            db.close()
            return res
        }

        fun deleteUser(id: Int): Int {
            val db = this.writableDatabase
            val res = db.delete("users", "id = ?", arrayOf(id.toString()))
            db.close()
            return res
        }

        fun getUser (username: String, password: String): userModel {
            val db = this.readableDatabase
            val c = db.rawQuery(
                "SELECT * FROM users WHERE username = ? AND password = ?",
                arrayOf(username, password)
            )

            var userModel = userModel()

            if (c.count==1){
                c.moveToFirst()
                val idIndex = c.getColumnIndex("id")
                val usernameIndex = c.getColumnIndex("username")
                val passwordIndex = c.getColumnIndex("password")

                userModel = userModel(
                    id=c.getInt(idIndex),
                    username=c.getString(usernameIndex),
                    password=c.getString(passwordIndex)
                )
            }
            db.close()
            return userModel
        }

        fun login(username: String, password: String): Boolean {
            val db = this.readableDatabase
            val c = db.rawQuery(
                "SELECT * FROM users WHERE username = ? AND password = ?",
                arrayOf(username, password)
            )
            var userModel = userModel()

            return if (c.count == 1) {
                db.close()
                true
            } else {
                db.close()
                return false
            }
        }
        /* ----------------------------------------------------------------------------------------
                                               CRUD CONTACT
       ------------------------------------------------------------------------------------------- */
        fun insertContact (name: String, email:String, address:String, phone:Int, imageId: Int): Long {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("name", name)
            contentValues.put("email", email)
            contentValues.put("address", address)
            contentValues.put("phone", phone)
            contentValues.put("imageId", imageId)
            val res = db.insert("contact", null, contentValues)
            db.close()
            return res
        }

        fun updateContact(id: Int,name: String, email:String,address:String, phone:Int, imageId: Int): Int {

            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("name", name)
            contentValues.put("email", email)
            contentValues.put("address", address)
            contentValues.put("phone", phone)
            contentValues.put("imageId", imageId)
            val res = db.update("contact", contentValues, "id = ?", arrayOf(id.toString()))
            db.close()
            return res
        }

        fun deleteContact(id: Int): Int {
            val db = this.writableDatabase
            val res = db.delete("contact", "id = ?", arrayOf(id.toString()))
            db.close()
            return res
        }

        fun getContact (id: Int): ContactModel {
            val db = this.readableDatabase
            val c = db.rawQuery(
                "SELECT * FROM contact WHERE id = ?",
                arrayOf(DBHelper.toString())
            )

            var contactsModel = ContactModel()

            if (c.count==1){
                c.moveToFirst()
                val idIndex = c.getColumnIndex("id")
                val nameIndex = c.getColumnIndex("name")
                val addressIndex = c.getColumnIndex("address")
                val emailIndex = c.getColumnIndex("email")
                val phoneIndex = c.getColumnIndex("phone")
                val imageIdIndex = c.getColumnIndex("imageId")

                contactsModel = ContactModel(
                    id=c.getInt(idIndex),
                    name=c.getString(nameIndex),
                    address=c.getString(addressIndex),
                    email=c.getString(emailIndex),
                    phone=c.getInt(phoneIndex),
                    imageId=c.getInt(imageIdIndex)
                )
            }
            db.close()
            return contactsModel
        }

        fun getAllContact(): ArrayList <ContactModel> {
            val db = this.readableDatabase
            val c = db.rawQuery("SELECT * FROM contact",null)
            var listContactsModel = ArrayList <ContactModel>()

            if (c.count > 0){
                c.moveToFirst()
                val idIndex = c.getColumnIndex("id")
                val nameIndex = c.getColumnIndex("name")
                val emailIndex = c.getColumnIndex("email")
                val addressIndex = c.getColumnIndex("address")
                val phoneIndex = c.getColumnIndex("phone")
                val imageIdIndex = c.getColumnIndex("imageId")
                do {
                    val contactsModel = ContactModel(
                        id=c.getInt(idIndex),
                        name=c.getString(nameIndex),
                        email=c.getString(emailIndex),
                        address=c.getString(addressIndex),
                        phone=c.getInt(phoneIndex),
                        imageId=c.getInt(imageIdIndex)
                    )
                    listContactsModel.add(contactsModel)
                }while (c.moveToNext())
            }
            db.close()
            return listContactsModel
        }
    }

}