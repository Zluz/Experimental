package jmr.px107;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.TargetDataLine;

public class AudioInventory {

	public static void doGetLinesFromSystem() {
//		final Mixer mixer = new com.sun.media.sound.DirectAudioDevice();
//		final Mixer mixer = new com.sun.media.sound.PortMixer();
//		final Mixer mixer = new com.sun.media.sound.SoftMixingMixer();
//		final Line.Info[] lines = mixer.getSourceLineInfo();
//		Line.Info[] getTargetLineInfo()
		
		final Info[] arrInfo = AudioSystem.getMixerInfo();
		for ( final Info info : arrInfo ) {
			
			System.out.println( "Info: " + info );
			
			final Mixer mixer = AudioSystem.getMixer( info );
			System.out.println( "\tMixer: " + mixer );
			
			final Line[] sourcelines = mixer.getSourceLines();
			if ( sourcelines.length>0 ) {
				for ( final Line line : sourcelines ) {
					System.out.println( "\t\tSource Line: " + line );
				}
			} else {
				System.out.println( "\t\tNo source lines." );
			}
			
			final Line[] targetlines = mixer.getTargetLines();
			if ( targetlines.length>0 ) {
				for ( final Line line : targetlines ) {
					System.out.println( "\t\tTarget Line: " + line );
				}
			} else {
				System.out.println( "\t\tNo target lines." );
			}

			
		}
		
	}
	
	public static void doGetLineDirectly() {

		float sampleRate = 44100;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = false;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(
				sampleRate, sampleSizeInBits, channels, signed, bigEndian);
//		AudioFormat format = AudioFormat

		
		final TargetDataLine line;
		
		final DataLine.Info 
				info = new DataLine.Info( TargetDataLine.class, format ); 
		
		if ( !AudioSystem.isLineSupported(info) ) {
		    // Handle the error.
			System.out.println( "Line is NOT Supported: " + info );
	    } else {
			System.out.println( "Line IS Supported: " + info );
	    }
		    // Obtain and open the line.
		try {
			System.out.println( "Attempting to open the line.." );
		    line = (TargetDataLine) AudioSystem.getLine(info);
		    line.open(format);
		    System.out.println( "Line opened." );
		} catch (LineUnavailableException ex) {
		        // Handle the error.
			System.err.println( "Exception encountered: " + ex.toString() );
		    //... 
		}
	}
	
	
	
	public static void main( final String[] args ) {
//		doGetLineDirectly();
		doGetLinesFromSystem();
	}
	
}
