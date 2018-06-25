package firstsample.mfcc_extractor.com.firstsample.Support;

import android.media.AudioRecord;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.android.AndroidAudioInputStream;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;

public class JAudioDispatcherFactory extends AudioDispatcherFactory {
    public static AudioDispatcher fromMic(int sampleRate, int audioBufferSize, int sampleSizeInBits,
                                          int bufferOverlap, AudioRecord recorder, int channel) {
        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, sampleSizeInBits, channel, true, false);
        TarsosDSPAudioInputStream audioStream = new AndroidAudioInputStream(recorder, format);
        return new AudioDispatcher(audioStream, audioBufferSize, bufferOverlap);
    }
}
