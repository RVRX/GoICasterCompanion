# GoIStreamToolRedux
<h1 id="application-overview">Overview</h1>
            <p>GoIStreamToolRedux (GoISTR) is a cross-platform application designed to assist <a href="https://gunsoficarus.com/" target="_blank">Guns of Icarus</a> tournament broadcasters with their interfaces and overlays. It includes UI management for teams, maps, timers, and tournament info that all ties into an output folder, providing OBS (or other casting tool) with static file paths to read from. The GoISTR project started as an adaptation of the original closed-course Windows application <a href="https://urzlab.com/goistreamtool/">GoiStreamTool</a> by GoIO player and caster Urz.</p>
            <p class="text-info">GoISTR is a free and open-source software. As such, community contributions to the application source or its supporting material is welcome, and feedback is appreciated to help grow the application.</p>

<h1 id="features">Features</h1>

<h3 id="team-management">Team Management</h3>
<p>Select up to four currently playing teams, each with thier own name, logo, and abbreviation.</p>
<img class="img-fluid" width="812" alt="image" src="https://user-images.githubusercontent.com/45409688/123657395-be577600-d7fe-11eb-95a4-39777cbddb40.png">

<h3 id="timers">Lobby Timer & Custom Timer</h3>
<p>Control both a lobby timer, and a (<em>coming soon</em>) fully custom timer.</p>
<img class="img-fluid" width="812" alt="image" src="https://user-images.githubusercontent.com/45409688/123658148-7553f180-d7ff-11eb-921b-7f8feea7432d.png">

<h3 id="map-management">Map Management</h3>
<p>Select between any of the Guns of Icarus maps. <em>Additional map meta-data (size, gamemode) coming in future release.</em></p>
<img class="img-fluid" width="812" alt="image" src="https://user-images.githubusercontent.com/45409688/123658468-bf3cd780-d7ff-11eb-874f-103d35e600bd.png">

<h3 id="tournament-info">Tournament Info</h3>
<p>Set the current tournament number. <em>More tournament features are planned for future.</em></p>
<img class="img-fluid" width="812" alt="image" src="https://user-images.githubusercontent.com/45409688/123658789-09be5400-d800-11eb-9591-3e6c2b1f5b0d.png">



<h1 id="usage">Usage</h1>
<h2 id="usage-overview">Overview</h2>
<p>
    The most important thing to know is that <kbd>Cmd+S</kbd> will save data on the current page. Upon editing a value, nothing is changed until the save keystroke is pressed.
This is to allow casters to queue up content before saving it.
</p>
<h2 id="input">Input</h2>
<p>
    The <code>input</code> folder is the directory that enables the application to do its job.
    It <b>contains all of the data on teams, maps, applications settings</b>, and the like.
    After initial setup, the only file an average user will modify would be the <code>teams.txt</code> and corresponding file in <code>team_logos</code>.
    <u>Any files marked with an (<span class="text-secondary">*</span>), are recommended to be left alone</u>.
</p>
<h4>map_images directory<span class="text-secondary">*</span></h4>
<p>
    The map images folder contains an <b>image of each map</b>. Each file is named specifically with the same name as found in the maps.txt, along with the file's extension.
    Any inconsistencies between the map image name and the name as found in <code>maps.txt</code> will result in undefined behaviour.
</p>
<h4>maps.txt<span class="text-secondary">*</span></h4>
<p>The maps text file <b>contains the exact name of each map</b>. See <code>map_images</code> for more details.</p>
<h4>team_logos directory</h4>
<p>The team logos folder contains an <b>image for each team</b>. Each file is <em>named specifically with the same name as found in the teams.txt</em> (the section before the "|" on each line). Any inconsistencies between a team's logo name and the name as found in teams.txt will result in no image being displayed for that team.</p>
<h4>teams.txt</h4>
<p>
    <b>Contains the name an abbreviation of each team</b>. formatted as:
    <pre>teamName1|ABBREVIATION1<br>teamName2|ABBREVIATION2</pre>
    Teams are allowed to have no image associated with them, but it is recommended. The image name must be <em>identical</em> to the team name in <code>teams.txt</code>.
</p>
<h2 id="output">Output</h2>
<p>The <code>output</code> folder is <b>where the application does its changes, and where the end-user points their streaming tool for source files</b>.</p>
<h4>Map.png</h4>
<p>Image of the current map.</p>
<h4>Map.txt</h4>
<p>Name of the current map.</p>
<h4>Team<var>X</var>.png</h4>
<p>Logo of the current team.</p>
<h4>Team<var>X</var>.txt</h4>
<p>Name of the current team.</p>
<h4>TeamShort<var>X</var>.txt</h4>
<p>Abbreviated name of the current team. <em>Often overlain on the top right team color ribbons to show which team is which.</em></p>
<h4>Timer.txt</h4>
<p>Current timer value in <code>MM:SS</code> format.</p>
<h4>TournamentNumber.txt</h4>
<p>The current tournament number. <em>e.g., to be appended to the end of "SCS: #", to get "SCS: #336".</em></p>

<h1 id="contributing">Contributing</h1>
<h3>Giving Feedback</h3>
<p>Reporting bugs, requesting features, and suggesting changes, can all be done through the <a href="https://github.com/RVRX/GoIStreamToolRedux/issues">GitHub Issues page</a> by creating a <a href="https://github.com/RVRX/GoIStreamToolRedux/issues/new">new issue</a> (<em>a GitHub account is required</em>).</p>
<h3>Contributing to source</h3>
<p>PRs are encouraged.</p>
