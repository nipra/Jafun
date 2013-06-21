package com.darwinsys.macosui;

import javax.swing.JFrame;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.ApplicationListener;

/**
 * The only os-dependant part of com.darwinsys, this is the
 * adapter class to handle MacOS's "different" way of doing About Box,
 * Quit item in App menu, Preferences, and so on.
 */
public class MacOSAppAdapter extends Application {
	ApplicationListener appListener;
	JFrame  parent;
	AboutBoxHandler abouter;
	PrefsHandler prefser;
	PrintHandler printer;
	ShutdownHandler shutter;


	/** Construct a MacOSAppAdapter.
	 * @param theParent A JFrame, usually the main application window.
	 * @param about A handler for the About box.
	 * @param prefs A Preferences handler.
	 * @param print A Print handler (bug: does not get invoked now).
	 * @param shut A shutdown handler
	 */
	public MacOSAppAdapter(JFrame theParent, AboutBoxHandler about,
		PrefsHandler prefs, PrintHandler print, ShutdownHandler shut) {
		appListener = new MyAppEventHandler();
		parent = theParent;

		if (about != null) {
			abouter = about;
			setEnabledAboutMenu(true);
			addAboutMenuItem();
		}

		if (prefs != null) {
			prefser = prefs;
			setEnabledPreferencesMenu(true);
			addPreferencesMenuItem();
		}
		
		printer = print;

		shutter = shut;
	}

	/** Method to register this handler with Apple's event manager, calling
	 * addApplicationListener in parent class.
	 */
	public void register() {
		addApplicationListener(appListener);
	}

	/** Inner class to provide ApplicationListener support. */
	class MyAppEventHandler implements ApplicationListener {

		/** This is called when the user does Application->About */
		public void handleAbout(ApplicationEvent event) {
			abouter.showAboutBox(parent);
			event.setHandled(true);
		}

		/** Called when the user does Application->Preferences */
		public void handlePreferences(ApplicationEvent event) {
			if (prefser != null)
				prefser.showPrefsDialog(parent);
				event.setHandled(true);
		}

		public void handlePrint(ApplicationEvent event) {
			if (printer != null)
				printer.doPrint(parent);
				event.setHandled(true);
		}

		/** This is called when the user does Application->Quit */
		public void handleQuit(ApplicationEvent event) {
			if (shutter != null)
				shutter.shutdown(parent);
			System.exit(0);	// should be notreached
		}

		/**
		 * @see com.apple.eawt.ApplicationListener#handleOpenApplication(com.apple.eawt.ApplicationEvent)
		 */
		public void handleOpenApplication(ApplicationEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * @see com.apple.eawt.ApplicationListener#handleOpenFile(com.apple.eawt.ApplicationEvent)
		 */
		public void handleOpenFile(ApplicationEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * @see com.apple.eawt.ApplicationListener#handlePrintFile(com.apple.eawt.ApplicationEvent)
		 */
		public void handlePrintFile(ApplicationEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * @see com.apple.eawt.ApplicationListener#handleReOpenApplication(com.apple.eawt.ApplicationEvent)
		 */
		public void handleReOpenApplication(ApplicationEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
}
