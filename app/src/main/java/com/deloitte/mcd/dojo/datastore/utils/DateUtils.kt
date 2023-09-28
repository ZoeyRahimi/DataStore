package com.deloitte.mcd.dojo.datastore.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)


    fun convertDateFormat(inputDate: String): String {
        return try {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val outputDateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
            val date = inputDateFormat.parse(inputDate)
            outputDateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            "" // Return an empty string in case of an error
        }
    }

    fun dateFrom(date: String): Date = simpleDateFormat.parse(date)
}