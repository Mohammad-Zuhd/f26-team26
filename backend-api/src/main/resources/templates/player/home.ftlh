<#-- Home page (GET /). It's the starting point for both of my user stories. -->
<#import "layout.ftlh" as l>
<@l.page title="Home" active="home">

  <section class="p-5 rounded-4 text-white bg-navy mb-5">
    <div class="row align-items-center g-4">
      <div class="col-lg-6">
        <span class="badge bg-orange mb-3">PLAYER</span>
        <h1 class="display-5 fw-bold mb-3">Find your team.</h1>
        <p class="fs-5">Make a player profile, browse local rec teams with open roster spots, and get in the game.</p>
      </div>
      <div class="col-lg-6">
        <img src="/images/sports-banner.jpg" alt="Recreational sports players"
             class="img-fluid rounded-4 shadow" style="width: 100%; height: 260px; object-fit: cover;">
      </div>
    </div>
  </section>

  <div class="row g-4">
    <#-- Story 1: Create / modify profile -->
    <div class="col-md-6">
      <div class="card h-100 border-0 shadow-sm rounded-4">
        <div class="card-body p-4">
          <h2 class="h4 fw-bold navy">My Player Profile</h2>
          <p class="text-muted">Tell teams your sports, skill level, location, and when you can play.</p>
          <#if playerLoggedIn!false>
            <a href="/player/profile" class="btn bg-navy text-white">View My Profile</a>
          <#else>
            <a href="/player/signup" class="btn bg-orange text-white me-2">Create a Profile</a>
            <a href="/player/login" class="btn btn-outline-secondary">I Already Have One</a>
          </#if>
        </div>
      </div>
    </div>

    <#-- Story 2: View available services (teams) -->
    <div class="col-md-6">
      <div class="card h-100 border-0 shadow-sm rounded-4">
        <div class="card-body p-4">
          <h2 class="h4 fw-bold navy">Teams Looking for Players</h2>
          <p class="text-muted">Search by sport, skill level, location, and schedule.</p>
          <a href="/player/teams" class="btn bg-navy text-white">Browse Teams</a>
        </div>
      </div>
    </div>
  </div>

</@l.page>