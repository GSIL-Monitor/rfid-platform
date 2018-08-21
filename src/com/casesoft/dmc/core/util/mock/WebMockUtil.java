package com.casesoft.dmc.core.util.mock;

import org.springframework.mock.web.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class WebMockUtil {
	
	private static MockHttpServletRequest mockRequest;
	
	  /**
	   * @return MockServletContext
	   */
	  public static ServletContext getServletContext() {
	    return new MockServletContext();
	  }
	  /**
	   * 
	   * @return MockServletConfig
	   */
	  public static ServletConfig getServletConfig() {
	    return new MockServletConfig(getServletContext());
	  }
	  /**
	   * @return MockHttpServletRequest
	   */
	  public static HttpServletRequest getHttpServletRequest() {
		  if(mockRequest != null)
			  return mockRequest;
	    mockRequest = 
	      new MockHttpServletRequest(getServletContext(), "POST", "");
	    mockRequest.setSession(getHttpSession());
	    return mockRequest;
	  }
	  
	  /**
	   * @return MockHttpServletResponse
	   */
	  public static HttpServletResponse getHttpServletResponse() {
	    return new MockHttpServletResponse();
	  }
	  
	  /**
	   * @return MockHttpSession
	   */
	  public static HttpSession getHttpSession() {
	    return new MockHttpSession(getServletContext(), "0");
	  }
	  
	  private WebMockUtil() {
	    //Do nothing
	  }
	}

