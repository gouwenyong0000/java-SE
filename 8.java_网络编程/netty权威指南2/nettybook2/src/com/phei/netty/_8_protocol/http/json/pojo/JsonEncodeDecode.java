package com.phei.netty._8_protocol.http.json.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JsonEncodeDecode {

    public static Order  decoder(ByteBuf body){
        byte[] bytes = new byte[body.readableBytes()];
        body.readBytes(bytes);
        String s = new String(bytes);


        Order order = new Gson().fromJson(s, Order.class);
        return order;

    }

    public static String encoder(Order obj) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);

    }
}
