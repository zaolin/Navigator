package org.fingerlinks.mobile.android.navigator;

/**
 * Created by fabio on 06/09/15.
 */
public class NavigatorException extends Throwable {

    public NavigatorException() {
    }

    public NavigatorException(String detailMessage) {
        super(detailMessage);
    }

    public NavigatorException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    public NavigatorException(Throwable cause) {
        super(cause);
    }
};