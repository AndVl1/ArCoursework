package ru.andvl.arapp.ui.ar

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import ru.andvl.arapp.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ArActivity : AppCompatActivity() {

    private lateinit var mArFragment: ArFragment
    private lateinit var mModelLink: String
    private var mModelRenderable: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }

        setContentView(R.layout.activity_ar)

        val intent = intent
        mModelLink = intent.getStringExtra(MODEL_LINK_EXTRA)!!

        Log.d(TAG, mModelLink)

        mArFragment = supportFragmentManager
            .findFragmentById(R.id.sceneform_fragment)
                as WritingArFragment

        buildModel(mModelLink)

//        val takeImageFab: FloatingActionButton = findViewById(R.id.take_image)
//        takeImageFab.setOnClickListener {
//            takePhoto()
//        }

        mArFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            placeRotatableObject(hitResult, plane, motionEvent)
//            placeObject(hitResult, plane, motionEvent)
        }
    }

    private fun buildModel(modelLink: String) {
        Log.d(TAG, Uri.parse(modelLink).toString())
        ModelRenderable.builder()
            .setSource(
                this, RenderableSource.builder().setSource(
                    this,
                    Uri.parse(modelLink),
                    RenderableSource.SourceType.GLTF2
                ).setScale(.25f)
                    .build()
            ).build()
            .thenAccept { renderable -> mModelRenderable = renderable }
            .exceptionally {
                Toast.makeText(
                    this,
                    "Unable to load renderable $modelLink",
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
                null
            }
    }

    private fun placeObject(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(mArFragment.arSceneView.scene)

        val transformableNode = TransformableNode(mArFragment.transformationSystem)
        transformableNode.setParent(anchorNode)
        transformableNode.renderable = mModelRenderable
        transformableNode.select()
    }

    private fun placeRotatableObject(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
//        anchorNode.setParent(mArFragment.arSceneView.scene)
        val rotatingNode = RotatingNode()

        val transformableNode = TransformableNode(mArFragment.transformationSystem)

        rotatingNode.renderable = mModelRenderable
        rotatingNode.addChild(transformableNode)
        rotatingNode.setParent(anchorNode)
        mArFragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }



    companion object {
        private const val MODEL_LINK_EXTRA = "MODEL_LINK"
        private const val TAG = "AR ACTIVITY"
        private const val MIN_OPENGL_VERSION = 3.0

        fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
            val openGlVersionString = (activity.getSystemService(ACTIVITY_SERVICE)
                    as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
            if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
                Log.e(
                    TAG,
                    "Sceneform requires OpenGL ES 3.0 later"
                )
                Toast.makeText(
                    activity,
                    "Sceneform requires OpenGL ES 3.0 or later",
                    Toast.LENGTH_LONG
                ).show()
                activity.finish()
                return false
            }
            return true
        }
    }
}