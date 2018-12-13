package com.dharmik.mvvmbase

import com.dharmik.mvvmarch.base.activity.BaseActivity
import com.dharmik.mvvmbase.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(MainViewModel::class.java) {

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel(viewModel: MainViewModel) {

    }

    override fun initData() {

    }
}
