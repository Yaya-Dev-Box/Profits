package com.yayarh.profits.di

import android.app.Application
import androidx.room.Room
import com.yayarh.profits.data.MyDatabase
import com.yayarh.profits.data.daos.ProductsDao
import com.yayarh.profits.data.repos.ProductsRepo
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
    fun provideProductsRepo(db: MyDatabase): ProductsRepo = ProductsRepo(db.productsDao())

}