/**
 * Xsplinefun displays colorful moving splines in a window.
 *
 * Taken from xsplinefun, Distribution of 02may92, by Jef Poskanzer,
 *  jef@netcom.com, jef@well.sf.ca.us
 *
 * @copyright (C) 1992 by Jef Poskanzer
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose and without fee is hereby granted, provided
 * that the above copyright notice appear in all copies and that both that
 * copyright notice and this permission notice appear in supporting
 * documentation.  This software is provided "as is" without express or
 * implied warranty.
 *
 * First step in converting to Java was to junk all the X Windows stuff;
 * in the process we lost all the customization (need to re-add with getopt?).
 */
public class XSplineFun {
	public static final int POINTS = 20;
	public static final int DEFAULT_MAX_COLORS = 20;

	/* Spline-fun smarts. */

	static int x[POINTS], y[POINTS], dx[POINTS], dy[POINTS];
	static int nred, ngreen, nblue, dred, dgreen, dblue;
	static int color;
	static XColor xcolors[DEFAULT_MAX_COLORS];

	public static void main(String[] av) {
		Frame f = new Frame("Spline Fun");
		XSplineFun xf = new XSplineFun();
		xf.init_splines();
		f.add(xf);
		while(true)
			try {
				xf.move_splines();
				Thread.sleep(150);		// msec
			} catch (Exception e) {
				System.out.println(e);
			}
	}

	static void
	init_splines()
	{
		int i;

		/* Initialize points. */
		for ( i = 0; i < POINTS; ++i )
		{
		x[i] = random() % width;
		y[i] = random() % height;
		dx[i] = random() % ( MAX_DELTA * 2 ) - MAX_DELTA;
		if ( dx[i] <= 0 ) --dx[i];
		dy[i] = random() % ( MAX_DELTA * 2 ) - MAX_DELTA;
		if ( dy[i] <= 0 ) --dy[i];
		}

		/* Initalize colors. */
		for ( color = 0; color < ncolors; ++color )
		{
		xcolors[color].red = xcolors[color].green = xcolors[color].blue = 0;
		xcolors[color].pixel = pixels[color];
		xcolors[color].flags = DoRed|DoGreen|DoBlue;
		}
		color = 0;
		nred = ngreen = nblue = 0;
		dred = random() % ( MAX_COLOR_DELTA * 2 ) - MAX_COLOR_DELTA;
		if ( dred <= 0 ) --dred;
		dgreen = random() % ( MAX_COLOR_DELTA * 2 ) - MAX_COLOR_DELTA;
		if ( dgreen <= 0 ) --dgreen;
		dblue = random() % ( MAX_COLOR_DELTA * 2 ) - MAX_COLOR_DELTA;
		if ( dblue <= 0 ) --dblue;
	}

	static void
	rotate_colormap()
	{
		int t, i;

		if ( forwards )
		{
		t = xcolors[0].pixel;
		for ( i = 0; i < ncolors - 1; ++i )
			xcolors[i].pixel = xcolors[i + 1].pixel;
		xcolors[ncolors - 1].pixel = t;
		XStoreColors(display, cmap, xcolors, ncolors );
		}
		else if ( backwards )
		{
		t = xcolors[ncolors - 1].pixel;
		for ( i = ncolors - 1; i > 0; --i )
			xcolors[i].pixel = xcolors[i - 1].pixel;
		xcolors[0].pixel = t;
		XStoreColors(display, cmap, xcolors, ncolors );
		}
	}

	static void
	new_color()
	{
		int t;

		for ( ; ; )
		{
		t = (int) nred + dred;
		if ( t >= 0 && t < 65536 ) break;
		dred = random() % ( MAX_COLOR_DELTA * 2 ) - MAX_COLOR_DELTA;
		if ( dred <= 0 ) --dred;
		}
		xcolors[color].red = nred = t;
		for ( ; ; )
		{
		t = (int) ngreen + dgreen;
		if ( t >= 0 && t < 65536 ) break;
		dgreen = random() % ( MAX_COLOR_DELTA * 2 ) - MAX_COLOR_DELTA;
		if ( dgreen <= 0 ) --dgreen;
		}
		xcolors[color].green = ngreen = t;
		for ( ; ; )
		{
		t = (int) nblue + dblue;
		if ( t >= 0 && t < 65536 ) break;
		dblue = random() % ( MAX_COLOR_DELTA * 2 ) - MAX_COLOR_DELTA;
		if ( dblue <= 0 ) --dblue;
		}
		xcolors[color].blue = nblue = t;
		XStoreColor(display, cmap, &(xcolors[color]) );
		XSetForeground( display, gc, xcolors[color].pixel );
		if ( ++color >= ncolors ) color -= ncolors;
	}

	static void
	move_splines()
	{
		int i, t, px, py, zx, zy, nx, ny;

		/* Rotate colormap if necessary. */
		rotate_colormap();

		/* Choose new color. */
		new_color();

		/* Backwards rotation requires two new colors each loop. */
		if ( backwards )
		new_color();

		/* Move the points. */
		for ( i = 0; i < POINTS; i++ )
		{
		for ( ; ; )
			{
			t = x[i] + dx[i];
			if ( t >= 0 && t < width ) break;
			dx[i] = random() % ( MAX_DELTA * 2 ) - MAX_DELTA;
			if ( dx[i] <= 0 ) --dx[i];
			}
		x[i] = t;
		for ( ; ; )
			{
			t = y[i] + dy[i];
			if ( t >= 0 && t < height ) break;
			dy[i] = random() % ( MAX_DELTA * 2 ) - MAX_DELTA;
			if ( dy[i] <= 0 ) --dy[i];
			}
		y[i] = t;
		}

		/* Draw the figure. */
		px = zx = ( x[0] + x[POINTS-1] ) / 2;
		py = zy = ( y[0] + y[POINTS-1] ) / 2;
		for ( i = 0; i < POINTS-1; ++i )
		{
		nx = ( x[i+1] + x[i] ) / 2;
		ny = ( y[i+1] + y[i] ) / 2;
		XDrawSpline(g, px, py, x[i], y[i], nx, ny );
		px = nx;
		py = ny;
		}
		XDrawSpline(g, px, py, x[POINTS-1], y[POINTS-1], zx, zy );
	}


	/* X spline routine. */

	int abs(x) {
		return x < 0 ? -x : x;
	}

	static void
	XDrawSpline(Graphics g, int x0, y0, x1, y1, x2, y2) {
		register int xa, ya, xb, yb, xc, yc, xp, yp;

		xa = ( x0 + x1 ) / 2;
		ya = ( y0 + y1 ) / 2;
		xc = ( x1 + x2 ) / 2;
		yc = ( y1 + y2 ) / 2;
		xb = ( xa + xc ) / 2;
		yb = ( ya + yc ) / 2;

		xp = ( x0 + xb ) / 2;
		yp = ( y0 + yb ) / 2;
		if ( abs( xa - xp ) + abs( ya - yp ) > SPLINE_THRESH )
		XDrawSpline( display, d, gc, x0, y0, xa, ya, xb, yb );
		else
		XDrawLine( display, d, gc, x0, y0, xb, yb );

		xp = ( x2 + xb ) / 2;
		yp = ( y2 + yb ) / 2;
		if ( abs( xc - xp ) + abs( yc - yp ) > SPLINE_THRESH )
		XDrawSpline( display, d, gc, xb, yb, xc, yc, x2, y2 );
		else
		XDrawLine( display, d, gc, xb, yb, x2, y2 );
	}
}
