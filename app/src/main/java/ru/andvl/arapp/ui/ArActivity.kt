package ru.andvl.arapp.ui

import android.app.Activity
import android.app.ActivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import ru.andvl.arapp.R

class ArActivity : AppCompatActivity() {

    private lateinit var mArFragment: ArFragment
    private lateinit var mModelLink: String
    private var mModelRenderable: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }

        setContentView(R.layout.activity_ar)

        val intent = intent
        mModelLink = intent.getStringExtra(MODEL_LINK_EXTRA)!!

        Log.d(TAG, mModelLink)

        mArFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment)
                as ArFragment

        buildModel(mModelLink)

        mArFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            placeObject(hitResult, plane, motionEvent)
        }
    }

    private fun buildModel(modelLink: String) {
        Log.d(TAG, Uri.parse(modelLink).toString())
        ModelRenderable.builder()
            .setSource(this, RenderableSource.builder().setSource(
                this,
                Uri.parse(modelLink),
                RenderableSource.SourceType.GLTF2)
                .setScale(.1f)
                .build()
            ).build()
            .thenAccept { renderable -> mModelRenderable = renderable }
            .exceptionally {
                Toast.makeText(this, "Unable to load renderable $modelLink", Toast.LENGTH_SHORT).show()
                onBackPressed()
                null
            }
    }

    private fun placeObject(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(mArFragment.arSceneView.scene)

        val model = TransformableNode(mArFragment.transformationSystem)
        model.setParent(anchorNode)
        model.renderable = mModelRenderable
        model.select()
    }

    private fun addNodeToScene() {}


    companion object {
        private const val MODEL_LINK_EXTRA = "MODEL_LINK"
        private const val TAG = "AR ACTIVITY"
        private const val MIN_OPENGL_VERSION = 3.0

        fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
            val openGlVersionString = (activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
            if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
                Log.e(
                    TAG,
                    "Sceneform requires OpenGL ES 3.0 later"
                )
                Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show()
                activity.finish()
                return false
            }
            return true
        }
    }
}