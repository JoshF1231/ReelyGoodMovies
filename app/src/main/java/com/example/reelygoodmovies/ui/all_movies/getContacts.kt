package com.example.reelygoodmovies.ui.all_movies

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import com.example.reelygoodmovies.data.models.Movie

fun shareMovieWithContact(context: Context, contact: String, movie: Movie) {
    val shareText = """
        Hey, check out this movie: ${movie.title}
        
        Plot: ${movie.plot}
        
        Rating: ${movie.rate}/5
    """.trimIndent()

    val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:$contact")
        putExtra("sms_body", shareText)
    }

    try {
        context.startActivity(smsIntent)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error sending message", Toast.LENGTH_SHORT).show()
    }
}

fun getContacts(context: Context): List<Pair<String, String>> {
    val contactList = mutableListOf<Pair<String, String>>()
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ),
        null, null, null
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            val number = it.getString(numberIndex)
            contactList.add(name to number)
        }
    }
    return contactList
}

fun showContactsDialog(context: Context, movie: Movie) {
    val contacts = getContacts(context)
    val contactNames = contacts.map { it.first as CharSequence }.toTypedArray()

    val builder = AlertDialog.Builder(context)
    builder.setTitle("Choose a contact to share the movie")
    builder.setItems(contactNames) { _, which ->
        val contact = contacts[which]
        shareMovieWithContact(context, contact.second, movie)
    }

    builder.setNegativeButton("Cancel") { dialog, _ ->
        dialog.dismiss()
    }
    builder.show()
}
