package com.ericsson.etelcrm.bil.kernel;


public interface IServices {

    <T> T getService(Class<T> srvInterface);

    BundleInfo getBundleInfo();

}
