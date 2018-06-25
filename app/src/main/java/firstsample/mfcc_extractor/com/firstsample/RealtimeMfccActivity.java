package firstsample.mfcc_extractor.com.firstsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import firstsample.mfcc_extractor.com.firstsample.Support.ExtractMFCCsTask;
import firstsample.mfcc_extractor.com.firstsample.Support.FileHelper;
import firstsample.mfcc_extractor.com.firstsample.Support.Recorder;

import static firstsample.mfcc_extractor.com.firstsample.Support.Recorder.RECORDER_BUFFER_SIZE;
import static firstsample.mfcc_extractor.com.firstsample.Support.Recorder.RECORDER_SAMPLERATE;

public class RealtimeMfccActivity extends AppCompatActivity implements ExtractMFCCsTask.OnProcessSuccessListener
        , Recorder.RecordActionListenner, View.OnClickListener {
    //region SYSTEM EVENTS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_mfcc);

        mtvInfo = findViewById(R.id.info);
        setButtonHandlers();
        enableButtons(false);
    }

    long size = 0;
    //endregion

    //region VIEW EVENTS
    @Override
    public void success(double[] vector) {
        Log.i("LOGGGGGG", "B. SUCCESS: " + Arrays.toString(vector));
        this.vectors.add(vector);
    }

    @Override
    public void onStartRecorder() {
        vectors = new ArrayList<>();
    }

    @Override
    public void onRecording(float[] data) {
        ExtractMFCCsTask task = new ExtractMFCCsTask(this, RECORDER_SAMPLERATE);
        task.execute(data);
    }

    @Override
    public void onStopped() {
        mIsRecording = false;
        Log.i("LOGGGGGG", "B. STOPPED: " + size);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart: {
                size = 0;
                mIsRecording = true;
                enableButtons(true);
                findViewById(R.id.btnSave).setEnabled(false);
                mRecorder = new Recorder(this, this);
                mRecorder.startRecording();
                break;
            }
            case R.id.btnStop: {
                mIsRecording = false;
                enableButtons(false);
                findViewById(R.id.btnSave).setEnabled(true);
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
        }
    }
    //endregion

    //region SUPPORTS
    private void setButtonHandlers() {
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
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
    boolean mIsRecording = false;
    private Recorder mRecorder;
    private List<double[]> vectors;
    private TextView mtvInfo;
    //endregion
}
