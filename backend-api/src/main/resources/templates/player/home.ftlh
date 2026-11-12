<#-- Shared RecLeague home page (GET /). Links to both sides of the app: Players and Team Captains. -->
<#import "layout.ftlh" as l>
<@l.page title="Home" active="home">

  <section class="p-5 rounded-4 text-white bg-navy mb-5">
    <div class="row align-items-center g-4">
      <div class="col-lg-6">
        <span class="badge bg-orange mb-3">RECLEAGUE</span>
        <h1 class="display-5 fw-bold mb-3">Find your team. Fill your roster.</h1>
        <p class="fs-5">Players find local rec teams with open spots. Team Captains manage their teams and fill open spots.</p>
      </div>
      <div class="col-lg-6">
        <img src="/images/sports-banner.jpg" alt="Recreational sports players"
             class="img-fluid rounded-4 shadow" style="width: 100%; height: 260px; object-fit: cover;">
      </div>
    </div>
  </section>

  <div class="row g-4">
    <#-- Player side (my stories) -->
    <div class="col-md-6">
      <div class="card h-100 border-0 shadow-sm rounded-4">
        <div class="card-body p-4">
          <span class="badge bg-orange mb-3">PLAYER</span>
          <h2 class="h4 fw-bold navy">I'm a Player</h2>
          <p class="text-muted">Make a profile and browse teams with open roster spots.</p>
          <div class="d-flex flex-wrap gap-2">
            <#if playerLoggedIn!false>
              <a href="/player/profile" class="btn bg-navy text-white">My Profile</a>
            <#else>
              <a href="/player/signup" class="btn bg-orange text-white">Create a Profile</a>
              <a href="/player/login" class="btn btn-outline-secondary">Find My Profile</a>
            </#if>
            <a href="/player/teams" class="btn bg-navy text-white">Browse Teams</a>
          </div>
        </div>
      </div>
    </div>

    <#-- Team Captain side (Mohammad's stories). His pages live under /captains/{captainId}/... -->
    <div class="col-md-6">
      <div class="card h-100 border-0 shadow-sm rounded-4">
        <div class="card-body p-4">
          <span class="badge bg-navy mb-3">TEAM CAPTAIN</span>
          <h2 class="h4 fw-bold navy">I'm a Team Captain</h2>
          <p class="text-muted">Manage your teams, post open spots, and review your roster.</p>
          <#-- method="get" sends /captain?captainId=1, and HomeUiController redirects to /captains/1/teams -->
          <form method="get" action="/captain" class="d-flex flex-wrap gap-2">
            <input type="number" name="captainId" min="1" placeholder="Captain ID" required
                   class="form-control" style="max-width: 160px;">
            <button type="submit" class="btn bg-navy text-white">Go to My Teams</button>
          </form>
          <p class="text-muted small mt-2 mb-0">Prototype: captains enter their ID until login is added.</p>
        </div>
      </div>
    </div>
  </div>

</@l.page>
