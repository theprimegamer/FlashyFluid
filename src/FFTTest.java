

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;


public class FFTTest {

	public static void main(String[] args) {

		int totalFramesRead = 0;

		File fileIn = new File("AT&T.wav");

		// somePathName is a pre-existing string whose value was

		// based on a user selection.

		try {

			AudioInputStream audioInputStream = 

					AudioSystem.getAudioInputStream(fileIn);

			int bytesPerFrame = 

					audioInputStream.getFormat().getFrameSize();

			if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {

				// some audio formats may have unspecified frame size

				// in that case we may read any amount of bytes

				bytesPerFrame = 1;

			} 

			// Set an arbitrary buffer size of 1024 frames.

			int numBytes = 1024 * bytesPerFrame; 

			byte[] audioBytes = new byte[numBytes];
			AudioFormat fuckyou = audioInputStream.getFormat();
			DoubleFFT_1D datTransform = new DoubleFFT_1D(numBytes/2);
			audioInputStream.skip(1000000);

			try {

				int numBytesRead = 0;

				int numFramesRead = 0;

				// Try to read numBytes bytes from the file.

				while ((numBytesRead = 

						audioInputStream.read(audioBytes)) != -1) {

					// Calculate the number of frames actually read.

					numFramesRead = numBytesRead / bytesPerFrame;

					totalFramesRead += numFramesRead;
					double[] audioBytesD = new double[audioBytes.length]; // will drop last byte if odd number
				    ByteBuffer bb = ByteBuffer.wrap(audioBytes);
				    bb.order(ByteOrder.LITTLE_ENDIAN);
				    for (int i = 0; i < audioBytesD.length/2; i++) {
				        audioBytesD[i] = bb.getShort();
				    }
				    datTransform.realForwardFull(audioBytesD);
				    for (int i = 0; i < audioBytesD.length; i++) {
				    	if (audioBytesD[i] == 0)
				    		continue;
				    	audioBytesD[i] = Math.log10(Math.abs(audioBytesD[i]))/Math.log10(2);
				    }
				    System.out.println(audioBytesD);
				    double[] magz = new double[audioBytesD.length/2];
				    for (int i = 0; i < audioBytesD.length; i = i+2) {
				        magz[i/2] = Math.sqrt(audioBytesD[i]*audioBytesD[i] + audioBytesD[i+1]*audioBytesD[i+1]);
				    }
				    double[] freqz = new double[audioBytesD.length/2];
				    for (int i = 0; i < freqz.length; i++) {
				        freqz[i] = magz[i]*fuckyou.getSampleRate()/(audioBytesD.length/2);
				    }
				    System.out.println(freqz);
				    int fuckoff = 192;
				    fuckoff++;
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