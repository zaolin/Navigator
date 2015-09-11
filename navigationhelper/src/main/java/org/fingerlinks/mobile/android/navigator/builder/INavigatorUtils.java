package org.fingerlinks.mobile.android.navigator.builder;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.fingerlinks.mobile.android.navigator.NavigatorException;

/**
 * Created by fabio on 11/09/15.
 */
public interface INavigatorUtils<U extends INavigatorUtils> {

    void confirmExitWithMessage(int message);

    void confirmExitWithMessage(String message);

    void confirmExitWithMessage(int message, long doublePressInterval);

    void confirmExitWithMessage(String message, long doublePressInterval);

    void goToPreviousBackStack() throws NavigatorException;

    void goBackToSpecificPoint(String tag) throws NavigatorException;

    boolean canGoBack(FragmentManager fragmentManager);

    boolean canGoBackToSpecificPoint(String tag, int container, FragmentManager fragmentManager);
};