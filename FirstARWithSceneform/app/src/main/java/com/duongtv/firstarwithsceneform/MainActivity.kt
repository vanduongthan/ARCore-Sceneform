package com.duongtv.firstarwithsceneform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

import com.google.ar.sceneform.ux.ArFragment

import java.util.function.Consumer
import java.util.function.Function
import android.widget.Toast

import android.app.ActivityManager

import android.os.Build.VERSION_CODES

import android.os.Build

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.MotionEvent
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.collision.Plane
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import android.view.Gravity
import com.google.ar.sceneform.assets.RenderableSource

import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode


class MainActivity : AppCompatActivity() {

    private val MIN_OPENGL_VERSION = 3.0
    private val TAG = "MainActivity"
    private var testViewRenderable: ViewRenderable? = null
    private lateinit var button: Button
    private var arFragment: ArFragment? = null

    private val GLTF_ASSET =
        "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF/Duck.gltf"
    private lateinit var renderableModel: ModelRenderable
    private lateinit var shapeModel: ModelRenderable
    private lateinit var androidModel: ModelRenderable
    private lateinit var gamingChairFromRawFolder: ModelRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?

        //buildRenderableView()
        //creteShapeModel()
        buildRenderableModelFromIternet()
        buildModelFromAssert()
        //buildModelFromGLBFile()
        setupARFragment()
    }

    private fun buildRenderableView(){
        ViewRenderable.builder()
            .setView(this, R.layout.renderable_view)
            .build()
            .thenAccept {
                Log.d(TAG, "onCreate: renderable accepted")
                testViewRenderable = it
                button = testViewRenderable?.getView()?.findViewById(R.id.btnChangeColor) as Button
                button.setOnClickListener {
                    it.setBackgroundColor(Utils.randomColor())
                    Log.d(TAG, "onCreate: button clicked")
                }
            }
    }

    private fun buildModelFromGLBFile() {
        /*ModelRenderable.builder()
            .setSource(
                this,
                Uri.parse(
                    "https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb"
                )
            )
            .build()
            .thenAccept(Consumer { renderable: ModelRenderable ->
                renderableModel = renderable
                Toast.makeText(this, "3D model is available", Toast.LENGTH_SHORT).show()
            })
            .exceptionally(
                Function<Throwable, Void?> { throwable: Throwable? ->

                    runOnUiThread{
                        val toast = Toast.makeText(
                            this, "Unable to load renderable " +
                                    GLTF_ASSET, Toast.LENGTH_LONG
                        )
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    }
                    null
                })*/
    }

    private fun buildModelFromAssert() {
        ModelRenderable.builder()
            .setSource(this, R.raw.andy)
            .build()
            .thenAccept {
                Toast.makeText(this, "assert is ready", Toast.LENGTH_SHORT).show()
                androidModel = it
            }
    }


    private fun creteShapeModel() {
        MaterialFactory.makeOpaqueWithColor(this, Color(android.graphics.Color.RED))
            .thenAccept { material: Material? ->
               shapeModel  =
                    ShapeFactory.makeSphere(0.1f, Vector3(0.0f, 0.15f, 0.0f), material)
            }
    }
    private var parentAnchorNode: AnchorNode ?= null
    var transformableNodeParent: TransformableNode? = null
    private fun setupARFragment() {
        arFragment?.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            //Create the anchor

            //anchorNode.renderable = testViewRenderable
            //anchorNode.renderable = shapeModel
            //anchorNode.renderable = renderableModel
            //anchorNode.renderable = androidModel
            //create the transformable object and add it to the anchor
            if (transformableNodeParent == null) {
                val anchor = hitResult.createAnchor()
                parentAnchorNode = AnchorNode(anchor)
                parentAnchorNode!!.setParent(arFragment!!.arSceneView.scene)
                transformableNodeParent = TransformableNode(arFragment!!.transformationSystem)
                transformableNodeParent!!.setParent(parentAnchorNode)
                transformableNodeParent!!.renderable = androidModel
                transformableNodeParent!!.select()
            } else {
                //create children node grouped with parent node
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode!!.setParent(transformableNodeParent)
                anchorNode.renderable = androidModel
            }
        }
    }

    private fun buildRenderableModelFromIternet() {
        ModelRenderable.builder()
            .setSource(
                this, RenderableSource.builder().setSource(
                    this,
                    Uri.parse(GLTF_ASSET),
                    RenderableSource.SourceType.GLTF2
                )
                    .setScale(0.5f) // Scale the original model to 50%.
                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                    .build()
            )
            .setRegistryId(GLTF_ASSET)
            .build()
            .thenAccept(Consumer { renderable: ModelRenderable ->
                renderableModel = renderable
                Toast.makeText(this, "3D model is available", Toast.LENGTH_SHORT).show()
            })
            .exceptionally(
                Function<Throwable, Void?> { throwable: Throwable? ->
                    val toast = Toast.makeText(
                        this, "Unable to load renderable " +
                                GLTF_ASSET, Toast.LENGTH_LONG
                    )
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                })
    }

    fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later")
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        val openGlVersionString =
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later")
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        return true
    }
}