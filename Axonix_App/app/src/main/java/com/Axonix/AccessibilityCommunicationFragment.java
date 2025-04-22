package com.Axonix;

import android.content.Intent;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Axonix.index.config.NetworkClient;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccessibilityCommunicationFragment extends Fragment {

    private EditText etInput;
    private TextView tvOutput;
    private Button btnVoiceToText;
    private Button btnTextToVoice;
    private Button btnTranslate;
    private TextToSpeech textToSpeech;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private static final int SAMPLE_RATE = 16000;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord audioRecord;
    private boolean isRecording = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communicate, container, false);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        // 加载 fragment 布局
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                tvOutput.setText("识别结果：" + result.get(0));
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化控件
        etInput = view.findViewById(R.id.et_input);
        tvOutput = view.findViewById(R.id.tv_output);
        btnVoiceToText = view.findViewById(R.id.btn_voice_to_text);
        btnTextToVoice = view.findViewById(R.id.btn_text_to_voice);
        btnTranslate = view.findViewById(R.id.btn_translate);

        // 模拟语音转文字
        btnVoiceToText.setOnTouchListener((v, event) -> {
            etInput.setEnabled(false);
            tvOutput.setText("请按住[语音转文字]按钮说话，松开后将进行转换");

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startRecording();
                    return false;
                case MotionEvent.ACTION_UP:
                    stopRecording();

                    return false;
            }
            return false;
        });

        btnTextToVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etInput.setEnabled(true);

                tvOutput.setText("【文字转语音】结果：朗读中……");
                textToSpeech = new TextToSpeech(getContext(), status1 -> {
                    if (status1 == TextToSpeech.SUCCESS) {
                        textToSpeech.speak(etInput.getText().toString().trim(), TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                });
            }
        });

        // 模拟翻译功能
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etInput.setEnabled(true);
                String translate_url = requireContext().getResources().getString(com.Axonix.index.R.string.python_base_url) + "translate";
                String text = etInput.getText().toString().trim();
                if (text.isEmpty()){
                    tvOutput.setText("请输入文字后再点击按钮");
                    return;
                }
                tvOutput.setText("正在翻译中请稍后。");

                OkHttpClient httpClient = NetworkClient.INSTANCE.getClient();
                RequestBody body = RequestBody.create(text, MediaType.get("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url(translate_url)
                        .post(body)
                        .build();

                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("translate", "获取失败", e);

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            Log.e("translate", "translate失败，状态码：" + response.code());
                            return;
                        }
                        String res = ""+response.body().string();

                        requireActivity().runOnUiThread(() -> {
                            tvOutput.setText(res);
                        });
                    }
                });
            }
        });
    }

    private void startRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);

        File outputFile = new File(getContext().getExternalFilesDir(null), "audio.wav");
        audioFilePath = outputFile.getAbsolutePath();

        isRecording = true;

        new Thread(() -> {
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                writeWavHeader(out, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
                byte[] buffer = new byte[bufferSize];
                audioRecord.startRecording();

                while (isRecording) {
                    int read = audioRecord.read(buffer, 0, buffer.length);
                    if (read > 0) {
                        out.write(buffer, 0, read);
                    }
                }

                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                if (audioFilePath != null && new File(audioFilePath).exists()) {
                    sendAudioToServer(audioFilePath);
                } else {
                    Toast.makeText(getContext(), "录音文件无效", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                updateWavHeader(outputFile);
                sendAudioToServer(outputFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void stopRecording() {
        isRecording = false;
    }

    private void writeWavHeader(FileOutputStream out, int sampleRate, int channels, int encoding) throws IOException {
        short bitsPerSample = (encoding == AudioFormat.ENCODING_PCM_16BIT) ? (short) 16 : (short) 8;
        short numChannels = (channels == AudioFormat.CHANNEL_IN_MONO) ? (short) 1 : (short) 2;

        byte[] header = new byte[44];
        long totalDataLen = 0;
        long byteRate = sampleRate * numChannels * bitsPerSample / 8;

        // RIFF header
        header[0] = 'R'; header[1] = 'I'; header[2] = 'F'; header[3] = 'F';
        header[4] = 0; header[5] = 0; header[6] = 0; header[7] = 0; // will update later
        header[8] = 'W'; header[9] = 'A'; header[10] = 'V'; header[11] = 'E';

        // fmt subchunk
        header[12] = 'f'; header[13] = 'm'; header[14] = 't'; header[15] = ' ';
        header[16] = 16; header[17] = 0; header[18] = 0; header[19] = 0;
        header[20] = 1; header[21] = 0; // PCM
        header[22] = (byte) numChannels;
        header[23] = 0;
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (numChannels * bitsPerSample / 8);
        header[33] = 0;
        header[34] = (byte) (bitsPerSample & 0xff);         // 低位字节
        header[35] = (byte) ((bitsPerSample >> 8) & 0xff);  // 高位字节


        // data subchunk
        header[36] = 'd'; header[37] = 'a'; header[38] = 't'; header[39] = 'a';
        header[40] = 0; header[41] = 0; header[42] = 0; header[43] = 0; // will update later

        out.write(header, 0, 44);
    }
    private void updateWavHeader(File wavFile) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(wavFile, "rw")) {
            long totalAudioLen = raf.length() - 44;
            long totalDataLen = totalAudioLen + 36;

            short numChannels = (CHANNEL_CONFIG == AudioFormat.CHANNEL_IN_MONO) ? (short) 1 : (short) 2;
            short bitsPerSample = (AUDIO_FORMAT == AudioFormat.ENCODING_PCM_16BIT) ? (short) 16 : (short) 8;
            int byteRate = SAMPLE_RATE * numChannels * bitsPerSample / 8;

            raf.seek(4);
            raf.write((byte) (totalDataLen & 0xff));
            raf.write((byte) ((totalDataLen >> 8) & 0xff));
            raf.write((byte) ((totalDataLen >> 16) & 0xff));
            raf.write((byte) ((totalDataLen >> 24) & 0xff));

            raf.seek(40);
            raf.write((byte) (totalAudioLen & 0xff));
            raf.write((byte) ((totalAudioLen >> 8) & 0xff));
            raf.write((byte) ((totalAudioLen >> 16) & 0xff));
            raf.write((byte) ((totalAudioLen >> 24) & 0xff));
        }
    }



    private void sendAudioToServer(String audioFilePath) {
        File audioFile = new File(audioFilePath);
        if (audioFile.exists()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), audioFile);

            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("audio", audioFile.getName(), requestBody)
                    .build();

            Request request = new Request.Builder()
                    .url(requireContext().getResources().getString(com.Axonix.index.R.string.python_base_url) + "aud")
                    .post(multipartBody)
                    .build();

            OkHttpClient client = NetworkClient.INSTANCE.getClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    getActivity().runOnUiThread(() -> tvOutput.setText(result));
                }
            });
        }
    }



    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}
