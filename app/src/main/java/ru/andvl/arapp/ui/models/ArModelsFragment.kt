package ru.andvl.arapp.ui.models

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.terrakok.cicerone.Router
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.andvl.arapp.App
import ru.andvl.arapp.R
import ru.andvl.arapp.databinding.FragmentModelsBinding
import ru.andvl.arapp.databinding.FragmentModelsListBinding
import ru.andvl.arapp.models.ModelsListContent
import ru.andvl.arapp.mvp.main.ArModelsPresenter
import ru.andvl.arapp.mvp.main.ArModelsView
import ru.andvl.arapp.ui.BaseFragment
import javax.inject.Inject

/**
 * A fragment representing a list of Items.
 */
class ArModelsFragment : BaseFragment(), ArModelsView {

    private lateinit var mBinding: FragmentModelsListBinding

    @Inject
    lateinit var mRouter: Router

    @InjectPresenter
    lateinit var mPresenter : ArModelsPresenter

    @ProvidePresenter
    fun createPresenter() = ArModelsPresenter(mRouter, 0)

    private var mColumnCount = 1
    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.INSTANCE.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        mBinding = FragmentModelsListBinding.inflate(layoutInflater)

        arguments?.let {
            mColumnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_models_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            mPresenter.getModels()
            mRecyclerView = view
        }
        return view
    }

    override fun setupList(list: MutableList<ModelsListContent.ListItem>) {
        if (mRecyclerView != null) {
            with(mRecyclerView!!){
                layoutManager = when{
                    mColumnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, mColumnCount)
                }
                adapter = ArModelsRecyclerViewAdapter(list, mRouter)
            }
        }
    }


    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        const val ARG_NUMBER_FRAGMENT = "number_fragment"
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ArModelsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}