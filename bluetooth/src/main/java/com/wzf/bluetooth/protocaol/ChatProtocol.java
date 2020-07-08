package com.wzf.bluetooth.protocaol;

import java.io.UnsupportedEncodingException;

public class ChatProtocol implements ProtocolHandler<String>{
    private static final String CHARSET_NAME = "utf-8";
    @Override
    public byte[] encodePackage(String data) {
        if( data == null) {
            return new byte[0];
        }
        else {
            try {
                return data.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
    }

    @Override
    public String decodePackage(byte[] netData) {
        if( netData == null) {
            return "";
        }
        try {
            return new String(netData, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
