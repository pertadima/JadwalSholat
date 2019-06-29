package com.jadwalshlat.www.jetpackcontroller.dagger

import android.arch.persistence.room.Room
import android.content.Context
import com.jadwalshlat.www.jetpackcontroller.UserDatabase
import com.jadwalshlat.www.jetpackcontroller.UserRepository
import com.jadwalshlat.www.jetpackcontroller.Webservice
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@Singleton
class UserModule(private val context: Context, private val mUrl: String) {
    @Provides
    @Singleton
    fun getRespository(): UserRepository {
        return UserRepository(
            Webservice.create(mUrl),
            Room.databaseBuilder(
                context,
                UserDatabase::class.java,
                "report_maker"
            ).build().userDao())

    }

}