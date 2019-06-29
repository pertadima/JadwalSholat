package com.jadwalshlat.www

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.jadwalshlat.www.fragment.HomeFragment
import com.jadwalshlat.www.jetpackcontroller.UserRepository
import com.jadwalshlat.www.jetpackcontroller.dagger.DaggerUserComponent
import com.jadwalshlat.www.jetpackcontroller.dagger.UserModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaggerUserComponent.builder().userModule(UserModule(this, "https://time.siswadi.com/"))
                .build().inject(this)
        changeFragmentOne(HomeFragment(), "HomeFragment")

    }

    fun changeFragmentOne(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_activity_main_1, fragment, tag)
            .commitAllowingStateLoss()

    }

    fun popUp(messages: String) {
        Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()

    }

    fun startLoading() {
        frameLayout_activity_main_2.visibility = View.VISIBLE

    }

    fun stopLoading() {
        frameLayout_activity_main_2.visibility = View.GONE

    }
}
