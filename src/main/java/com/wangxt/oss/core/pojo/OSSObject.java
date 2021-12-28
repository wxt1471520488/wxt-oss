package com.wangxt.oss.core.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
public class OSSObject {

    private InputStream inputStream;

    public OSSObject(InputStream is) {
        this.inputStream = is;
    }
}
