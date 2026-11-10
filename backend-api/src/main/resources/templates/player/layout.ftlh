<#--
  Shared layout for every player page (same idea as layout.ftlh in the class Plant demo).
  The navbar, colors and footer come from our Milestone 2 prototype, and they live in ONE place,
  so every page looks the same and we only fix things once.

  How a page uses it:
    <#import "layout.ftlh" as l>
    <@l.page title="My Profile" active="profile"> ...the page's own content... </@l.page>

  "title" goes in the browser tab, "active" tells the navbar which link to make bold.
  <#nested> is the spot where the page's own content gets dropped in.
-->
<#macro page title active="">
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${title} | RecLeague</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <#-- Our two brand colors from the prototype, navy and orange -->
  <style>
    .navy { color: #123C73; }
    .bg-navy { background-color: #123C73; }
    .bg-orange { background-color: #F15A29; }
    .nav-link-active { font-weight: 700; color: #123C73 !important; }
  </style>
</head>

<body class="bg-light d-flex flex-column min-vh-100">

  <nav class="navbar bg-white border-bottom shadow-sm">
    <div class="container">
      <a class="navbar-brand d-flex align-items-center navy fw-bold fs-4" href="/">
        <#-- Images are served from src/main/resources/static/images -->
        <img src="/images/recleague-logo.png" alt="RecLeague Logo" width="64" height="64" class="me-2">
        RecLeague
      </a>
      <div class="d-flex align-items-center gap-4">
        <a href="/player/teams" class="text-decoration-none text-secondary ${(active == 'teams')?then('nav-link-active', '')}">Browse Teams</a>

        <#-- playerLoggedIn comes from PlayerUiExceptionHandler and is true when the session has a player.
             The !false means "if it's missing, treat it as false" (error pages don't always get it). -->
        <#if playerLoggedIn!false>
          <a href="/player/profile" class="text-decoration-none text-secondary ${(active == 'profile')?then('nav-link-active', '')}">My Profile</a>
          <#-- Logging out is a POST, so it needs a tiny form instead of a link -->
          <form method="post" action="/player/logout" class="m-0">
            <button type="submit" class="btn bg-navy text-white">Log Out</button>
          </form>
        <#else>
          <a href="/player/login" class="text-decoration-none text-secondary ${(active == 'login')?then('nav-link-active', '')}">Find My Profile</a>
          <a href="/player/signup" class="btn bg-orange text-white">Sign Up</a>
        </#if>
      </div>
    </div>
  </nav>

  <main class="container my-5 flex-grow-1">
    <#-- A "flash" message from the controller (like "Your profile was saved."). It shows up once. -->
    <#if message??>
      <div class="alert alert-success">${message}</div>
    </#if>
    <#nested>
  </main>

  <footer class="py-4 text-white text-center bg-navy">
    <p class="fw-bold mb-1">RecLeague</p>
    <p class="mb-0 small">&copy; 2026 RecLeague. All rights reserved.</p>
  </footer>
</body>
</html>
</#macro>