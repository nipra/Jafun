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

/**
 * Play a sound file from the network using the java.applet.Applet API.
 */
public class PlaySound {
    public static void main(String[] args)
	throws java.net.MalformedURLException
    {
	java.applet.AudioClip clip =
	    java.applet.Applet.newAudioClip(new java.net.URL(args[0]));
	clip.play();
    }
}
