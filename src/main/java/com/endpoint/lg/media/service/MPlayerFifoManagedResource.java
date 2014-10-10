/* vim: si ts=2 sw=2 et
*/
/*
 * Copyright (C) 2013-2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.endpoint.lg.media.service;

import interactivespaces.InteractiveSpacesException;
import interactivespaces.configuration.Configuration;
import interactivespaces.util.process.NativeCommandRunner;
import interactivespaces.util.resource.ManagedResource;

import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

import org.apache.commons.logging.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.ArrayDeque;

/**
 * Writes commands to a managed FIFO.
 * 
 * @author Wojciech Ziniewicz <wojtek@endpoint.com>
 * @author Matt Vollrath <matt@endpoint.com>
 * @author Josh Tolley <josh@endpoint.com>
 */
public class MPlayerFifoManagedResource implements ManagedResource {

  /**
   * Config key for FIFO base directory.
   */
  public static final String CONFIGURATION_NAME_MPLAYER_TMPDIR = "space.activity.mplayer.tmpdir";

  /**
   * Path for mplayer FIFO.
   */
  private File mplayerFifo;

  /**
   * Output stream for mplayer FIFO.
   */
  private OutputStream fifoOutputStream;

  /**
   * The print stream for the output stream.
   */
  private PrintStream printStream;

  /**
   * A string buffer for commands received before the PrintStream is ready.
   */
  private ArrayDeque<String> printBuffer;

  /**
   * Executor service to use.
   */
  private final ExecutorService executorService; 
  /**
   * Logger for the resource.
   */
  private final Log log;

  /**
   * Construct the resource.
   * 
   * @param config
   *          the configuration
   * @param log
   *          the logger to use
   */
  public MPlayerFifoManagedResource(String id, Configuration config,
      ExecutorService executorService, Log log, String tmpdir) {
    this.executorService = executorService;
    this.log = log;

    printBuffer = new ArrayDeque<String>();
//    if (config.getPropertyString(CONFIGURATION_NAME_MPLAYER_TMPDIR).equals(null)) {
//      log.error("Can't get property string " + CONFIGURATION_NAME_MPLAYER_TMPDIR);
//    }
    mplayerFifo =
        new File(String.format("%s/%s.fifo",
            tmpdir, id));
            // getActivityFilesystem().getTempDataDirectory().getAbsolutePath(),
            // config.getPropertyString(CONFIGURATION_NAME_MPLAYER_TMPDIR), id));
  }

  /**
   * Create the fifo using mkfifo
   */
  @Override
  public void startup() {
    if (!mplayerFifo.exists()) {
      NativeCommandRunner runner = new NativeCommandRunner();
      runner.execute(Lists.newArrayList("/usr/bin/mkfifo", mplayerFifo.getAbsolutePath()));
      if (!runner.isSuccess()) {
        throw new InteractiveSpacesException("Could not create FIFO for mplayer at " + mplayerFifo.getAbsolutePath());
      }
    }

    executorService.submit(new Runnable() {
      @Override
      public void run() {
        try {
          log.debug("Opening fifo file for write: " + mplayerFifo.getAbsolutePath());
          fifoOutputStream = new FileOutputStream(mplayerFifo);
          printStream = new PrintStream(fifoOutputStream);
          flush();
        } catch (FileNotFoundException e) {
          log.error("Could not open fifo file for writing");
        }
      }
    });
  }

  /**
   * Fetches the path to the FIFO.
   * 
   * @return FIFO path
   */
  public String getAbsolutePath() {
    return mplayerFifo.getAbsolutePath();
  }

  @Override
  public void shutdown() {
    Closeables.closeQuietly(printStream);
    mplayerFifo.delete();
  }

  /**
   * Writes to the FIFO if it is ready and commands are buffered.
   */
  private synchronized void flush() {
    if (printStream != null) {
      while (!printBuffer.isEmpty()) {
        String command = printBuffer.removeLast();

        printStream.println(command);
        log.info("Wrote to FIFO: " + command);
      }
    }
  }

  /**
   * Should take care of writing json messages to fifo file
   * <p>
   * Fifo message is a string e.h. "loadfile /tmp/test.avi"
   * <p>
   * http://www.mplayerhq.hu/DOCS/tech/slave.txt
   */
  public void writeToFifo(String command) {
    printBuffer.addFirst(command);

    flush();
  }
}
