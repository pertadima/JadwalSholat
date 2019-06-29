package com.jadwalshlat.www.jetpackcontroller.dagger

import com.jadwalshlat.www.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [UserModule::class])
@Singleton
interface UserComponent {
    fun inject(mainActivity: MainActivity)

}