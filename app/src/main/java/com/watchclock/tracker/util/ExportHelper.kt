package com.watchclock.tracker.util

import android.content.Context
import android.os.Environment
import com.watchclock.tracker.data.model.CollectionItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportHelper {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US)

    /** Exports items as CSV to the Downloads folder. Returns the file path or null on failure. */
    fun exportCsv(context: Context, items: List<CollectionItem>): String? {
        return try {
            val timestamp = dateFormat.format(Date())
            val filename = "watch_clock_collection_$timestamp.csv"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, filename)

            file.bufferedWriter().use { writer ->
                // Header
                writer.write(CSV_HEADER)
                writer.newLine()
                // Rows
                items.forEach { item ->
                    writer.write(item.toCsvRow())
                    writer.newLine()
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Exports items as JSON to the Downloads folder. Returns the file path or null on failure. */
    fun exportJson(context: Context, items: List<CollectionItem>): String? {
        return try {
            val timestamp = dateFormat.format(Date())
            val filename = "watch_clock_collection_$timestamp.json"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, filename)

            val exportData = items.map { it.toExportMap() }
            val jsonArray = buildString {
                append("[\n")
                exportData.forEachIndexed { index, map ->
                    append("  {\n")
                    map.entries.forEachIndexed { i, (k, v) ->
                        val escaped = v.replace("\\", "\\\\").replace("\"", "\\\"")
                        append("    \"$k\": \"$escaped\"")
                        if (i < map.size - 1) append(",")
                        append("\n")
                    }
                    append("  }")
                    if (index < exportData.size - 1) append(",")
                    append("\n")
                }
                append("]")
            }
            file.writeText(jsonArray)
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private val CSV_HEADER = listOf(
        "ID", "Type", "Brand", "Model", "SerialNumber", "YearOfManufacture", "Era",
        "Category", "PurchaseDate", "PurchaseCost", "CurrentValue", "InsuranceValue",
        "CosmeticCondition", "MechanicalCondition", "MovementType", "Features",
        "RepairsNeeded", "Notes", "Provenance", "Location", "Tags", "DateAdded"
    ).joinToString(",")

    private fun CollectionItem.toCsvRow(): String {
        return listOf(
            id, type.name, brand.csvEscape(), model.csvEscape(),
            serialNumber.csvEscape(), yearOfManufacture ?: "",
            era?.csvEscape() ?: "", category?.csvEscape() ?: "",
            purchaseDate?.csvEscape() ?: "", purchaseCost ?: "",
            currentValue ?: "", insuranceValue ?: "",
            cosmeticCondition?.csvEscape() ?: "", mechanicalCondition?.csvEscape() ?: "",
            movementType?.csvEscape() ?: "", features.csvEscape(),
            repairsNeeded?.csvEscape() ?: "", notes?.csvEscape() ?: "",
            provenance?.csvEscape() ?: "", location?.csvEscape() ?: "",
            tags?.csvEscape() ?: "", dateAdded
        ).joinToString(",")
    }

    private fun String.csvEscape(): String {
        return if (contains(",") || contains("\"") || contains("\n")) {
            "\"${replace("\"", "\"\"")}\""
        } else this
    }

    private fun CollectionItem.toExportMap(): Map<String, String> = mapOf(
        "id" to id.toString(),
        "type" to type.name,
        "brand" to brand,
        "model" to model,
        "serialNumber" to serialNumber,
        "yearOfManufacture" to (yearOfManufacture?.toString() ?: ""),
        "era" to (era ?: ""),
        "category" to (category ?: ""),
        "purchaseDate" to (purchaseDate ?: ""),
        "purchaseCost" to (purchaseCost?.toString() ?: ""),
        "currentValue" to (currentValue?.toString() ?: ""),
        "insuranceValue" to (insuranceValue?.toString() ?: ""),
        "cosmeticCondition" to (cosmeticCondition ?: ""),
        "mechanicalCondition" to (mechanicalCondition ?: ""),
        "movementType" to (movementType ?: ""),
        "features" to features,
        "repairsNeeded" to (repairsNeeded ?: ""),
        "notes" to (notes ?: ""),
        "provenance" to (provenance ?: ""),
        "location" to (location ?: ""),
        "tags" to (tags ?: ""),
        "dateAdded" to dateAdded.toString(),
        "lastModified" to lastModified.toString()
    )
}
