# GoIStreamToolRedux
<p>GoIStreamToolRedux (GoISTR) is a cross-platform application designed to assist <a href="https://gunsoficarus.com/" target="_blank">Guns of Icarus</a> tournament broadcasters with their interfaces and overlays. It includes UI management for teams, maps, timers, and tournament info that all ties into an output folder, providing OBS (or other casting tool) with static file paths to read from. The GoISTR project started as an adaptation of the original closed-course Windows application <a href="https://urzlab.com/goistreamtool/">GoiStreamTool</a> by GoIO player and caster Urz.</p>
<p>GoISTR is a free and open-source software. As such, community contributions to the source or supporting material are welcome, and feedback is appreciated to help grow the application.</p>


<h1 id="features">Features</h1>

<h3 id="team-management">Team Management</h3>
<p>Select up to four currently playing teams, each with thier own name, logo, and abbreviation.</p>
<img width="812" alt="image" src="https://user-images.githubusercontent.com/45409688/123657395-be577600-d7fe-11eb-95a4-39777cbddb40.png">

<h3 id="timers">Lobby Timer & Custom Timer</h3>
<p>Control both a lobby timer, and a (<em>coming soon</em>) fully custom timer.</p>
<img width="812" alt="image" src="https://user-images.githubusercontent.com/45409688/123658148-7553f180-d7ff-11eb-921b-7f8feea7432d.png">

<h3 id="map-management">Map Management</h3>
<p>Select between any of the Guns of Icarus maps. <em>Additional map meta-data (size, gamemode) coming in future release.</em></p>
<img width="812" alt="image" src="https://user-images.githubusercontent.com/45409688/123658468-bf3cd780-d7ff-11eb-874f-103d35e600bd.png">

<h3 id="tournament-info">Tournament Info</h3>
<p>Set the current tournament number. <em>More tournament features are planned for future.</em></p>
<img width="812" alt="image" src="https://user-images.githubusercontent.com/45409688/123658789-09be5400-d800-11eb-9591-3e6c2b1f5b0d.png">


<h1 id="usage">Usage</h1>
<h3 id="usage-overview">Overview</h3>
<h3 id="input">Input</h3>
Any files marked with an (*), are reccomended to be left alone.
<h4>map_images directory</h4>
The map images folder contains an image of each map. Each file is named specifically with the same name as found in the maps.txt, along with the file's extension. Any inconsistencies between the map image name and the name as found in maps.txt will result in undefinded behaviour.
<h4>maps.txt</h4>
The maps text file contains the exact name of each map. See map_images for more details.
<h4>team_logos directory</h4>
The team logos folder contains an image for each team. Each file is named specifically with the same name as found in the teams.txt (the section before the "|" on each line). Any inconsistencies between a team's logo name and the name as found in teams.txt will result in no image being displayed for that team.
<h4>teams.txt</h4>
<h3 id="output">Output</h3>

<h1>Contributing</h1>
