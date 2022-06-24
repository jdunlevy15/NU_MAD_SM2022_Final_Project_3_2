package com.example.nu_mad_sm2022_final_project_3_2;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.net.URI;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCameraController#newInstance} factory method to
 * create an instance of this fragment.
 */
@Keep
public class FragmentCameraController extends Fragment {
    // Dog profile to be updated
    private String dogId;

    // view items
    Button captureButton, galleryButton;
    private PreviewView viewFinder;

    // camera
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraSelector cameraSelector;
    private Preview preview;
    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider = null;

    // listener
    private ICameraControllerListener cListener;

    private static final String ARG_DOG_ID = "param_dog_id";

    public FragmentCameraController() {
        // Required empty public constructor
    }

    public static FragmentCameraController newInstance(String dogId) {
        FragmentCameraController fragment = new FragmentCameraController();
        Bundle args = new Bundle();
        args.putString(ARG_DOG_ID, dogId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_DOG_ID)) {
                this.dogId = args.getString(ARG_DOG_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_camera_controller, container, false);

        // view items
        captureButton = view.findViewById(R.id.registerImageCaptureButton);
        viewFinder = view.findViewById(R.id.viewFinder);
        galleryButton = view.findViewById(R.id.galleryButton);

        // take pic on button press
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("photo", "capture pressed");
                takePhoto();
            }
        });

        // open gallery
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.onGalleryPress();
            }
        });

        // start the camera
        startCamera();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentCameraController.ICameraControllerListener){
            cListener = (FragmentCameraController.ICameraControllerListener) context;
        }else{
            throw new RuntimeException(context+" must implement FragmentCameraController");
        }
    }

    // camera functions
    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(()-> {
            preview = new Preview.Builder().build();
            preview.setSurfaceProvider(viewFinder.getSurfaceProvider());
            imageCapture = new ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build();
            try {
                cameraProvider = cameraProviderFuture.get();
                cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private void takePhoto() {
        long timestamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/CameraX-Image");
        }

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                getContext().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        ).build();

        imageCapture.takePicture(outputFileOptions,
                ContextCompat.getMainExecutor(getContext()),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.d("photo", "image saved");
                        cListener.onPhotoTaken(outputFileResults.getSavedUri(), dogId);
                        Toast.makeText(getContext(), "Picture Captured!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.d("photo", "error");
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public interface ICameraControllerListener {
        void onPhotoTaken(Uri imageURI, String dogID);
        void onGalleryPress();
    }
}