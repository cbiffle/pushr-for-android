package org.mg8.pushr.droid;

import junit.framework.Test;
import junit.framework.TestSuite;

import android.test.suitebuilder.TestSuiteBuilder;

/**
 * Tests for the Pushr Android app.
 *
 * To run this suite from the command line:
 * $ adb shell am instrument -e class org.mg8.pushr.droid.AllTests \
 *   -w org.mg8.pushr.droid.tests/android.test.InstrumentationTestRunner
 */
public class AllTests extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
                .includeAllPackagesUnderHere()
                .build();
    }
}
