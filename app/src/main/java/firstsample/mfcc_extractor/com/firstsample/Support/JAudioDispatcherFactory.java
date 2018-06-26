package firstsample.mfcc_extractor.com.firstsample.Support;

import android.media.AudioRecord;

import firstsample.mfcc_extractor.com.firstsample.tarsosDSP.android.be.tarsos.dsp.io.android.AndroidAudioInputStream;
import firstsample.mfcc_extractor.com.firstsample.tarsosDSP.android.be.tarsos.dsp.io.android.AudioDispatcherFactory;
import firstsample.mfcc_extractor.com.firstsample.tarsosDSP.core.be.tarsos.dsp.AudioDispatcher;
import firstsample.mfcc_extractor.com.firstsample.tarsosDSP.core.be.tarsos.dsp.io.TarsosDSPAudioFormat;
import firstsample.mfcc_extractor.com.firstsample.tarsosDSP.core.be.tarsos.dsp.io.TarsosDSPAudioInputStream;

public class JAudioDispatcherFactory extends AudioDispatcherFactory {
    public static AudioDispatcher fromMic(int sampleRate, int audioBufferSize, int sampleSizeInBits,
                                          int bufferOverlap, AudioRecord recorder, int channel) {
        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, sampleSizeInBits, channel, true, false);
        TarsosDSPAudioInputStream audioStream = new AndroidAudioInputStream(recorder, format);
        return new AudioDispatcher(audioStream, audioBufferSize, bufferOverlap);
    }
}
