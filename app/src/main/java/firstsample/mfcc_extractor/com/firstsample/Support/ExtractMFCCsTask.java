package firstsample.mfcc_extractor.com.firstsample.Support;

import android.os.AsyncTask;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.mfcc.MFCC;
import firstsample.mfcc_extractor.com.firstsample.MMFCC;

public class ExtractMFCCsTask extends AsyncTask<float[], Void, Void> {
    public interface OnProcessSuccessListener {
        void success(double[] vector);
    }

    private OnProcessSuccessListener mOnProcessSuccess;
    private int mSampleRate;

    public ExtractMFCCsTask(OnProcessSuccessListener onProcessSuccess, int sampleRate) {
        this.mOnProcessSuccess = onProcessSuccess;
        this.mSampleRate = sampleRate;
    }

    @Override
    protected Void doInBackground(float[]... a) {
        MMFCC mfcc = new MMFCC(Recorder.RECORDER_BUFFER_SIZE, mSampleRate, 39, 40, 300, 8000);
        boolean isSuccess = mfcc.jprocess(a[0]);

        if (!isSuccess) return null;
        double[] result = new double[mfcc.getMFCC().length];
        int i = 0;
        for (double d : mfcc.getMFCC()) {
            result[i++] = d;
        }

        mOnProcessSuccess.success(result);
        return null;
    }
}