package nikmax.shopinglist.core.data.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ShoppingDb {
        return Room.databaseBuilder(
            context = context,
            klass = ShoppingDb::class.java,
            name = "shopping_db"
        ).build()
    }
    
    @Singleton
    @Provides
    fun provideListsDao(database: ShoppingDb): ListsDao {
        return database.listsDao()
    }
    
    @Singleton
    @Provides
    fun provideItemsDao(database: ShoppingDb): ItemsDao {
        return database.itemsDao()
    }
}
