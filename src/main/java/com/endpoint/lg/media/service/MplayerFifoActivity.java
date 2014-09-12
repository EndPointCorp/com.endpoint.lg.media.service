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

import interactivespaces.activity.impl.ros.BaseRoutableRosActivity;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Listens for media commands and creates mplayer instances accordingly.
 * 
 * @author Wojciech Ziniewicz <wojtek@endpoint.com>
 * @author Matt Vollrath <matt@endpoint.com>
 */
public class MplayerFifoActivity extends BaseRoutableRosActivity {
  private MplayerInstanceFactory factory;
  private Map<String, MplayerInstance> instances;

  @Override
  public void onActivitySetup() {
    factory = new MplayerInstanceFactory(getLog(), getConfiguration(), getController());
    instances = Maps.newHashMap();
  }
  
  /**
   * Fetches an mplayer instance, creating one if it did not already exist.
   * 
   * @param id
   *          unique id of the mplayer instance
   */
  private MplayerInstance getInstance(String id) {
	// TODO: doesnt spawn - why?
    MplayerInstance instance = instances.get(id);

    if (instance == null) {
      instance = factory.newInstance(id);
      instances.put(id, instance);
      addManagedResource(instance);
      instance.startup();
      getLog().debug("Made new mplayer instance at " + id);
    }

    return instance;
  }

  /**
   * Sends a command to a mplayer instance.
   * 
   * @param id
   *          unique id of the mplayer instance
   * @param command
   *          to be sent to the FIFO
   */
  private void commandInstance(String id, String command) {
    MplayerInstance instance = getInstance(id);

    instance.sendCommand(command);
    getLog().debug(String.format("Sent %s to %s", command, id));
  }

  /**
   * Shuts down and destroys an mplayer instance.
   * 
   * @param id
   *          unique id of the mplayer instance
   */
  private void killInstance(String id) {
    MplayerInstance instance = instances.get(id);

    if (instance != null) {
      instance.shutdown();
      instances.put(id, null);
    }
  }

  @Override
  public void onActivityActivate() {
    getLog().debug("Activating media service");
//    // for testing
//    commandInstance("foo", String.format("loadfile %s 0",
//        getActivityFilesystem().getInstallFile("test.avi").getAbsolutePath()));
//    commandInstance("bar", String.format("loadfile %s 0",
//        getActivityFilesystem().getInstallFile("test.avi").getAbsolutePath()));
//    commandInstance("baz", String.format("loadfile %s 0",
//        getActivityFilesystem().getInstallFile("test.avi").getAbsolutePath()));
//
//    commandInstance("foo", "set_property speed 0.8");
//    commandInstance("bar", "set_property speed 0.9");
//    commandInstance("baz", "set_property brightness -25");
  }

  @Override
  public void onActivityDeactivate() {
    getLog().debug("Deactivating media service");
//    // for testing
//    killInstance("foo");
//    killInstance("bar");
//    killInstance("baz");
  }

  @Override
  public void onNewInputJson(String channelName, Map<String, Object> m) {
    if (!isActivated())
      return;
    getLog().debug("Got message on input channel " + channelName);
    getLog().debug(m);
  }
}
