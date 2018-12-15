package com.dharmik.mvvmarch.base.activity

import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.transition.Transition
import android.view.View
import com.dharmik.mvvmarch.base.fragment.BaseFragment
import com.dharmik.mvvmarch.base.viewmodel.BaseViewModel
import com.dharmik.mvvmarch.utils.SnackbarMessage
import android.view.WindowManager




abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) :
    AppCompatActivity(), BaseFragment.Callback, IBaseActivity {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    val binding by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes()) as DB
    }

    private fun getViewModel(viewmodel: Class<VM>): VM {
        return ViewModelProviders.of(this).get(viewmodel)
    }

    val viewModel by lazy {
        getViewModel(mViewModelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel(viewModel)
        super.onCreate(savedInstanceState)
        viewModel.apply {
            lifecycle.addObserver(this)
        }
        registerUIChangeLiveDataCallBack()
        initData()
    }

    /**
     *
     *  You need to override this method.
     *  And you need to set viewModel to binding: binding.viewModel = viewModel
     *
     */
    abstract fun initViewModel(viewModel: VM)

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //do nothing
    }

    private fun registerUIChangeLiveDataCallBack() {
       /* viewModel.snackbarMessage.observe(this,
            { message: String ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            } as SnackbarMessage.SnackbarObserver)*/

        viewModel.let {
            it.getUC().getShowProgressLiveData().observe(this, Observer<Boolean> {

            })
            it.getUC().getHideProgressLiveData().observe(this, Observer<Boolean> {

            })
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun getLastNotNull(list: List<Fragment>): Fragment? {
        list.indices.reversed()
            .map { list[it] }
            .forEach {
                return it
            }
        return null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getTopFragment()?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * function to show the fragment
     *
     * @param name fragment to be shown
     */
    fun showFragment(name: Fragment, layout: Int) {
        val fragmentManager = supportFragmentManager
        // check if the fragment is in back stack
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(layout, name, name.javaClass.name)
        fragmentTransaction.addToBackStack(name.javaClass.name)
        fragmentTransaction.commitAllowingStateLoss()
    }

    /**
     * function to show the fragment
     *
     * @param current current visible fragment
     * @param newFragment next visible fragment
     * @param sharedView view which show the transition
     *
     */
    fun showFragmentWithTransition(current: Fragment, newFragment: Fragment, sharedView: View, layout: Int) {
        val fragmentManager = supportFragmentManager
        // check if the fragment is in back stack
        val fragmentPopped = fragmentManager.popBackStackImmediate(newFragment.javaClass.name, 0)
        if (fragmentPopped) {
            // fragment is pop from backStack
        } else {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setReorderingAllowed(true)
            fragmentTransaction.add(layout, newFragment, newFragment.javaClass.name)
            fragmentTransaction.addToBackStack(newFragment.javaClass.name)
            fragmentTransaction.addSharedElement(sharedView, sharedView.transitionName)
            fragmentTransaction.commitAllowingStateLoss()
//            newFragment.view?.rootView?.let { fragmentCircularReveal(sharedView, it) }
        }
    }

    /**
     * function to get the transition of the list item
     *
     * @param itemView view which show the transition
     *
     */
    fun getListFragmentExitTransition(itemView: View) {
        val epicCenterRect = Rect()
        //itemView is the full-width inbox item's view
        itemView.getGlobalVisibleRect(epicCenterRect)
        // Set Epic center to a imaginary horizontal full width line under the clicked item, so the explosion happens vertically away from it
        epicCenterRect.top = epicCenterRect.bottom
        val exitTransition = Explode()
        exitTransition.epicenterCallback = object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition): Rect {
                return epicCenterRect
            }
        }
    }

    /**
     * function to show the fragment with default transition
     *
     * @param newFragment next visible fragment
     *
     */

    fun showFragmentWithOutTransition(newFragment: Fragment, layout: Int) {
        val fragmentManager = supportFragmentManager
        // check if the fragment is in back stack
        val fragmentPopped = fragmentManager.popBackStackImmediate(newFragment.javaClass.name, 0)
        if (fragmentPopped) {
            // fragment is pop from backStack
        } else {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(layout, newFragment, newFragment.javaClass.name)
            fragmentTransaction.addToBackStack(newFragment.javaClass.name)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    /**
     * function to retrieve top fragment from the back stack entry
     *
     */

    fun getTopFragment(): Fragment? {
        if (supportFragmentManager.backStackEntryCount == 0) {
            return null
        }
        val fragmentTag =
            supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
        return supportFragmentManager.findFragmentByTag(fragmentTag)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
        binding.unbind()
    }

    fun setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT

            setFullScreen()
        }
    }

    fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = color
        }
    }
}