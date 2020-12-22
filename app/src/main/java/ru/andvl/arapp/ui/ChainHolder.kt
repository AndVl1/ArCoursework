package ru.andvl.arapp.ui

import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

interface ChainHolder {
    val mChain: MutableList<WeakReference<Fragment>>
}