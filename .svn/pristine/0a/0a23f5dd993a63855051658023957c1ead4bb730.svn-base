package com.dxc.darklight.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class InflaterDecompressingEntity extends HttpEntityWrapper{

	public InflaterDecompressingEntity(HttpEntity wrapped) {
		super(wrapped);
	}

	@Override
	public InputStream getContent() throws IOException {
		// the wrapped entity's getContent() decides about repeatability
		InputStream wrappedin = wrappedEntity.getContent();
		// 注意：InflaterInputStream的第二个参数，不设第一个参数，解析http://news.imobile.com.cn出错
		return new InflaterInputStream(wrappedin, new Inflater(true));
	}
	@Override
	public long getContentLength() {
		return -1;
	}
}
