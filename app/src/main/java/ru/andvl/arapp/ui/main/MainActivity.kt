package ru.andvl.arapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import moxy.MvpAppCompatActivity
import ru.andvl.arapp.App
import ru.andvl.arapp.R
import ru.andvl.arapp.Screens.main
import ru.andvl.arapp.databinding.ActivityMainBinding
import ru.andvl.arapp.ui.ChainHolder
import java.lang.ref.WeakReference
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), ChainHolder {
	private lateinit var mMainBinding: ActivityMainBinding

	private val mNavigator = object: AppNavigator(this, R.id.container) {
		override fun applyCommand(command: Command) {
			super.applyCommand(command)
			supportFragmentManager.executePendingTransactions()
		}
	}

	override val mChain = ArrayList<WeakReference<Fragment>>()

	@Inject
	lateinit var mNavigatorHolder: NavigatorHolder

	override fun onCreate(savedInstanceState: Bundle?) {
		App.INSTANCE.appComponent.inject(this)
		super.onCreate(savedInstanceState)
		mMainBinding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(mMainBinding.root)

		mNavigator.applyCommands(arrayOf<Command>(Replace(main())))
	}

	override fun onResumeFragments() {
		super.onResumeFragments()
		mNavigatorHolder.setNavigator(mNavigator)
	}

	override fun onPause() {
		mNavigatorHolder.removeNavigator()
		super.onPause()
	}
}