package firstsample.mfcc_extractor.com.firstsample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.android.AudioDispatcherFactory;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.AudioDispatcher;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.AudioEvent;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.AudioProcessor;
import firstsample.mfcc_extractor.com.firstsample.Support.ExtractMFCCsTask;
import firstsample.mfcc_extractor.com.firstsample.Support.FileHelper;
import firstsample.mfcc_extractor.com.firstsample.Support.Recorder;

import static firstsample.mfcc_extractor.com.firstsample.Support.Recorder.RECORDER_BPP;
import static firstsample.mfcc_extractor.com.firstsample.Support.Recorder.RECORDER_BUFFER_SIZE;
import static firstsample.mfcc_extractor.com.firstsample.Support.Recorder.RECORDER_SAMPLERATE;

public class RealtimeMfccActivity extends AppCompatActivity implements ExtractMFCCsTask.OnProcessSuccessListener
        , Recorder.RecordActionListener, View.OnClickListener {
    //region SYSTEM EVENTS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_mfcc);

        mtvInfo = findViewById(R.id.info);
        setButtonHandlers();
        enableButtons(false);
    }
    //endregion

    //region VIEW EVENTS
    @Override
    public void success(double[] vector) {
        this.vectors.add(vector);
    }

    @Override
    public void onStartRecorder() {
    }

    @Override
    public void onStopped() {
    }

    private void extract() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dispatcher = AudioDispatcherFactory.fromMic(RECORDER_SAMPLERATE, RECORDER_BUFFER_SIZE, RECORDER_BPP,
                        0, mRecorder.getRecorder(), 2, mRecorder);
                dispatcher.addAudioProcessor(new AudioProcessor() {
                    @Override
                    public boolean process(AudioEvent audioEvent) {
                        ExtractMFCCsTask task = new ExtractMFCCsTask(RealtimeMfccActivity.this, RECORDER_SAMPLERATE);
                        task.execute(audioEvent);
                        return true;
                    }

                    @Override
                    public void processingFinished() {
                        Log.i("LOGGGGGGGGG", "processingFinished: " + vectors.size());
                    }
                });
                dispatcher.run();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart: {
                enableButtons(true);
                findViewById(R.id.btnSave).setEnabled(false);
                mRecorder = new Recorder(this, this);
                vectors = new ArrayList<>();
                extract();
                mRecorder.startRecording();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enableButtons(false);
                                findViewById(R.id.btnSave).setEnabled(true);
                                dispatcher.stop();
                                mRecorder.stopRecording();
                            }
                        });
                    }
                }, 3000);
                break;
            }
            case R.id.btnStop: {
                enableButtons(false);
                findViewById(R.id.btnSave).setEnabled(true);
                dispatcher.stop();
                mRecorder.stopRecording();
                break;
            }
            case R.id.btnSave: {
                StringBuilder builder = new StringBuilder();
                for (double[] vector : vectors) {
                    StringBuilder cell = new StringBuilder();
                    for (double vt : vector) {
                        cell.append(String.valueOf(vt)).append(", ");
                    }
                    builder.append(cell).append("\r\n");
                }
                FileHelper.saveToFile(builder.toString());
                break;
            }
            case R.id.btnCmp: {
                Intent intent = new Intent(RealtimeMfccActivity.this, CompareActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
    //endregion

    //region SUPPORTS
    private void setButtonHandlers() {
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnCmp).setOnClickListener(this);
    }

    private void enableButton(int id, boolean isEnable) {
        findViewById(id).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }
    //endregion

    //region VARS
    private Recorder mRecorder;
    private List<double[]> vectors;
    private TextView mtvInfo;
    private AudioDispatcher dispatcher;
    //endregion
}
