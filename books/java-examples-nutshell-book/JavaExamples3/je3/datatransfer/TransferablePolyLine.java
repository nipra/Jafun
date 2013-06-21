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
package je3.datatransfer;
import java.awt.datatransfer.*;
import je3.graphics.PolyLine;

/**
 * This class implements the Transferable interface for PolyLine objects.
 * It also defines a DataFlavor used to describe this data type.
 */
public class TransferablePolyLine implements Transferable {
    public static DataFlavor FLAVOR= new DataFlavor(PolyLine.class,"PolyLine");
    static DataFlavor[] FLAVORS = new DataFlavor[] { FLAVOR };
   
    PolyLine line;  // This is the PolyLine we wrap.
    public TransferablePolyLine(PolyLine line) { this.line = line; }

    /** Return the supported flavor */
    public DataFlavor[] getTransferDataFlavors() { return FLAVORS; }

    /** Check for the one flavor we support */
    public boolean isDataFlavorSupported(DataFlavor f){return f.equals(FLAVOR);}
   
    /** Return the wrapped PolyLine, if the flavor is right */
    public Object getTransferData(DataFlavor f)
	throws UnsupportedFlavorException 
    {
	if (!f.equals(FLAVOR)) throw new UnsupportedFlavorException(f);
	return line;
    }
}
