package com.example.sampleardemo;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SkeletonNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    //Cloud anchor
//    private CloudAnchorFragment cloudAnchorFragment;
//
//    private enum AppAnchorState {
//        NONE,
//        HOSTING,
//        HOSTED
//    }
//
//    private Anchor anchor;
//    private AppAnchorState anchorState = AppAnchorState.NONE;
//    private boolean isPlaced = false;
//    private SharedPreferences preferences;
//    private SharedPreferences.Editor editor;

    //Static fragment
    private ArFragment arFragment;

    private ModelRenderable andyRenderable;
    private ModelRenderable catRenderable;
    private ModelRenderable dogRenderable;

    private ModelRenderable andyAnimatedRenderable;
    private ModelAnimator andyAnimator;
    private int i = 0;

    private boolean isTapped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cloud anchor
//        preferences = getSharedPreferences("AnchorId", MODE_PRIVATE);
//        editor = preferences.edit();
//
//        cloudAnchorFragment = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.cloudAnchorFragment);
//        cloudAnchorFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
//            if(!isPlaced){
//                anchor = arFragment.getArSceneView().getSession().hostCloudAnchor(hitResult.createAnchor());
//                anchorState = AppAnchorState.HOSTING;
//
//                showToast("Hosting...");
//
//                createModel(anchor);
//                isPlaced = true;
//            }
//        }));
//
//        cloudAnchorFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
//            if(anchorState == AppAnchorState.HOSTING)
//                return;
//
//            Anchor.CloudAnchorState cloudAnchorState = anchor.getCloudAnchorState();
//
//            if(cloudAnchorState.isError()){
//                showToast(cloudAnchorState.toString());
//            }
//            else if(cloudAnchorState == Anchor.CloudAnchorState.SUCCESS){
//                anchorState = AppAnchorState.HOSTED;
//
//                String anchorId = anchor.getCloudAnchorId();
//                editor.putString("anchorId", anchorId);
//                editor.apply();
//
//                showToast("Anchor hosted successfully. Anchor Id: " + anchorId);
//            }
//        });
//
//        Button resolve = findViewById(R.id.resolve);
//        resolve.setOnClickListener(view -> {
//            String anchorId = preferences.getString("anchorId", "null");
//
//            if(anchorId.equals("null")){
//                showToast("No anchorId found");
//                return;
//            }
//
//            Anchor resolvedAnchor = arFragment.getArSceneView().getSession().resolveCloudAnchor(anchorId);
//            createModel(resolvedAnchor);
//        });

        //Static fragment
        arFragment = (ArFragment) getSupportFragmentManager()
                .findFragmentById(R.id.arFragment);

//        ArSceneView arSceneView = arFragment.getArSceneView();
//        Scene scene = arSceneView.getScene();
//
//        ModelRenderable.builder()
//                .setSource(this, Uri.parse("Andy.sfb"))
//                .build()
//                .thenAccept(modelRenderable -> { andyRenderable = modelRenderable; });
//
//        ModelRenderable.builder()
//                .setSource(this, Uri.parse("Mesh_Cat.sfb"))
//                .build()
//                .thenAccept(modelRenderable -> { catRenderable = modelRenderable; });
//
//        ModelRenderable.builder()
//                .setSource(this, Uri.parse("Mesh_Beagle.sfb"))
//                .build()
//                .thenAccept(modelRenderable -> { dogRenderable = modelRenderable; });

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            if (isTapped)
                return;

            initializeModel(hitResult.createAnchor(), arFragment);

//            Anchor anchor = hitResult.createAnchor();
//            AnchorNode anchorNode = new AnchorNode(anchor);
//            anchorNode.setParent(scene);
//
//            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
//            andy.setParent(anchorNode);
//            Node andyNode = createSceneGraph();
//            andyNode.setParent(andy);
//            andy.select();

            isTapped = true;
        });
    }

    private void initializeModel(Anchor anchor, ArFragment arFragment) {

        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("andy_dance.sfb"))
                .build()
                .thenAccept(modelRenderable -> {

                    SkeletonNode skeletonNode = new SkeletonNode();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    skeletonNode.setParent(anchorNode);
                    skeletonNode.setRenderable(modelRenderable);

                    arFragment.getArSceneView().getScene().addChild(anchorNode);

                    Button button = findViewById(R.id.btn_animate);
                    button.setOnClickListener(view -> {
                        animateModel(modelRenderable);
                    });
                });
    }

    private void animateModel(ModelRenderable modelRenderable) {
        if(andyAnimator != null && andyAnimator.isRunning()){
            andyAnimator.end();
        }

        int animationCount = modelRenderable.getAnimationDataCount();

        if(i == animationCount){
            i=0;
        }

        AnimationData animationData = modelRenderable.getAnimationData(i);
        andyAnimator = new ModelAnimator(animationData, modelRenderable);
        andyAnimator.start();
        i++;
    }

//    private void showToast(String toString) {
//        Toast.makeText(this, toString, Toast.LENGTH_LONG).show();
//    }
//
//    private void createModel(Anchor anchor) {
//        ModelRenderable
//                .builder()
//                .setSource(this, Uri.parse("Andy.sfb"))
//                .build()
//                .thenAccept(modelRenderable -> placeModel(anchor, modelRenderable));
//    }
//
//    private void placeModel(Anchor anchor, ModelRenderable modelRenderable) {
//        AnchorNode anchorNode = new AnchorNode(anchor);
//        anchorNode.setRenderable(modelRenderable);
//        cloudAnchorFragment.getArSceneView().getScene().addChild(anchorNode);
//    }

    private Node createSceneGraph() {

        Node andyNode = new Node();
        andyNode.setRenderable(andyRenderable);

        Node catNode = new Node();
        catNode.setParent(andyNode);
        catNode.setLocalPosition(new Vector3(0.2f, -0.2f, 0.0f));
        catNode.setRenderable(catRenderable);

        Node dogNode = new Node();
        dogNode.setParent(andyNode);
        dogNode.setLocalPosition(new Vector3(-0.2f, 0.2f, 0.0f));
        dogNode.setRenderable(dogRenderable);

        return andyNode;
    }
}
