package firstsample.mfcc_extractor.com.firstsample.Support;


import android.os.AsyncTask;

import firstsample.mfcc_extractor.com.firstsample.tarsosDSP.core.be.tarsos.dsp.AudioEvent;
import firstsample.mfcc_extractor.com.firstsample.tarsosDSP.core.be.tarsos.dsp.mfcc.MFCC;

public class ExtractMFCCsTask extends AsyncTask<AudioEvent, Void, Void> {
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
    protected Void doInBackground(AudioEvent... a) {
        MFCC mfcc = new MFCC(a[0].getBufferSize(), mSampleRate, 39, 40, 300, 8000);
        boolean isSuccess = mfcc.process(a[0]);

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