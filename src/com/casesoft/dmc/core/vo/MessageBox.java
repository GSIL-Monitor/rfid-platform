package com.casesoft.dmc.core.vo;

/**
 * 响应Web请求的信息封装类，转换成JSON后返回给前台
 * @author Administrator
 *
 */
public class MessageBox {

	private boolean success;
	private String msg;
	private Object result;
    private String statusCode = "000";
	public MessageBox(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}
	public MessageBox(boolean success, String msg, Object result) {
		super();
		this.success = success;
		this.msg = msg;
		this.result = result;
	}
	public MessageBox() {
	}
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
	
}
