package firstsample.mfcc_extractor.com.firstsample;

import be.tarsos.dsp.mfcc.MFCC;

public class MMFCC extends MFCC {
    private float[] mfcc;
    private int centerFrequencies[];
    private int amountOfMelFilters; //Number of mel filters (SPHINX-III uses 40)
    private float lowerFilterFreq; //lower limit of filter (or 64 Hz?)
    private float upperFilterFreq; //upper limit of filter (or half of sampling freq.?)

    private int samplesPerFrame;
    private float sampleRate;

    public MMFCC(int samplesPerFrame, float sampleRate, int amountOfCepstrumCoef, int amountOfMelFilters, float lowerFilterFreq, float upperFilterFreq) {
        super(samplesPerFrame, sampleRate, amountOfCepstrumCoef, amountOfMelFilters, lowerFilterFreq, upperFilterFreq);
        this.samplesPerFrame = samplesPerFrame;
        this.sampleRate = sampleRate;
        this.amountOfMelFilters = amountOfMelFilters;
        this.lowerFilterFreq = lowerFilterFreq;
        this.upperFilterFreq = upperFilterFreq;
        jcalculateFilterBanks();
    }

    public boolean jprocess(float[] audioFloatBuffer) {
        // Magnitude Spectrum
        float bin[] = magnitudeSpectrum(audioFloatBuffer);
        // get Mel Filterbank
        float fbank[] = melFilter(bin, centerFrequencies);
        // Non-linear transformation
        float f[] = nonLinearTransformation(fbank);
        // Cepstral coefficients
        mfcc = cepCoefficients(f);

        return true;
    }

    private void jcalculateFilterBanks() {
        centerFrequencies = new int[amountOfMelFilters + 2];

        centerFrequencies[0] = Math.round(lowerFilterFreq / sampleRate * samplesPerFrame);
        centerFrequencies[centerFrequencies.length - 1] = samplesPerFrame / 2;

        double mel[] = new double[2];
        mel[0] = freqToMel(lowerFilterFreq);
        mel[1] = freqToMel(upperFilterFreq);

        float factor = (float) ((mel[1] - mel[0]) / (amountOfMelFilters + 1));
        //Calculates te centerfrequencies.
        for (int i = 1; i <= amountOfMelFilters; i++) {
            float fc = (inverseMel(mel[0] + factor * i) / sampleRate) * samplesPerFrame;
            centerFrequencies[i - 1] = Math.round(fc);
        }
    }

    /**
     * calculates the inverse of Mel Frequency<br>
     * calls: none<br>
     * called by: featureExtraction
     */
    private static float inverseMel(double x) {
        return (float) (700 * (Math.pow(10, x / 2595) - 1));
    }

    public float[] getMFCC() {
        return mfcc;
    }
}
