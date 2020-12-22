package ru.andvl.arapp.ui.camera

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.github.terrakok.cicerone.Router
import com.google.ar.sceneform.ux.ArFragment
import ru.andvl.arapp.R
import ru.andvl.arapp.databinding.FragmentCameraBinding
import javax.inject.Inject


private const val ARG_MODEL_LINK = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 * -- As a result, it is not needed here. But probably
 * I will try to connect it with summer practice
 * project, so I won't delete it now
 */
class CameraFragment : Fragment() {
    private var mPreviewView: PreviewView? = null
    private var mImageCapture: ImageCapture? = null
    private var mPreview: Preview? = null
    private var mCamera: Camera? = null
    private var mCurrentLens = CameraSelector.LENS_FACING_BACK

    private lateinit var mBinding: FragmentCameraBinding

    @Inject
    lateinit var mRouter: Router

    private var modelLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = FragmentCameraBinding.inflate(layoutInflater)
        arguments?.let {
            modelLink = it.getString(ARG_MODEL_LINK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_camera, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPreviewView = view.findViewById(R.id.camera_captureView)
        if (allPermissionsGranted()) {
            startCamera(mCurrentLens)
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permissions_error),
                    Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
//            else {
//                startCamera(mCurrentLens)
//            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera(lens: Int){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            mPreview = Preview.Builder().build()

            mImageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lens)
                .build()

            try {
                cameraProvider.unbindAll()
                mCamera =
                    cameraProvider.bindToLifecycle(this, cameraSelector, mPreview, mImageCapture)
                mPreview?.setSurfaceProvider(mPreviewView?.surfaceProvider)
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage!!)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val TAG = "CAMERA PREVIEW"
        private const val REQUEST_CODE_PERMISSION = 1111

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param modelLink Link to OBJ/GLTF/whatever 3D model file.
         * @return A new instance of fragment CameraFragment.
         */
        @JvmStatic
        fun newInstance(modelLink: String) =
            CameraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MODEL_LINK, modelLink)
                }
            }
    }
}