package com.yayarh.profits.di

import android.app.Application
import androidx.room.Room
import com.yayarh.profits.data.MyDatabase
import com.yayarh.profits.data.daos.ProductsDao
import com.yayarh.profits.data.repos.DailySalesRepo
import com.yayarh.profits.data.repos.DailySalesRepoImpl
import com.yayarh.profits.data.repos.ProductsRepo
import com.yayarh.profits.data.repos.ProductsRepoImpl
import com.yayarh.profits.data.repos.RegisterRepo
import com.yayarh.profits.data.repos.RegisterRepoImpl
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
    fun provideRegisterDao(db: MyDatabase) = db.registerDao()

    @Singleton
    @Provides
    fun provideRegisterRepo(db: MyDatabase): RegisterRepo = RegisterRepoImpl(db.registerDao())

    @Singleton
    @Provides
    fun provideDailySalesDao(db: MyDatabase) = db.dailySalesDao()

    @Singleton
    @Provides
    fun provideDailySalesRepo(db: MyDatabase): DailySalesRepo = DailySalesRepoImpl(db.dailySalesDao())
}