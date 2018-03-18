package com.casesoft.dmc.core.servlet;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by john on 2017/1/4.
 *  * servletRequest读取流不清空
 */
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] buffer;
    public BufferedServletRequestWrapper(HttpServletRequest request) throws IOException {
        super( request );
        InputStream is = request.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buff[] = new byte[ 1024 ];
        int read;
        while( ( read = is.read( buff ) ) > 0 ) {
            baos.write( buff, 0, read );
        }
        this.buffer = baos.toByteArray();
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new BufferedServletInputStream( this.buffer );
    }

}
