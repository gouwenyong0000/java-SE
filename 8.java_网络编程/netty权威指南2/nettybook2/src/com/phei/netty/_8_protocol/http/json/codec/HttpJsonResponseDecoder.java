/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phei.netty._8_protocol.http.json.codec;

import com.phei.netty._8_protocol.http.xml.codec.AbstractHttpXmlDecoder;
import com.phei.netty._8_protocol.http.xml.codec.HttpXmlResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * @author Lilinfeng
 * @date 2014年3月1日
 * @version 1.0
 */
public class HttpJsonResponseDecoder extends
        AbstractHttpJsonDecoder<DefaultFullHttpResponse> {

    public HttpJsonResponseDecoder(Class<?> clazz) {
	this(clazz, false);
    }

    public HttpJsonResponseDecoder(Class<?> clazz, boolean isPrintlog) {
	super(clazz, isPrintlog);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx,
	    DefaultFullHttpResponse msg, List<Object> out) throws Exception {
	HttpXmlResponse resHttpXmlResponse = new HttpXmlResponse(msg, decode0(
		ctx, msg.content()));
	out.add(resHttpXmlResponse);
    }

}
