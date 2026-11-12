<#--
  "Find My Profile" page
  Players don't have passwords in our API, so they come back by typing the email they signed up with.
-->
<#import "layout.ftlh" as l>
<@l.page title="Find My Profile" active="login">

  <div class="row justify-content-center">
    <div class="col-md-6">
      <h1 class="fw-bold navy mb-4">Find My Profile</h1>
      <div class="card border-0 shadow-sm rounded-4">
        <div class="card-body p-4">
          <#if formError??>
            <div class="alert alert-danger">${formError}</div>
          </#if>
          <form method="post" action="/player/login">
            <label for="email" class="form-label">Email you signed up with</label>
            <input id="email" name="email" type="email" class="form-control mb-3" value="${email!''}">
            <button type="submit" class="btn bg-navy text-white">Continue</button>
          </form>
          <p class="small text-muted mt-3 mb-1">Prototype login: email only, no password check yet.</p>
          <p class="mb-0 text-muted">New here? <a href="/player/signup">Create a profile</a>.</p>
        </div>
      </div>
    </div>
  </div>

</@l.page>