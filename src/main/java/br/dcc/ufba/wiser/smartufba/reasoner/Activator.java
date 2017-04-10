package br.dcc.ufba.wiser.smartufba.reasoner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    static BundleContext bc;


    public void start(BundleContext bc) throws Exception {
        System.out.println("Starting the bundle Rules Semantic for IoT");
    }

    public void stop(BundleContext bc) throws Exception {
        System.out.println("Stopping the bundle Rules Semantic for IoT");
    }

}

