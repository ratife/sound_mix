package sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {
	static Mixer mixer;
	static Clip clip;
	public static void main(String a[]) {
		Info[] infoMix = AudioSystem.getMixerInfo(); 
		/*for(Info inf : infoMix) {
			System.out.println(inf.getName() + "-"+inf.getVendor() + "-"+inf.getDescription());
		}*/
		mixer = AudioSystem.getMixer(infoMix[0]);
		DataLine.Info info = new DataLine.Info(Clip.class,null);
		try {
			clip = (Clip) mixer.getLine(info);
			URL url1 = Main.class.getResource("/sound/1.wav");
			AudioInputStream inStream1 = AudioSystem.getAudioInputStream(url1);
			URL url2 = Main.class.getResource("/sound/5.wav");
			AudioInputStream inStream2 = AudioSystem.getAudioInputStream(url2);
			
			byte[] bits = mixe(inStream1,inStream2);
			System.out.println("Opening...." + inStream2.getFormat());
			//byte[] bits = getBit(inStream2);
			clip.open(inStream2.getFormat(),bits,0,bits.length);
			//clip.open(inStream1.getFormat(),bits,0,800);
			System.out.println("Starting....");
			clip.start();
			int cpt = 0;
			do {
				try {
					Thread.sleep(50);
					cpt ++;
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}while(cpt<2000);
			System.out.println("ending....");
			clip.stop();
			clip.close();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static byte[] getBit(AudioInputStream inStream1) throws IOException {
		int read;
		ByteArrayOutputStream outStream1 = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
	    while ((read = inStream1.read(buffer)) != -1) {
	    	outStream1.write(buffer, 0, read);
	    }
	    outStream1.flush();
	    
	    return outStream1.toByteArray();
	}


	static  byte[] mixe(AudioInputStream in1 , AudioInputStream in2) throws IOException {
		AudioInputStream ret;
		int read;
		ByteArrayOutputStream outStream1 = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
	    while ((read = in1.read(buffer)) != -1) {
	    	outStream1.write(buffer, 0, read);
	    }
	    outStream1.flush();
	    
	    byte[] byteBuffer1 = outStream1.toByteArray();
	    //------------
	    
	    ByteArrayOutputStream outStream2 = new ByteArrayOutputStream();
		buffer = new byte[1024];
	    while ((read = in2.read(buffer)) != -1) {
	    	outStream2.write(buffer, 0, read);
	    }
	    outStream2.flush();
	    
	    byte[] byteBuffer2 = outStream2.toByteArray();
	    
	    return mixBuffers1(byteBuffer1, byteBuffer2);
	    
	}

	
	
	private static byte[] mixBuffers1(byte[] bufferA, byte[] bufferB) {
		int min = bufferA.length>bufferB.length?bufferB.length:bufferA.length;
	    byte[] array = new byte[min];
	    for (int i=0; i<min; i++) {
	        array[i] = (byte) ((bufferA[i] + bufferB[i]));
	    }
	    return array;
	}
	
	private static byte[] mixBuffers2(byte[] bufferA, byte[] bufferB) {
		int min = bufferA.length>bufferB.length?bufferB.length:bufferA.length;
	    byte[] array = new byte[min];
	    
	    for (int i=0; i<min; i+=2) {
	        short buf1A = bufferA[i+1];
	        short buf2A = bufferA[i];
	        buf1A = (short) ((buf1A & 0xff) << 8);
	        buf2A = (short) (buf2A & 0xff);

	        short buf1B = bufferB[i+1];
	        short buf2B = bufferB[i];
	        buf1B = (short) ((buf1B & 0xff) << 8);
	        buf2B = (short) (buf2B & 0xff);

	        short buf1C = (short) (buf1A + buf1B);
	        short buf2C = (short) (buf2A + buf2B);

	        short res = (short) (buf1C | buf2C);

	        array[i] = (byte) res;
	        array[i+1] = (byte) (res >> 8);
	    }

	    return array;
	}
	
	private static byte[] mixBuffers3(byte[] bufferA, byte[] bufferB) {
		int min = bufferA.length>bufferB.length?bufferB.length:bufferA.length;
	    byte[] array = new byte[min];
	    
	    for (int i=0; i<min; i+=2) {
	        short buf1A = bufferA[i+1];
	        short buf2A = bufferA[i];
	        buf1A = (short) ((buf1A & 0xff) << 8);
	        buf2A = (short) (buf2A & 0xff);

	        short buf1B = bufferB[i+1];
	        short buf2B = bufferB[i];
	        buf1B = (short) ((buf1B & 0xff) << 8);
	        buf2B = (short) (buf2B & 0xff);

	        short buf1C = (short) (buf1A + buf1B);
	        short buf2C = (short) (buf2A + buf2B);

	        short res = (short) (buf1C + buf2C);

	        array[i] = (byte) res;
	        array[i+1] = (byte) (res >> 8);
	    }

	    return array;
	}
}
