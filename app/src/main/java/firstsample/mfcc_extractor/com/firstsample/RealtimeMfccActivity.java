package firstsample.mfcc_extractor.com.firstsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.android.AudioDispatcherFactory;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.AudioDispatcher;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.AudioEvent;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.AudioProcessor;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.mfcc.MFCC;
import firstsample.mfcc_extractor.com.firstsample.Support.FileHelper;
import firstsample.mfcc_extractor.com.firstsample.Support.Recorder;

import static firstsample.mfcc_extractor.com.firstsample.Support.Recorder.RECORDER_BUFFER_SIZE;
import static firstsample.mfcc_extractor.com.firstsample.Support.Recorder.RECORDER_BPP;
import static firstsample.mfcc_extractor.com.firstsample.Support.Recorder.RECORDER_SAMPLERATE;

public class RealtimeMfccActivity extends AppCompatActivity implements Recorder.RecordActionListener, View.OnClickListener {
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
    public void onStartRecorder() {
        mDuration = System.currentTimeMillis();
        mtvInfo.setText("===================== \n");
        mtvInfo.append("\n- START RECORD");
        ((ScrollView) findViewById(R.id.scroll)).fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onStopped() {
        mtvInfo.append("\n- DURATION : " + (System.currentTimeMillis() - mDuration) + " ms");
        mtvInfo.append("\n- STOP RECORD");
        mtvInfo.append("\n ----- ");
        mtvInfo.append("\n- START EXTRACTION");
        mtvInfo.append("\n ... ");
        ((ScrollView) findViewById(R.id.scroll)).fullScroll(View.FOCUS_DOWN);
        mDuration = System.currentTimeMillis();
        this.extract();
        mIsRecording = false;
    }

    private void extract() {
        mMFCCVectors = new ArrayList<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(mRecorder.getCurrentPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert fileInputStream != null;

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(RECORDER_SAMPLERATE, RECORDER_BUFFER_SIZE, RECORDER_BPP,
                0, fileInputStream, 2, mRecorder);

        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                MFCC mfcc = new MFCC(1024, 44100, 39, 40, 300, 8000);
                boolean isSuccess = mfcc.process(audioEvent);

                if (isSuccess) {
                    double[] result = new double[mfcc.getMFCC().length];
                    int i = 0;
                    for (double d : mfcc.getMFCC()) {
                        result[i++] = d;
                    }
                    mMFCCVectors.add(result);
                }
                return true;
            }

            @Override
            public void processingFinished() {
                mtvInfo.append("\n- EXTRACTION DURATION : " + (System.currentTimeMillis() - mDuration) + " ms");
                mDuration = System.currentTimeMillis();
                mtvInfo.append("\n- FINISH EXTRACTION");
                mtvInfo.append("\n- MFCC COUNT : " + mMFCCVectors.size() + "\n");
                findViewById(R.id.btnSave).setEnabled(true);
                ((ScrollView) findViewById(R.id.scroll)).fullScroll(View.FOCUS_DOWN);
            }
        });
        dispatcher.run();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart: {
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
                mRecorder.stopRecording();
                break;
            }
            case R.id.btnSave: {
                StringBuilder builder = new StringBuilder();
                for (double[] vector : mMFCCVectors) {
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
    boolean mIsRecording = false;
    private Recorder mRecorder;
    private List<double[]> mMFCCVectors;
    private TextView mtvInfo;
    private long mDuration = 0;
    //endregion
}
