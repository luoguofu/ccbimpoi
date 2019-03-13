package com.weqia.utils.http.okserver.download;

import java.io.IOException;

/**
 * Created by berwin on 2017/11/3.
 */

public class DownErrException extends IOException {

    public DownErrException() {
    }

    public DownErrException(String message) {
        super(message);
    }

    public DownErrException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownErrException(Throwable cause) {
        super(cause);
    }
}
