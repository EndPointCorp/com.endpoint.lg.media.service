package com.endpoint.lg.media.service;

import com.endpoint.lg.media.service.MPlayerInstance;
import com.endpoint.lg.support.message.Scene;
import com.endpoint.lg.support.message.Window;
import com.google.common.collect.Maps;
import interactivespaces.activity.impl.ros.BaseRoutableRosActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Media player controller activity
 *
 * @author Josh Tolley <josh@endpoint.com>
 */
public class MediaServiceActivity extends BaseRoutableRosActivity {
    private List<MPlayerInstance> instances;

    @Override
    public void onActivitySetup() {
        getLog().info("Activity com.endpoint.lg.media.service setup");
        instances = new ArrayList<MPlayerInstance>();
    }

    @Override
    public void onActivityStartup() {
        getLog().info("Activity com.endpoint.lg.media.service startup");
    }

    @Override
    public void onActivityPostStartup() {
        getLog().info("Activity com.endpoint.lg.media.service post startup");
    }

    @Override
    public void onActivityActivate() {
        getLog().info("Activity com.endpoint.lg.media.service activate");
    }

    @Override
    public void onActivityDeactivate() {
        getLog().info("Activity com.endpoint.lg.media.service deactivate");
    }

    @Override
    public void onActivityPreShutdown() {
        getLog().info("Activity com.endpoint.lg.media.service pre shutdown");
    }

    @Override
    public void onActivityShutdown() {
        getLog().info("Activity com.endpoint.lg.media.service shutdown");
        killAllInstances();
    }

    @Override
    public void onActivityCleanup() {
        getLog().info("Activity com.endpoint.lg.media.service cleanup");
    }

    public void onNewInputJson(String channelName, Map<String, Object> message) {
        Scene s;

        getLog().debug("Received message: " + message.toString());
        try {
            s = Scene.fromJson(jsonStringify(message));
            getLog().debug("Received JSON message: " + s.toString());

            /* Only kill all previous mplayers *after* we've determined we can
             * parse this new scene */
            killAllInstances();

            for (Window w : s.windows) {
                if ((w.activity.equals("video") || w.activity.equals("audio")) &&
                        Arrays.asList(
                            getConfiguration().getRequiredPropertyString("lg.window.viewport.target").split(",")
                        ).indexOf(w.presentation_viewport) != -1
                        ) {
                    handleMediaCommand(w);
                }
            }
        }
        catch (IOException e) {
            getLog().error("Somewhere we failed to understand this message", e);
        }
    }

    /**
     * Handles a single window definition, in a scene. Filtering to make sure
     * this window description belongs to this activity should have been done
     * previously.
     */
    private void handleMediaCommand(Window window) {
        /* Create new mplayer instance, and run it */
        MPlayerInstance m = new MPlayerInstance(this, getController(), getConfiguration(), getLog(), window, getActivityFilesystem().getTempDataDirectory().getAbsolutePath());
        instances.add(m);
        m.startup();

        // For now, we only support one asset per window anyway; anything else
        // should get an exception from the Window parsing code
        m.play(window.assets[0]);
    }

    /**
     * Kill all MPlayer instances, and clean up the instance list
     */
    private void killAllInstances() {
        for (MPlayerInstance m : instances) {
            if (m != null)
                m.shutdown();
        }
        instances.clear();
    }
}
