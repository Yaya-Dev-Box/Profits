package com.yayarh.profits.di

import android.app.Application
import androidx.room.Room
import com.yayarh.profits.data.MyDatabase
import com.yayarh.profits.data.daos.ProductsDao
import com.yayarh.profits.data.repos.base.DailySalesRepo
import com.yayarh.profits.data.repos.imps.DailySalesRepoImpl
import com.yayarh.profits.data.repos.base.OrdersRepo
import com.yayarh.profits.data.repos.imps.OrdersRepoImpl
import com.yayarh.profits.data.repos.base.ProductsRepo
import com.yayarh.profits.data.repos.imps.ProductsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(ctx: Application): MyDatabase = Room.databaseBuilder(ctx, MyDatabase::class.java, "profits").build()

    @Singleton
    @Provides
    fun provideProductsDao(db: MyDatabase): ProductsDao = db.productsDao()

    @Singleton
    @Provides
    fun provideProductsRepo(db: MyDatabase): ProductsRepo = ProductsRepoImpl(db.productsDao())

    @Singleton
    @Provides
    fun provideDailySalesDao(db: MyDatabase) = db.dailySalesDao()

    @Singleton
    @Provides
    fun provideDailySalesRepo(db: MyDatabase): DailySalesRepo = DailySalesRepoImpl(db.dailySalesDao())

    @Singleton
    @Provides
    fun provideOrdersDao(db: MyDatabase) = db.ordersDao()

    @Singleton
    @Provides
    fun provideOrdersRepo(db: MyDatabase): OrdersRepo = OrdersRepoImpl(db.ordersDao())
}