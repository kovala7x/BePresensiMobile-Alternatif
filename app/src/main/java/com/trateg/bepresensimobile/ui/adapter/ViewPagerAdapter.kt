package com.trateg.bepresensimobile.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trateg.bepresensimobile.ui.surat_diajukan.SuratDiajukanFragment
import com.trateg.bepresensimobile.ui.surat_diproses.SuratDiprosesFragment
import com.trateg.bepresensimobile.util.SessionManager


class ViewPagerAdapter(fragment: Fragment?) : FragmentStateAdapter(fragment!!) {
    private val mFragments: Array<Fragment> = arrayOf(
        SuratDiajukanFragment(),
        SuratDiprosesFragment()
    )
    val session = SessionManager(context = fragment?.requireContext()!!)
    val mFragmentNames = if(session.getRole().toUpperCase().equals("DOSEN")){
        arrayOf("Belum Diproses", "Sudah Diproses")
    }else{
        arrayOf("Diajukan", "Diproses")
    }

    override fun getItemCount(): Int {
        return mFragments.size //Number of fragments displayed
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return SuratDiajukanFragment()
            1 -> return SuratDiprosesFragment()
        }
        return SuratDiajukanFragment()
    }
}