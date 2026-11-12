<#-- view team page, built from team-detail.html in our prototype -->
<#import "layout.ftlh" as l>
<@l.page title=team.teamName() active="teams">

  <a href="/player/teams" class="text-decoration-none navy">&larr; Back to teams</a>

  <div class="card border-0 shadow-sm rounded-4 mt-3">
    <div class="card-body p-4">
      <span class="badge bg-orange mb-3">${team.sport()?upper_case}</span>
      <h1 class="fw-bold navy">${team.teamName()}</h1>
      <#-- description is optional so only show it if there is one -->
      <#if team.description()?has_content>
        <p class="text-muted">${team.description()}</p>
      </#if>

      <table class="table mt-3">
        <tr><th>Sport</th><td>${team.sport()}</td></tr>
        <tr><th>Skill Level</th><td>${team.skillLevel()}</td></tr>
        <tr><th>Captain</th><td>${team.captainName()}</td></tr>
        <tr><th>Schedule</th><td>${team.schedule()}</td></tr>
        <tr><th>Location</th><td>${team.location()}</td></tr>
        <tr><th>Available Spots</th><td>${team.availableSpots()}</td></tr>
      </table>

      <h2 class="h5 fw-bold navy mt-4">Open Spots</h2>
      <table class="table align-middle">
        <tr><th>Position</th><th>Spots</th><th>Game Date</th><th>Time</th><th>Where</th></tr>
        <#list openSpots as spot>
          <tr>
            <td>${spot.spotType}</td>
            <td>${spot.availableSpots}</td>
            <td>${spot.gameDate}</td>
            <td>${spot.gameTime}</td>
            <td>${spot.location}</td>
          </tr>
        <#else>
          <tr><td colspan="5" class="text-muted">This team has no open spots right now.</td></tr>
        </#list>
      </table>

      <#-- request to join is story 3, saving it for the final presentation -->
      <p class="text-muted small mb-0">Joining a team is coming in the next increment.</p>
    </div>
  </div>

</@l.page>