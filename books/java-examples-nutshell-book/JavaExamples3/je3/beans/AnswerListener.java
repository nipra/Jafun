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
package je3.beans;

/**
 * Classes that want to be notified when the user clicks a button in a
 * YesNoPanel should implement this interface.  The method invoked depends
 * on which button the user clicked.
 **/
public interface AnswerListener extends java.util.EventListener {
    public void yes(AnswerEvent e);
    public void no(AnswerEvent e);
    public void cancel(AnswerEvent e);
}
