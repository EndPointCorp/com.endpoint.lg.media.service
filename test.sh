#!/bin/sh

timeout 3s wssh ws://localhost:9000/websocket 2>/dev/null <<MSG
{"name":"Afghan Hound","resource_uri":"/director_api/scene/afghan-hound/","slug":"afghan-hound","windows":[{"activity":"video","assets":["/home/josh/aogg.avi"],"height":768,"presentation_viewport":"42-a","width":1024,"x_coord":28,"y_coord":28},{"activity":"earth","assets":["http://lg-cms/media/assets/afghanistan_placemarks.kmz"],"height":1920,"presentation_viewport":"42-a","width":1080,"x_coord":0,"y_coord":0},{"activity":"earth","assets":["http://lg-cms/media/assets/afghanistan_placemarks.kmz","http://lg-cms/media/assets/afghanistan_tour.kml"],"height":1920,"presentation_viewport":"42-a","width":1080,"x_coord":1080,"y_coord":0},{"activity":"earth","assets":["http://lg-cms/media/assets/afghanistan_placemarks.kmz"],"height":1920,"presentation_viewport":"42-a","width":1080,"x_coord":2160,"y_coord":0},{"activity":"browser","assets":["http://lg-cms/media/assets/afghanistan_info.html"],"height":1920,"presentation_viewport":"42-b","width":1080,"x_coord":0,"y_coord":0}]}
MSG

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
