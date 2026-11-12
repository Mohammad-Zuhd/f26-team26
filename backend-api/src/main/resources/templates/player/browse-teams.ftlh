<#-- browse teams page, built from browse-teams.html in our prototype -->
<#import "layout.ftlh" as l>
<@l.page title="Browse Teams" active="teams">

  <h1 class="fw-bold navy">Teams Looking for Players</h1>
  <p class="text-muted mb-4">Only teams with open roster spots show up here. Use the filters to narrow it down.</p>

  <#-- method="get" puts the filters in the URL so the controller can read them -->
  <form method="get" action="/player/teams" class="card border-0 shadow-sm rounded-4 mb-4">
    <div class="card-body p-4 row g-3 align-items-end">
      <div class="col-md-3">
        <label for="sport" class="form-label">Sport</label>
        <select id="sport" name="sport" class="form-select">
          <option value="">All sports</option>
          <#list sports as s>
            <option value="${s}" <#if sport == s>selected</#if>>${s}</option>
          </#list>
        </select>
      </div>
      <div class="col-md-3">
        <label for="skillLevel" class="form-label">Skill level</label>
        <select id="skillLevel" name="skillLevel" class="form-select">
          <option value="">Any level</option>
          <#list skillLevels as level>
            <option value="${level}" <#if skillLevel == level>selected</#if>>${level}</option>
          </#list>
        </select>
      </div>
      <div class="col-md-2">
        <label for="location" class="form-label">Location</label>
        <input id="location" name="location" type="text" class="form-control" placeholder="Greensboro" value="${location}">
      </div>
      <div class="col-md-2">
        <label for="schedule" class="form-label">Schedule</label>
        <input id="schedule" name="schedule" type="text" class="form-control" placeholder="Sunday" value="${schedule}">
      </div>
      <div class="col-md-2 d-flex gap-2">
        <button type="submit" class="btn bg-navy text-white">Search</button>
        <#if sport?has_content || skillLevel?has_content || location?has_content || schedule?has_content>
          <a href="/player/teams" class="btn btn-outline-secondary">Clear</a>
        </#if>
      </div>
    </div>
  </form>

  <p class="text-muted">${teams?size} team(s) found</p>

  <div class="row g-4">
    <#-- loops over the real teams from the database instead of the hardcoded cards -->
    <#list teams as team>
      <div class="col-md-4">
        <div class="card h-100 border-0 shadow-sm rounded-4">
          <div class="card-body p-4 d-flex flex-column">
            <span class="badge bg-orange align-self-start mb-3">${team.sport()?upper_case}</span>
            <h2 class="h4 fw-bold navy">${team.teamName()}</h2>
            <p class="text-muted mb-2">${team.sport()} | ${team.skillLevel()}</p>
            <p class="mb-1"><strong>Location:</strong> ${team.location()}</p>
            <p class="mb-1"><strong>Schedule:</strong> ${team.schedule()}</p>
            <p class="mb-3"><strong>Available Spots:</strong> ${team.availableSpots()}</p>
            <#-- ?c so the id prints like 1234 and not 1,234 -->
            <a href="/player/teams/${team.id()?c}" class="btn bg-navy text-white mt-auto">View Team</a>
          </div>
        </div>
      </div>
    <#else>
      <div class="col-12">
        <div class="alert alert-info mb-0">No teams match your filters right now. Try clearing a filter.</div>
      </div>
    </#list>
  </div>

</@l.page>