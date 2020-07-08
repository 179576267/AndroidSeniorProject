package com.wzf.bluetooth.protocaol;

public interface ProtocolHandler<T> {
    public byte[] encodePackage(T data);
    public T decodePackage(byte[] netData);
}
