package com.Axonix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.model.TrafficLog;
import com.Axonix.index.model.TrafficLogExample;
import com.Axonix.index.model.TrafficLogUpdate;
import com.Axonix.index.session.UserSessionManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Route(path = "/feature/traffic")
public class TrafficRecognitionFragment extends Fragment {

    private AMapLocationClient locationClient;
    private PreviewView previewView;
    private ExecutorService  cameraExecutor;
    private String TRAFFIC_URL;
    private TextToSpeech textToSpeech;
    private ImageView imageView;
    private Date startTime ;
    private double log=0,lat=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traffic, container, false);
        startTime = new Date();
//        TRAFFIC_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/traffic-logs/aitraffic";
        TRAFFIC_URL = requireContext().getResources().getString(com.Axonix.index.R.string.python_base_url_traffic);
        previewView = view.findViewById(R.id.previewView);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        imageView = view.findViewById(R.id.resultImageView);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        cameraExecutor = Executors.newSingleThreadExecutor();

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 10);
        } else {
            startCamera();
        }
        showPrivacyDialog();
        initLocation(requireContext());
        return view;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setTargetResolution(new Size(1080, 720)) // 可调整大小
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {
                    private long lastAnalyzedTime = 0; // 上次分析的时间戳

                    @Override
                    public void analyze(@NonNull ImageProxy image) {
                        long currentTime = System.currentTimeMillis();

                        if (currentTime - lastAnalyzedTime >= 300) { // 每500ms执行一次
                            lastAnalyzedTime = currentTime;

                            @SuppressLint("UnsafeOptInUsageError")
                            Image mediaImage = image.getImage();
                            if (mediaImage != null) {
                                Bitmap bitmap = toBitmap(mediaImage);
                                image.close();

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                                byte[] imageBytes = baos.toByteArray();
                                String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                                uploadImage(base64Image);
                            } else {
                                image.close();
                            }
                        } else {
                            image.close();
                        }
                    }
                });

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void uploadImage(String base64Image) {
        OkHttpClient client = NetworkClient.INSTANCE.getClient();
        String json = "{\"image_base64\":\"" + base64Image + "\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(TRAFFIC_URL)
                .post(body)
                .build();
        Log.d("base64Img",base64Image);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("AI", "上传失败", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();

                try {
                    JSONObject jsonResponse = new JSONObject(responseData);

                    String status = jsonResponse.getString("status");
                    String ttsText = jsonResponse.getString("tts_text").trim();
                    boolean vibrate = jsonResponse.getBoolean("vibrate");
                    String base64Img = jsonResponse.getString("image_annotated");
                    if (!isAdded()) {
                        return;
                    }
                    requireActivity().runOnUiThread(() -> {
                        previewView.setVisibility(View.GONE);
                        if (!ttsText.isEmpty()) {
                            textToSpeech = new TextToSpeech(getContext(), status1 -> {
                                if (status1 == TextToSpeech.SUCCESS) {
                                    textToSpeech.setSpeechRate(2.5f);
                                    textToSpeech.speak(ttsText, TextToSpeech.QUEUE_FLUSH, null, null);
                                }
                            });
                        }

                        if (vibrate) {
                            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibrator.vibrate(200);
                            }
                        }

                        byte[] decodedString = Base64.decode(base64Img, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        Matrix matrix = new Matrix();
                        matrix.postRotate(+90);

                        Bitmap rotatedBitmap = Bitmap.createBitmap(decodedByte, 0, 0,
                                decodedByte.getWidth(), decodedByte.getHeight(), matrix, true);

                        imageView.setImageBitmap(rotatedBitmap);
                        imageView.setVisibility(View.VISIBLE);

                    });

                } catch (JSONException  e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];

        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 100, out);
        byte[] jpegBytes = out.toByteArray();

        return BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.length);
    }
    private void showPrivacyDialog() {
        AMapLocationClient.updatePrivacyShow(getContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getContext(), true);
    }

    private void initLocation(Context context) {
        try {
            locationClient = new AMapLocationClient(context);
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setOnceLocation(true);
            locationClient.setLocationOption(option);
            locationClient.setLocationListener(this::onLocationReceived);
            locationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLocationReceived(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
             lat = aMapLocation.getLatitude();
             log = aMapLocation.getLongitude();

        } else {
            Toast.makeText(getContext(), "定位失败！请检查 GPS 设置", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
        String traffic_log_url = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/traffic-logs/update/by-example/selective";

        TrafficLog trafficLog = new TrafficLog();
        trafficLog.setUserId(UserSessionManager.getInstance(requireContext()).getUser().getId());
        trafficLog.setStartTime(startTime);
        trafficLog.setEndTime(new Date());
        trafficLog.setLastLng(BigDecimal.valueOf(log));
        trafficLog.setLastLat(BigDecimal.valueOf(lat));

        TrafficLogExample trafficLogExample = new TrafficLogExample();
        TrafficLogExample.Criteria criteria = trafficLogExample.createCriteria();
        criteria.andUserIdEqualTo(UserSessionManager.getInstance(requireContext()).getUser().getId());

        TrafficLogUpdate trafficLogUpdate = new TrafficLogUpdate(trafficLog, trafficLogExample);

        Gson gson = NetworkTimeClient.getGson();
        String json = gson.toJson(trafficLogUpdate);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(traffic_log_url)
                .put(body)
                .build();

        OkHttpClient httpClient = NetworkClient.INSTANCE.getClient();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("更新路况日志", "失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("更新路况日志", "成功");
                } else
                    Log.d("更新路况日志", "失败");
            }
        });
    }
}
