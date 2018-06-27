/*
 *      _______                       _____   _____ _____
 *     |__   __|                     |  __ \ / ____|  __ \
 *        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
 *        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/
 *        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |
 *        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|
 *
 * -------------------------------------------------------------
 *
 * TarsosDSP is developed by Joren Six at IPEM, University Ghent
 *
 * -------------------------------------------------------------
 *
 *  Info: http://0110.be/tag/TarsosDSP
 *  Github: https://github.com/JorenSix/TarsosDSP
 *  Releases: http://0110.be/releases/TarsosDSP/
 *
 *  TarsosDSP includes modified source code by various authors,
 *  for credits and info, see README.
 *
 */

package firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.android;

import android.media.AudioRecord;

import java.io.File;
import java.io.FileInputStream;

import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.AudioDispatcher;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.io.TarsosDSPAudioFormat;
import firstsample.mfcc_extractor.com.firstsample.DroidTarsosDSP.core.io.TarsosDSPAudioInputStream;

public class AudioDispatcherFactory {
    public static AudioDispatcher fromMic(int sampleRate, int audioBufferSize, int sampleSizeInBits,
                                          int bufferOverlap, AudioRecord recorder, int channel, AudioDispatcher.NeedToReadBufferCallback mufferCallback) {
        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, sampleSizeInBits, channel, true, false);
        TarsosDSPAudioInputStream audioStream = new AndroidAudioInputStream(recorder, format);
        return new AudioDispatcher(audioStream, audioBufferSize, bufferOverlap, mufferCallback);
    }

    public static AudioDispatcher fromFile(int sampleRate, int audioBufferSize, int sampleSizeInBits,
                                           int bufferOverlap, FileInputStream recorder, int channel, AudioDispatcher.NeedToReadBufferCallback bufferCallback) {

        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, sampleSizeInBits, channel, true, false);
        TarsosDSPAudioInputStream audioStream = new AndroidFileInputStream(recorder, format);
        return new AudioDispatcher(audioStream, audioBufferSize, bufferOverlap, bufferCallback);
    }
}
