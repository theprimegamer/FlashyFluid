import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class TheAudio {
	protected AudioInputStream theStream;
	protected int sampleCt, bytesPerFrame;
	public TheAudio(String file) throws UnsupportedAudioFileException, IOException{
		File fileIn = new File("AT&T.wav");
		this.theStream = AudioSystem.getAudioInputStream(fileIn);
		this.sampleCt = 0;
		this.bytesPerFrame = this.theStream.getFormat().getFrameSize();
	}
	public double[] getForces(int width, int sampleNum){ //width = size of the array to return, sampleNum = number of samples to take
		int numBytes = sampleNum * this.bytesPerFrame; 
		byte[] audioBytes = new byte[numBytes];
		DoubleFFT_1D datTransform = new DoubleFFT_1D(numBytes/2);
		int numBytesRead = -1;
		try {
			numBytesRead = this.theStream.read(audioBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (numBytesRead == -1)
			return null;
		this.sampleCt += numBytesRead/this.bytesPerFrame;
		double[] audioBytesD = new double[audioBytes.length]; // will drop last byte if odd number
		ByteBuffer bb = ByteBuffer.wrap(audioBytes);
		if (!this.theStream.getFormat().isBigEndian())
			bb.order(ByteOrder.LITTLE_ENDIAN); //WAV format is LI
		for (int i = 0; i < audioBytesD.length/2; i++) {
			audioBytesD[i] = bb.getShort();
		}
		datTransform.realForwardFull(audioBytesD);
		//audioBytesD now the real/imaginary pairings
		double[] magz = new double[audioBytesD.length/2];
		for (int i = 0; i < audioBytesD.length; i = i+2) {
			magz[i/2] = Math.sqrt(audioBytesD[i]*audioBytesD[i] + audioBytesD[i+1]*audioBytesD[i+1]);
		}
		// At this point, magz = magnitude of that power map thing... condense it down to width # of bins
		// Magz may need to be fukkt wit
		double[] out = new double[width];
		double binIncrement = magz.length/width;
		double currIndex = 0;
		for (int i = 0; i < out.length; i++){
			double sum = 0;
			int ct = 0;
			for (int j = (int)currIndex; j < currIndex + binIncrement; j++){
				sum += magz[j];
				ct++;
			}
			out[i] = sum/ct;
		}
		return out;
	}
}

