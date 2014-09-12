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

import interactivespaces.controller.SpaceController;
import interactivespaces.system.InteractiveSpacesEnvironment;
import interactivespaces.activity.binary.NativeActivityRunner;
import interactivespaces.activity.binary.NativeActivityRunnerFactory;
import interactivespaces.configuration.Configuration;
import interactivespaces.service.ServiceRegistry;
import interactivespaces.service.comm.network.server.UdpServerNetworkCommunicationEndpointService;
import interactivespaces.service.comm.network.client.UdpClientNetworkCommunicationEndpointService;
import interactivespaces.service.comm.network.server.UdpServerNetworkCommunicationEndpoint;
import interactivespaces.service.comm.network.client.UdpClientNetworkCommunicationEndpoint;

import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.commons.logging.Log;

/**
 * A factory for creating manageable mplayer instances.
 * 
 * @author Matt Vollrath <matt@endpoint.com>
 */
public class MplayerInstanceFactory {
  // TODO: flimsy port selection
  private static final int MIN_PORT = 10042;
  private static final int MAX_PORT = 10142;
  private int currentPort;

  private Log log;
  private Configuration config;
  private InteractiveSpacesEnvironment environment;

  private NativeActivityRunnerFactory runnerFactory;
//  private UdpServerNetworkCommunicationEndpointService udpServerService;
//  private UdpClientNetworkCommunicationEndpointService udpClientService;

  /**
   * Creates a factory from the given activity resources.
   * 
   * @param log
   *          activity logger
   * @param config
   *          activity configuration
   * @param controller
   *          activity controller
   */
  public MplayerInstanceFactory(Log log, Configuration config, SpaceController controller) {
    this.log = log;
    this.config = config;

    runnerFactory = controller.getNativeActivityRunnerFactory();
    environment = controller.getSpaceEnvironment();

    ServiceRegistry registry = environment.getServiceRegistry();
    //udpServerService = registry.getService(UdpServerNetworkCommunicationEndpointService.NAME);
    //udpClientService = registry.getService(UdpClientNetworkCommunicationEndpointService.NAME);

    currentPort = MIN_PORT;
  }

  /**
   * Creates a new mplayer instance with the given id.
   * 
   * @param id
   *          unique id for the instance
   */
  public MplayerInstance newInstance(String id) {
    MplayerFifoManagedResource fifo = createFifo(id);
    NativeActivityRunner runner = createRunner(fifo.getAbsolutePath());
    //UdpServerNetworkCommunicationEndpoint udpServer = createUdpServer(currentPort++);
    //UdpClientNetworkCommunicationEndpoint udpClient = createUdpClient();

    if (currentPort >= MAX_PORT)
      currentPort = MIN_PORT;

    return new MplayerInstance(id, log, runner, fifo); //, udpServer, udpClient);
  }

  /**
   * Creates a NativeActivityRunner for an MplayerInstance.
   * 
   * @param fifoPath
   *          path to the control FIFO
   * @return an mplayer runner
   */
  private NativeActivityRunner createRunner(String fifoPath) {
    NativeActivityRunner runner = runnerFactory.newPlatformNativeActivityRunner(log);

    Map<String, Object> runnerConfig = Maps.newHashMap();

    runnerConfig.put(NativeActivityRunner.ACTIVITYNAME, "/usr/bin/mplayer");
    runnerConfig
        .put(
            NativeActivityRunner.FLAGS,
            "-really-quiet -msglevel global=5 -nocache -osdlevel 0 -noborder -nolirc -nomouseinput -nograbpointer -noconsolecontrols -idle -slave -input file="
                + fifoPath);

    runner.configure(runnerConfig);

    return runner;
  }

  /**
   * Creates a UDP server for an MplayerInstance.
   * 
   * @param port
   *          port number to listen on
   * @return UDP server endpoint
   */
//  private UdpServerNetworkCommunicationEndpoint createUdpServer(int port) {
//    UdpServerNetworkCommunicationEndpoint udpServer = udpServerService.newServer(port, log);
//
//    return udpServer;
//  }

  /**
   * Creates a UDP client for an MplayerInstance.
   * 
   * @return UDP client endpoint
   */
//  private UdpClientNetworkCommunicationEndpoint createUdpClient() {
//    UdpClientNetworkCommunicationEndpoint udpClient = udpClientService.newClient(log);
//
//    return udpClient;
//  }

  /**
   * Creates a managed FIFO interface for an MplayerInstance.
   * 
   * @param id
   *          the unique id of the MplayerInstance
   * @return FIFO resource
   */
  private MplayerFifoManagedResource createFifo(String id) {
    MplayerFifoManagedResource fifo =
        new MplayerFifoManagedResource(id, config, environment.getExecutorService(), log);

    return fifo;
  }
}
