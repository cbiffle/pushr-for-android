package org.mg8.pushr.flickr;

import junit.framework.Test;
import junit.framework.TestSuite;

import android.test.suitebuilder.TestSuiteBuilder;

/**
 * Tests for the Flickr interface code.
 *
 * To run this suite from the command line:
 * $ adb shell am instrument -e class org.mg8.pushr.flickr.AllTests \
 *   -w org.mg8.pushr.droid.tests/android.test.InstrumentationTestRunner
 */
public class AllTests extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
                .includeAllPackagesUnderHere()
                .build();
    }
}
