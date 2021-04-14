package com.mads2202.nerdlauncher

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class SingleFragmentActivity: AppCompatActivity() {
    protected abstract fun createFragment(): Fragment?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        var mFragmentManager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = mFragmentManager.findFragmentById(com.mads2202.nerdlauncher.R.id.fragments_container);
        if (fragment == null) {
            fragment = createFragment();
            mFragmentManager.beginTransaction()
                .add(com.mads2202.nerdlauncher.R.id.fragments_container, fragment!!)
                .commit();
        }
    }
    @LayoutRes
    fun getLayoutResId():Int{
        return com.mads2202.nerdlauncher.R.layout.activity_fragment
    }
}