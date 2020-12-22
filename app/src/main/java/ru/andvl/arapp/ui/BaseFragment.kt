package ru.andvl.arapp.ui

import android.content.Context
import androidx.fragment.app.Fragment
import moxy.MvpAppCompatFragment
import java.lang.ref.WeakReference

abstract class BaseFragment: MvpAppCompatFragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val activity = activity
        if (activity is ChainHolder) {
            (activity as ChainHolder).mChain.add(WeakReference<Fragment>(this))
        }
    }

    override fun onDetach() {
        val activity = activity
        if (activity is ChainHolder) {
            val chain = (activity as ChainHolder).mChain
            val it = chain.iterator()
            while (it.hasNext()) {
                val fragmentReference = it.next()
                val fragment = fragmentReference.get()
                if (fragment != null && fragment === this) {
                    it.remove()
                    break
                }
            }
        }
        super.onDetach()
    }
}