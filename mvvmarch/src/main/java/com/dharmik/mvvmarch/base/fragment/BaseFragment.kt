package com.dharmik.mvvmarch.base.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dharmik.mvvmarch.base.activity.BaseActivity
import com.dharmik.mvvmarch.base.activity.IBaseActivity
import com.dharmik.mvvmarch.base.viewmodel.BaseViewModel

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) : Fragment(),IBaseActivity {

    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding: DB? = null
        private set
    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null

    private fun getViewModel(viewmodel: Class<VM>): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(viewmodel)
    }

    val viewModel by lazy {
        getViewModel(mViewModelClass)
    }

    abstract fun initViewModel(viewModel: VM)

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity
            activity!!.onFragmentAttached()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel(viewModel)
        super.onCreate(savedInstanceState)
        retainInstance = true
        lifecycle.addObserver(viewModel)
        registerUIChangeLiveDataCallBack()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding?.root
        return mRootView
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.setVariable(bindingVariable, viewModel)
        binding?.executePendingBindings()

        initData()
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
        binding?.unbind()
    }

    private fun registerUIChangeLiveDataCallBack() {
        viewModel.let {
            it.getUC().getShowProgressLiveData().observe(this, Observer<Boolean> {

            })
            it.getUC().getHideProgressLiveData().observe(this, Observer<Boolean> {

            })
        }

    }

}