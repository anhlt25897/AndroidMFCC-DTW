package firstsample.mfcc_extractor.com.firstsample.Support;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    private final static String fileName = "data.txt";
    private final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MFCC_TEXT_RES/";
    private final static String TAG = FileHelper.class.getName();

    public static List<double[]> ReadFile(String fileName, int noFeatures) {
        List<double[]> resList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path + fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null && !TextUtils.isEmpty(line)) {
                line = line.replace(",", "").trim();
                int i = 0;
                double[] doubles = new double[noFeatures];
                for (String v : line.split(" ")) {
                    double d = Double.valueOf(v.trim());
                    doubles[i++] = d;
                }
                resList.add(doubles);
            }
            fileInputStream.close();

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return resList;
    }

    public static void saveToFile(String data) {
        try {
            new File(path).mkdir();
            File file = new File(path + fileName);
            file.delete();
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }
}