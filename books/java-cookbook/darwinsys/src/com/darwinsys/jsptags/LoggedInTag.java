package com.darwinsys.jsptags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * LoggedInUser - include body content if the user is logged in as "user".
 * @version $Id: LoggedInTag.java,v 1.2 2004/06/16 18:50:24 ian Exp $
 */
public class LoggedInTag extends BodyTagSupport {
	private String userName;

	/** Invoked at the tag start boundary; does the work */
	public int doStartTag() throws JspException {
		String myLabel = null;

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		return request.getRemoteUser().equals(null) ? 
				SKIP_BODY :
				EVAL_BODY_INCLUDE  ;

	}
}
