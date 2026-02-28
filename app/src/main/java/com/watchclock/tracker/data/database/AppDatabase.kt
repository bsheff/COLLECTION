package com.watchclock.tracker.data.database

import android.content.Context
import androidx.room.*
import com.watchclock.tracker.data.database.dao.*
import com.watchclock.tracker.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CollectionItem::class,
        Brand::class,
        WatchModel::class,
        Photo::class,
        ServiceHistory::class,
        ItemFeature::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun brandDao(): BrandDao
    abstract fun watchModelDao(): WatchModelDao
    abstract fun photoDao(): PhotoDao
    abstract fun serviceHistoryDao(): ServiceHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "watch_clock_tracker.db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { database ->
                                DatabaseCallback(database).populateDefaultBrands()
                            }
                        }
                    }
                })
                .build()
        }
    }
}

class Converters {
    @TypeConverter
    fun fromCollectionType(value: CollectionType): String = value.name

    @TypeConverter
    fun toCollectionType(value: String): CollectionType = CollectionType.valueOf(value)
}
