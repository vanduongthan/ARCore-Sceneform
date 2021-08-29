package com.duongtv.firstarwithsceneform

import androidx.annotation.Nullable
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.BaseTransformableNode
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer
import com.google.ar.sceneform.ux.SelectionVisualizer

class CircleArrowSelectionVisualizer : SelectionVisualizer {

    var footprintNode: Node? = null

    init {
        footprintNode = Node()
    }

    override fun applySelectionVisual(node: BaseTransformableNode?) {
        footprintNode!!.setParent(node)
    }

    override fun removeSelectionVisual(node: BaseTransformableNode?) {
        footprintNode!!.setParent(null)
    }
}