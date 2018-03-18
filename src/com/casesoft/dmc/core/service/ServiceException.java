package com.casesoft.dmc.core.service;
/**
 * Service层的异常类
 * @author Administrator
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
		
	protected Throwable rootCause;
	
	protected String code;

    public ServiceException(String msg) {
        super(msg);
    }
    
    public ServiceException(String code, String msg) {
    	super(msg);
    	this.code = code;
    }

    public ServiceException(Throwable rootCause) {
        super();
        this.rootCause = rootCause;
    }

    public ServiceException(String msg, Throwable rootCause) {
        super(msg);
        this.rootCause = rootCause;
    }

    public Throwable getException() {
        return (this.rootCause);
    }

    public String getMessage() {
        String msg = super.getMessage();

        if (msg == null && rootCause != null) {
            msg = rootCause.getMessage();
        }

        return msg;
    }
    
    public String getCode() {
    	return code;
    }

}
