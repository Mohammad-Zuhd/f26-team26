<#--
  "My Profile" page (GET /player/profile). Built from my-profile.html in our prototype.
  Every value here comes from the "player" object the controller loaded from Neon.
  .ftlh files auto-escape HTML, so if someone types <script> as their name it just shows as text.
-->
<#import "layout.ftlh" as l>
<@l.page title="My Profile" active="profile">

  <section class="p-5 rounded-4 text-white bg-navy mb-5">
    <span class="badge bg-orange mb-3">PLAYER</span>
    <h1 class="display-5 fw-bold mb-2">${player.name}</h1>
    <p class="fs-5 mb-0">${player.preferredSports} | ${player.skillLevel}</p>
  </section>

  <h2 class="fw-bold navy">My Profile</h2>
  <p class="text-muted mb-4">Your player information.</p>

  <div class="card border-0 shadow-sm rounded-4">
    <div class="card-body p-4">
      <table class="table">
        <tr><th>Name</th><td>${player.name}</td></tr>
        <tr><th>Email</th><td>${player.email}</td></tr>
        <tr><th>Location</th><td>${player.location}</td></tr>
        <tr><th>Preferred Sports</th><td>${player.preferredSports}</td></tr>
        <tr><th>Skill Level</th><td>${player.skillLevel}</td></tr>
        <#-- availability is optional, so if it's empty (null), it show "Not set" instead of crashing -->
        <tr><th>Availability</th><td>${player.availability!'Not set'}</td></tr>
      </table>

      <a href="/player/profile/edit" class="btn bg-orange text-white">Edit Profile</a>
      <a href="/player/teams" class="btn bg-navy text-white ms-2">Find Teams</a>
    </div>
  </div>

</@l.page>