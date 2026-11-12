<#-- friendly error page so users never see a stack trace -->
<#import "layout.ftlh" as l>
<@l.page title="Something went wrong">

  <div class="card border-0 shadow-sm rounded-4">
    <div class="card-body p-4">
      <h1 class="fw-bold navy">Oops!</h1>
      <p class="fs-5">${errorMessage!'Something went wrong.'}</p>
      <a href="/player/teams" class="btn bg-navy text-white">Browse Teams</a>
      <a href="/" class="btn btn-outline-secondary ms-2">Home</a>
    </div>
  </div>

</@l.page>