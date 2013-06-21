<HTML>
<HEAD>
	<TITLE>Ian Darwin's Computer Terms and Acronyms</TITLE>
	<%@ page import="java.io.*" %>
</HEAD>
<BODY BGCOLOR=white>
<H1>Ian Darwin's Computer Terms and Acronyms</H1>
<TABLE BORDER=2>
<TR><TH>Term<TH>Meaning</TR>
	<%
	// This part of the Servlet generates a list of lines like
	//	<TR> <TD>JSP <TD>Java Server Pages, a neat tool for ...

	// Filenames like this must NOT be read as parameters, since that
	// would allow any script kiddie to read any file on your system!!
	// In production code they would be read from a Properties file.
	String TERMSFILE = "/var/www/htdocs/hs/terms.txt";

	TermsAccessor tax = new TermsAccessor(TERMSFILE);
	Iterator it = tax.iterator();
	while (it.hasNext()) {
		Term t = it.next();
		out.print("<TR><TD>");
		out.print(t.term);
		out.print("</TD><TD>");
		out.print(t.definition);
		out.println("</TD></TR>");
	}
	%>
</TABLE>
<HR></HR>
<A HREF="/servlet/TermsServletPDF">Printer-friendly (Acrobat PDF) version</A>
<HR></HR>
<A HREF="/contact.html">Ask about another term</A>
<HR></HR>
<A HREF="index.html">Back to HS</A> <A HREF="../">Back to DarwinSys</A>
<HR></HR>
<H6>Produced by $Id: terms.jsp,v 1.6 2002/01/26 19:04:11 ian Exp $
using
<%= ident %>.
</H6>
