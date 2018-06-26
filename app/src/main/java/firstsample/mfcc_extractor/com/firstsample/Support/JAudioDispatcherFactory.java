package firstsample.mfcc_extractor.com.firstsample.Support;

import android.media.AudioRecord;

import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.android.AndroidAudioInputStream;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.android.AudioDispatcherFactory;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.AudioDispatcher;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.io.TarsosDSPAudioFormat;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.io.TarsosDSPAudioInputStream;

public class JAudioDispatcherFactory extends AudioDispatcherFactory {
    public static AudioDispatcher fromMic(int sampleRate, int audioBufferSize, int sampleSizeInBits,
                                          int bufferOverlap, AudioRecord recorder, int channel) {
        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, sampleSizeInBits, channel, true, false);
        TarsosDSPAudioInputStream audioStream = new AndroidAudioInputStream(recorder, format);
        return new AudioDispatcher(audioStream, audioBufferSize, bufferOverlap);
    }
}
