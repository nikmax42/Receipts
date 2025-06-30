package nikmax.shopinglist.core.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nikmax.shopinglist.core.data.room.ItemsDao
import nikmax.shopinglist.core.data.room.ListsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {
    @Singleton
    @Provides
    fun provideRepo(listsDao: ListsDao, itemsDao: ItemsDao): ShoppingRepo {
        return ShoppingRepoImpl(
            listsDao = listsDao,
            itemsDao = itemsDao
        )
    }
}
