Media Service
=============

Java class: com.endpoint.lg.media.service

Liquid Galaxy Interactive Spaces service to control MPlayer instances in response to scene messages


Configuration variables for LG-CMS activities
---------------------------------------------

media.service
    # Expects a defined input route to receive director messages
    lg.window.viewport.target               Viewport name, to filter incoming messages
    space.activity.mplayer.path             Where to find mplayer
    space.activity.mplayer.flags            Extra mplayer flags

Working config:
lg.window.viewport.target=42-a
space.activity.mplayer.flags=-really-quiet -msglevel global=5 -nograbpointer -noconsolecontrols -nomouseinput -osdlevel 0 -ontop -cache 8192
space.activity.mplayer.path=/usr/bin/mplayer


Copyright (C) 2015 Google Inc.
Copyright (C) 2015 End Point Corporation

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
