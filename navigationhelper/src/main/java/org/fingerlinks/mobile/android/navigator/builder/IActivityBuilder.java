package org.fingerlinks.mobile.android.navigator.builder;

/**
 * Created by fabio on 07/09/15.
 */
public interface IActivityBuilder<A extends IActivityBuilder> {

    A addRequestCode(int request_code);

    /**
     * replacement method Builders.Any.N.commit () only to avoid duplication of the method call
     */
    void commit();
};