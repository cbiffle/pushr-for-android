package org.mg8.pushr.droid.svc;

import java.io.IOException;
import java.io.InputStream;

/**
 * A thing that can be opened, yielding data.  Essentially a thunk
 * for getting an {@link InputStream} lazily.
 * 
 * @author Cliff L. Biffle
 */
public interface Openable {
  InputStream open() throws IOException;
}
