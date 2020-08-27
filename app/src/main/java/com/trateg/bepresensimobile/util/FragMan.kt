package com.trateg.bepresensimobile.util

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.home.HomePresenter
import com.trateg.bepresensimobile.profil.ProfilPresenter
import com.trateg.bepresensimobile.riwayat.RiwayatPresenter
import com.trateg.bepresensimobile.surat.SuratPresenter
import java.io.Serializable

object FragMan : FragManContract() {

    const val HISTORY_KEY = "HISTORY_KEY"
    const val CURRENT_TAB_KEY = "CURRENT_TAB_KEY"

    private var mBottomNavigationView: BottomNavigationView? = null
    private var mFragmentManager: FragmentManager? = null
    private var mContainerId: Int = 0
    private var mCurrentTab = 0
    private lateinit var mHistory: Array<ArrayList<String>>

    override fun getHistory(): Array<ArrayList<String>> = mHistory

    override fun setHistory(history: Serializable)  {
        mHistory = history as Array<ArrayList<String>>
    }

    override fun setCurrentTab(index: Int) {
        mCurrentTab = index
    }

    override fun getCurrentTab(): Int = mCurrentTab

    override fun init(fragmentManager: FragmentManager, bottomNavigationView: BottomNavigationView, containerId: Int, isRestored: Boolean) {
        mBottomNavigationView = bottomNavigationView
        mFragmentManager = fragmentManager
        mHistory = Array(bottomNavigationView.menu.size()) { arrayListOf<String>() }
        mContainerId = containerId
        if(!isRestored)
            initRootFragments()
        setBottomNavigationListener()
    }

    override fun addFragment(fragment: BaseFragment, tag: String?, inStack: Boolean?, addAndHide: Boolean?) {
        if(mFragmentManager?.beginTransaction() == null) return
        var ft = mFragmentManager!!.beginTransaction()
        var formattedTag = tag
        if (tag?.contains("_") == true) { // root fragments
            val tabIndex = tag.split("_")[0].toInt()
            mHistory[tabIndex].add(tag)
        } else {
            getActiveFragment(mCurrentTab)
                ?.let { ft= ft.hide(it) }
            val indexInTab = mHistory[mCurrentTab].last().split("_")[1].toInt()
            formattedTag = "${mCurrentTab}_${indexInTab + 1}"
            mHistory[mCurrentTab].add(formattedTag)
        }
        ft = ft.add(mContainerId, fragment, formattedTag)

        if (addAndHide == true)
            ft = ft.hide(fragment)
        if (inStack == true)
            //ft = ft.addToBackStack(tag)
        ft.commitAllowingStateLoss()
    }

    override fun destroy() {
        mBottomNavigationView = null
        mFragmentManager = null
        mCurrentTab = 0
        mHistory.forEach { it.clear() }
    }

    override fun initRootFragments() {
        val homeFragment = HomePresenter.newInstance(Bundle())
        val profilFragment = ProfilPresenter.newInstance(Bundle())
        val riwayatFragment = RiwayatPresenter.newInstance(Bundle())
        val suratFragment = SuratPresenter.newInstance(Bundle())
        var tabRootFragments = ArrayList<BaseFragment>()
        tabRootFragments.add(profilFragment)
        tabRootFragments.add(riwayatFragment)
        tabRootFragments.add(suratFragment)

        //TODO based on tab count , you can add more roots
        tabRootFragments.forEachIndexed { index, fragment ->
            val i = index + 1
            addFragment(fragment, "${i}_0", addAndHide = true)
        }
        addFragment(homeFragment, "0_0")
    }

    override fun changeTab(index: Int, isBackPressChange: Boolean) {
        if(!isBackPressChange) {
            val activeFragment =
                getActiveFragment(mCurrentTab)
            val requrestedTabFragment = mFragmentManager?.findFragmentByTag(
                mHistory[index].last())
            if (activeFragment == null || requrestedTabFragment == null) return
            if(activeFragment == requrestedTabFragment) return
            mFragmentManager?.beginTransaction()?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)?.hide(activeFragment)?.show(requrestedTabFragment)?.commitAllowingStateLoss()
        }else {
            mBottomNavigationView?.selectedItemId =
                getMenuItemId(index)
        }
        mCurrentTab = index
    }

    override fun getActiveFragment(tabIndex: Int): Fragment? {
        return mFragmentManager?.findFragmentByTag(mHistory[tabIndex].last())
    }

    override fun onBackPressed(): Boolean {
        val currentTabHistory = mHistory[mCurrentTab]
        if(currentTabHistory.size > 1) { //
            val activeFragment =
                getActiveFragment(mCurrentTab)
            val previousFragment = mFragmentManager?.findFragmentByTag(currentTabHistory[currentTabHistory.size - 2])
            if(activeFragment == null || previousFragment == null) return false
            if(activeFragment == previousFragment) return false
            mFragmentManager?.beginTransaction()?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)?.remove(activeFragment)?.show(previousFragment)?.commitAllowingStateLoss()
            currentTabHistory.removeAt(currentTabHistory.size - 1)
            return false
        }else if(mCurrentTab != 0){
            changeTab(0, true)
            return false
        }
        return true
    }

    override fun setBottomNavigationListener() {
        mBottomNavigationView?.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        changeTab(0, false)
                        return true
                    }
                    R.id.navigation_profil -> {
                        changeTab(1, false)
                        return true
                    }
                    R.id.navigation_riwayat -> {
                        changeTab(2, false)
                        return true
                    }
                    R.id.navigation_surat -> {
                        changeTab(3, false)
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun getMenuItemId(tabIndex: Int): Int {
        when(tabIndex){
            0-> { return R.id.navigation_home}
            1-> { return R.id.navigation_profil}
            2 -> {return R.id.navigation_riwayat}
            else -> {return R.id.navigation_surat}
        }
    }
}