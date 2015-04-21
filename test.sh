#!/bin/sh

# Copyright (C) 2015 Google Inc.
# Copyright (C) 2015 End Point Corporation
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not
# use this file except in compliance with the License. You may obtain a copy of
# the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations under
# the License.

timeout 3s wssh ws://localhost:9000/websocket 2>/dev/null <<MSG
{"name":"Afghan Hound","resource_uri":"/director_api/scene/afghan-hound/","slug":"afghan-hound","windows":[{"activity":"browser","assets": ["http://imdb.com"],"height":1920,"presentation_viewport":"fred","width":1080,"x_coord":0,"y_coord":0},{"activity":"browser","assets": ["http://www.endpoint.com"],"height":1920,"presentation_viewport":"fred","width":1080,"x_coord":0,"y_coord":0},{"activity":"browser","assets": ["http://www.endpoint.com"],"height":1920,"presentation_viewport":"ethel","width":1080,"x_coord":0,"y_coord":0}]}
MSG

#{"name":"Afghan Hound","resource_uri":"/director_api/scene/afghan-hound/","slug":"afghan-hound","windows":[{"activity":"video","assets":["/home/josh/aogg.avi"],"height":768,"presentation_viewport":"42-a","width":1024,"x_coord":28,"y_coord":28},{"activity":"earth","assets":["http://lg-cms/media/assets/afghanistan_placemarks.kmz"],"height":1920,"presentation_viewport":"42-a","width":1080,"x_coord":0,"y_coord":0},{"activity":"earth","assets":["http://lg-cms/media/assets/afghanistan_placemarks.kmz","http://lg-cms/media/assets/afghanistan_tour.kml"],"height":1920,"presentation_viewport":"42-a","width":1080,"x_coord":1080,"y_coord":0},{"activity":"earth","assets":["http://lg-cms/media/assets/afghanistan_placemarks.kmz"],"height":1920,"presentation_viewport":"42-a","width":1080,"x_coord":2160,"y_coord":0},{"activity":"browser","assets":["http://lg-cms/media/assets/afghanistan_info.html"],"height":1920,"presentation_viewport":"42-b","width":1080,"x_coord":0,"y_coord":0}]}
# {
#     "name":"Afghan Hound",
#     "resource_uri":"/director_api/scene/afghan-hound/",
#     "slug":"afghan-hound",
#     "windows":
#         [
#             {
#                 "activity":"video",
#                 "assets": [
#                     "http://lg-cms/media/assets/afghan_hound_video_2014-08-20.mp4"
#                 ],
#                 "height":768,
#                 "presentation_viewport":"42-a",
#                 "width":1024,
#                 "x_coord":28,
#                 "y_coord":28
#             },
#             {
#                 "activity":"earth",
#                 "assets": [
#                     "http://lg-cms/media/assets/afghanistan_placemarks.kmz"
#                 ],
#                 "height":1920,
#                 "presentation_viewport":"42-a",
#                 "width":1080,
#                 "x_coord":0,
#                 "y_coord":0
#             },
#             {
#                 "activity":"earth",
#                 "assets": [
#                     "http://lg-cms/media/assets/afghanistan_placemarks.kmz",
#                     "http://lg-cms/media/assets/afghanistan_tour.kml"
#                 ],
#                 "height":1920,
#                 "presentation_viewport":"42-a",
#                 "width":1080,
#                 "x_coord":1080,
#                 "y_coord":0
#             },
#             {
#                 "activity":"earth",
#                 "assets": [
#                     "http://lg-cms/media/assets/afghanistan_placemarks.kmz"
#                 ],
#                 "height":1920,
#                 "presentation_viewport":"42-a",
#                 "width":1080,
#                 "x_coord":2160,
#                 "y_coord":0
#             },
#             {
#                 "activity":"browser",
#                 "assets": [
#                     "http://lg-cms/media/assets/afghanistan_info.html"
#                 ],
#                 "height":1920,
#                 "presentation_viewport":"42-b",
#                 "width":1080,
#                 "x_coord":0,
#                 "y_coord":0
#             }
#         ]
#     }
