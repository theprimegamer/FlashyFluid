

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class FFTTest {
	public static void main(String[] args) {
		int totalFramesRead = 0;
		File fileIn = new File("AT&T.wav");
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
			int bytesPerFrame = audioInputStream.getFormat().getFrameSize();

			// Set an arbitrary buffer size of 1024 frames.

			int numBytes = 1024 * bytesPerFrame; 

			byte[] audioBytes = new byte[numBytes];
			DoubleFFT_1D datTransform = new DoubleFFT_1D(numBytes/2);
			audioInputStream.skip(1000000);

			try {
				int numBytesRead = 0;
				int numFramesRead = 0;
				while ((numBytesRead = 	audioInputStream.read(audioBytes)) != -1) {
					// Calculate the number of frames actually read.
					numFramesRead = numBytesRead / bytesPerFrame;
					totalFramesRead += numFramesRead;
					double[] audioBytesD = new double[audioBytes.length]; // will drop last byte if odd number
				    ByteBuffer bb = ByteBuffer.wrap(audioBytes);
				    if (!audioInputStream.getFormat().isBigEndian())
				    	bb.order(ByteOrder.LITTLE_ENDIAN); //WAV format is LI
				    for (int i = 0; i < audioBytesD.length/2; i++) {
				        audioBytesD[i] = bb.getShort();
				    }
				    datTransform.realForwardFull(audioBytesD);
				    System.out.println(audioBytesD);
				    double[] magz = new double[audioBytesD.length/2];
				    for (int i = 0; i < audioBytesD.length; i = i+2) {
				        magz[i/2] = Math.sqrt(audioBytesD[i]*audioBytesD[i] + audioBytesD[i+1]*audioBytesD[i+1]);
				    }
				    double[] freqz = new double[audioBytesD.length/2];
				    for (int i = 0; i < freqz.length; i++) {
				        freqz[i] = magz[i]*audioInputStream.getFormat().getSampleRate()/(audioBytesD.length/2);
				    }
					// Here, do something useful with the audio data that's 

					// now in the audioBytes array...

				}





			} catch (Exception ex) { 

				// Handle the error...

			}

		} catch (Exception e) {

			// Handle the error...

		}

	}


}