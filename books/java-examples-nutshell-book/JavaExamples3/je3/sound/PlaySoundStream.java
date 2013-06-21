/*
 * Copyright (c) 2004 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 3nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose,
 * including teaching and use in open-source projects.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book, 
 * please visit http://www.davidflanagan.com/javaexamples3.
 */
package je3.sound;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;

/**
 * This class plays sounds streaming from a URL: it does not have to preload
 * the entire sound into memory before playing it. It is a command-line
 * application with no gui. It includes code to convert ULAW and ALAW
 * audio formats to PCM so they can be played. Use the -m command-line option
 * before MIDI files.
 */
public class PlaySoundStream {
    // Create a URL from the command-line argument and pass it to the 
    // right static method depending on the presence of the -m (MIDI) option.
    public static void main(String[] args) throws Exception {
	if (args[0].equals("-m")) streamMidiSequence(new URL(args[1]));
	else streamSampledAudio(new URL(args[0]));

	// Exit explicitly.
	// This is needed because the audio system starts background threads.
	System.exit(0);
    }

    /** Read sampled audio data from the specified URL and play it */
    public static void streamSampledAudio(URL url)
	throws IOException, UnsupportedAudioFileException,
	       LineUnavailableException
    {
	AudioInputStream ain = null;  // We read audio data from here
	SourceDataLine line = null;   // And write it here.

	try {
	    // Get an audio input stream from the URL
	    ain=AudioSystem.getAudioInputStream(url);

	    // Get information about the format of the stream
	    AudioFormat format = ain.getFormat();
	    DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);

	    // If the format is not supported directly (i.e. if it is not PCM
	    // encoded, then try to transcode it to PCM.
	    if (!AudioSystem.isLineSupported(info)) {
		// This is the PCM format we want to transcode to.
		// The parameters here are audio format details that you
		// shouldn't need to understand for casual use.
		AudioFormat pcm =
		    new AudioFormat(format.getSampleRate(), 16,
				    format.getChannels(), true, false);

		// Get a wrapper stream around the input stream that does the
		// transcoding for us.
		ain = AudioSystem.getAudioInputStream(pcm, ain);

		// Update the format and info variables for the transcoded data
		format = ain.getFormat(); 
		info = new DataLine.Info(SourceDataLine.class, format);
	    }

	    // Open the line through which we'll play the streaming audio.
	    line = (SourceDataLine) AudioSystem.getLine(info);
	    line.open(format);  

	    // Allocate a buffer for reading from the input stream and writing
	    // to the line.  Make it large enough to hold 4k audio frames.
	    // Note that the SourceDataLine also has its own internal buffer.
	    int framesize = format.getFrameSize();
	    byte[] buffer = new byte[4 * 1024 * framesize]; // the buffer
	    int numbytes = 0;                               // how many bytes

	    // We haven't started the line yet.
	    boolean started = false;

	    for(;;) {  // We'll exit the loop when we reach the end of stream
		// First, read some bytes from the input stream.
		int bytesread=ain.read(buffer,numbytes,buffer.length-numbytes);
		// If there were no more bytes to read, we're done.
		if (bytesread == -1) break;
		numbytes += bytesread;
		
		// Now that we've got some audio data, to write to the line,
		// start the line, so it will play that data as we write it.
		if (!started) {
		    line.start();
		    started = true;
		}
		
		// We must write bytes to the line in an integer multiple of
		// the framesize.  So figure out how many bytes we'll write.
		int bytestowrite = (numbytes/framesize)*framesize;
		
		// Now write the bytes. The line will buffer them and play
		// them. This call will block until all bytes are written.
		line.write(buffer, 0, bytestowrite);
		
		// If we didn't have an integer multiple of the frame size, 
		// then copy the remaining bytes to the start of the buffer.
		int remaining = numbytes - bytestowrite;
		if (remaining > 0)
		    System.arraycopy(buffer,bytestowrite,buffer,0,remaining);
		numbytes = remaining;
	    }

	    // Now block until all buffered sound finishes playing.
	    line.drain();
	}
	finally { // Always relinquish the resources we use
	    if (line != null) line.close();
	    if (ain != null) ain.close();
	}
    }

    // A MIDI protocol constant that isn't defined by javax.sound.midi
    public static final int END_OF_TRACK = 47;

    /* MIDI or RMF data from the specified URL and play it */
    public static void streamMidiSequence(URL url)
        throws IOException, InvalidMidiDataException, MidiUnavailableException
    {
	Sequencer sequencer=null;     // Converts a Sequence to MIDI events
	Synthesizer synthesizer=null; // Plays notes in response to MIDI events

	try {
	    // Create, open, and connect a Sequencer and Synthesizer
	    // They are closed in the finally block at the end of this method.
	    sequencer = MidiSystem.getSequencer();
	    sequencer.open();  
	    synthesizer = MidiSystem.getSynthesizer();
	    synthesizer.open();
	    sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());

	    // Specify the InputStream to stream the sequence from
	    sequencer.setSequence(url.openStream());  
	    
	    // This is an arbitrary object used with wait and notify to 
	    // prevent the method from returning before the music finishes
	    final Object lock = new Object();

	    // Register a listener to make the method exit when the stream is 
	    // done. See Object.wait() and Object.notify()
	    sequencer.addMetaEventListener(new MetaEventListener() {
		    public void meta(MetaMessage e) {
			if (e.getType() == END_OF_TRACK) {
			    synchronized(lock) { 
				lock.notify();
			    }
			}
		    }
		});
	    
	    // Start playing the music
	    sequencer.start();
	    
	    // Now block until the listener above notifies us that we're done.
	    synchronized(lock) {
		while(sequencer.isRunning()) {
		    try { lock.wait(); } catch(InterruptedException e) {}
		}
	    }
	}
	finally {
	    // Always relinquish the sequencer, so others can use it.
	    if (sequencer != null) sequencer.close();
	    if (synthesizer != null) synthesizer.close();
	}
    }
}
