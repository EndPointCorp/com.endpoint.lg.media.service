package com.endpoint.lg.media.service;

import com.endpoint.lg.support.interactivespaces.ConfigurationHelper;
import com.endpoint.lg.support.message.Window;
import com.endpoint.lg.support.window.ManagedWindow;
import com.endpoint.lg.support.window.WindowGeometry;
import com.endpoint.lg.support.window.WindowIdentity;
import com.endpoint.lg.support.window.WindowInstanceIdentity;
import com.google.common.collect.Maps;
import interactivespaces.activity.binary.NativeActivityRunner;
import interactivespaces.util.process.NativeApplicationRunner;
import interactivespaces.util.process.StandardNativeApplicationRunnerCollection;
import interactivespaces.activity.impl.BaseActivity;
import interactivespaces.configuration.Configuration;
import interactivespaces.activity.ActivityRuntime;
import interactivespaces.util.resource.ManagedResource;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.logging.Log;

/**
 * Handles one running MPlayer instance
 * 
 * @author Wojciech Ziniewicz <wojtek@endpoint.com>
 * @author Matt Vollrath <matt@endpoint.com>
 * @author Josh Tolley <josh@endpoint.com>
 */
public class MPlayerInstance implements ManagedResource {
    private Log log;
    private Window window;
    private NativeApplicationRunner runner;
    private StandardNativeApplicationRunnerCollection runnerCollection;
    private MPlayerFifoManagedResource fifo;
    private String windowInstanceString;
    private WindowInstanceIdentity windowId;
    private ManagedWindow managedWindow;

    private Log getLog() {
        return log;
    }

    public MPlayerInstance(BaseActivity act, ActivityRuntime _activityRuntime, Configuration _config, Log _log, Window _window, String tmpdir)
    {
        log = _log;
        window = _window;

        fifo = new MPlayerFifoManagedResource(
            UUID.randomUUID().toString().replace("-", ""), _config,
            _activityRuntime.getSpaceEnvironment().getExecutorService(), _log, tmpdir
        );

        windowInstanceString = UUID.randomUUID().toString().replace("-", "");

        Map<String, Object> runnerConfig = Maps.newHashMap();
        runnerConfig.put(
            NativeActivityRunner.EXECUTABLE_PATHNAME,
            _config.getRequiredPropertyString("space.activity.mplayer.path")
        );
        runnerConfig.put(
            NativeActivityRunner.EXECUTABLE_FLAGS,
                ConfigurationHelper.getConfigurationConcat(_config, "space.activity.mplayer.flags", " ") +
                " -name " + windowInstanceString + " -idle -input file=\"" + fifo.getAbsolutePath() + "\"" +
                getGeometryFlags(window)
        );
        getLog().debug("Mplayer flags: " + runnerConfig.get(NativeActivityRunner.EXECUTABLE_FLAGS));

        windowId = new WindowInstanceIdentity(windowInstanceString);
        managedWindow = new ManagedWindow(act, windowId, getWindowGeometry(window));
        managedWindow.setViewportName(_window.presentation_viewport);
        managedWindow.resize(window.width, window.height);

        runnerCollection = new StandardNativeApplicationRunnerCollection(act.getSpaceEnvironment(), _log);
        act.addManagedResource(runnerCollection);
        runner = runnerCollection.newNativeApplicationRunner();
        runner.configure(runnerConfig);
        runnerCollection.addNativeApplicationRunner(runner);
    }

    private WindowGeometry getWindowGeometry(Window w) {
        return new WindowGeometry(w.width, w.height, w.x_coord, w.y_coord);
    }

    private String getGeometryFlags(Window w) {
        return " -geometry " + w.width + "x" + w.height; // + "+" + w.x_coord + "+" + w.y_coord;
    }
    
    public void sendCommand(String command) {
        getLog().debug("Sending command: '" + command + "'");
        fifo.writeToFifo(command);
    }

    public void play(String url) {
        sendCommand("loadfile \"" + url + "\" 0");
    }

    public void setSpeed(double speed) {
        sendCommand("set_property speed " + speed);
    }

    public void setBrightness(double brightness) {
        sendCommand("set_property brightness " + brightness);
    }

    public Window getWindow() {
        return window;
    }

    public void startup() {
        fifo.startup();
        runner.startup();
        managedWindow.setVisible(true);
        managedWindow.raise();
        managedWindow.update();
    }

    public void shutdown() {
        fifo.writeToFifo("quit");
        runner.shutdown();
        fifo.shutdown();
        managedWindow.shutdown();
    }
}
