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

import interactivespaces.util.resource.ManagedResource;
import interactivespaces.util.resource.ManagedResources;
import interactivespaces.activity.binary.NativeActivityRunner;
import interactivespaces.service.comm.network.server.UdpServerNetworkCommunicationEndpoint;
import interactivespaces.service.comm.network.client.UdpClientNetworkCommunicationEndpoint;
import interactivespaces.service.comm.network.client.UdpPacket;

import org.apache.commons.logging.Log;

/**
 * Contains all resources needed to support an mplayer instance.
 * 
 * @author Matt Vollrath <matt@endpoint.com>
 */
public class MplayerInstance extends ManagedResources implements ManagedResource {
  public interface MplayerInstanceListener {
    void onUdpPacket(String id, UdpPacket packet);
  }

  private String id;
  private Log log;
  private NativeActivityRunner runner;
  private MplayerFifoManagedResource fifo;
  private UdpServerNetworkCommunicationEndpoint udpServer;
  private UdpClientNetworkCommunicationEndpoint udpClient;

  /**
   * Creates an MplayerInstance with the given components.
   * 
   * @param id
   *          unique name for the instance
   * @param log
   *          activity logger
   * @param runner
   *          native mplayer process runner
   * @param fifo
   *          managed fifo instance
   * @param udpServer
   *          udp server endpoint
   * @param udpClient
   *          udp client endpoint
   */
  public MplayerInstance(String id, Log log, NativeActivityRunner runner,
      MplayerFifoManagedResource fifo, UdpServerNetworkCommunicationEndpoint udpServer,
      UdpClientNetworkCommunicationEndpoint udpClient) {

    super(log);

    this.id = id;
    this.log = log;
    this.runner = runner;
    this.fifo = fifo;
    this.udpServer = udpServer;
    this.udpClient = udpClient;

    addResource(this.fifo);
    addResource(this.udpServer);
    addResource(this.udpClient);
    addResource(this.runner);
  }

  public void shutdown() {
    log.debug("Shutting down MplayerInstance " + id);
    shutdownResources();
  }

  public void startup() {
    log.debug("Starting up MplayerInstance " + id);
    startupResources();
  }

  /**
   * Send a command to the mplayer instance's FIFO.
   * 
   * @param command
   *          see http://www.mplayerhq.hu/DOCS/tech/slave.txt
   */
  public void sendCommand(String command) {
    fifo.writeToFifo(command);
  }
}
