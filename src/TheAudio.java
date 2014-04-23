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
	protected int sampleCt, bytesPerFrame, sampleRate;
	public TheAudio(String file) throws UnsupportedAudioFileException, IOException{
		File fileIn = new File(file);
		this.theStream = AudioSystem.getAudioInputStream(fileIn);
		this.sampleCt = 0;
		this.bytesPerFrame = this.theStream.getFormat().getSampleSizeInBits()/4;
		this.sampleRate = (int)this.theStream.getFormat().getSampleRate();
	}
	public void skip(int nBytes){
		try {
			this.theStream.skip(nBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public double[] getForces(int width, int time){ //width = size of the array to return, time = time of window in ms
		int sampleNum = (int)((time*this.sampleRate)/((double) 1000));
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
		double maxSample = 0;
		for (int i = 0; i < audioBytesD.length/2; i++) {
			audioBytesD[i] = bb.getShort();
			if (Math.abs(audioBytesD[i]) > maxSample)
				maxSample = audioBytesD[i];
		}
		double sMult = maxSample/(double)25000;
		datTransform.realForwardFull(audioBytesD);
		//audioBytesD now the real/imaginary pairings
		double[] magz = new double[numBytes/4];
		double maxMag = 0;
		for (int i = 0; i < audioBytesD.length/2; i = i+2) {
			magz[i/2] = Math.sqrt(audioBytesD[i]*audioBytesD[i] + audioBytesD[i+1]*audioBytesD[i+1]);
			if (Math.abs(magz[i/2]) > maxMag)
				maxMag = magz[i/2];
		}
		// At this point, magz = magnitude of that power map thing... condense it down to width # of bins
		// Magz may need to be fukkt wit
		double[] out = new double[width];
		double binIncrement = magz.length/width;
		double currIndex = 0;
		double maxBin = 0;
		for (int i = 0; i < out.length; i++){
			double sum = 0;
			int ct = 0;
			for (int j = (int)currIndex; j < currIndex + binIncrement; j++){
				sum += magz[j];
				ct++;
			}
			out[i] = sum/(double)ct;
			currIndex += binIncrement;
			if (out[i] > maxBin)
				maxBin = out[i];
		}
		for (int i = 0; i < out.length; i++){
			out[i] = sMult*out[i]/maxBin;
		}
		return out;
	}
}

