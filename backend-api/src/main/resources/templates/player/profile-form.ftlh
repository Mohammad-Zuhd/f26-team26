<#-- One form for both "Create" and "Edit" (same idea as plant-form.ftlh in the class demo) -->
<#import "layout.ftlh" as l>

<#-- Grab Spring's validation errors so each field can show its own message -->
<#assign errors = .data_model["org.springframework.validation.BindingResult.player"]!>
<#function fieldError field>
  <#if errors?has_content && errors.hasFieldErrors(field)>
    <#return errors.getFieldError(field).defaultMessage>
  </#if>
  <#return "">
</#function>

<@l.page title=formTitle active="profile">

  <h1 class="fw-bold navy mb-4">${formTitle}</h1>

  <div class="card border-0 shadow-sm rounded-4">
    <div class="card-body p-4">

      <#-- Error from the service, like a duplicate email -->
      <#if formError??>
        <div class="alert alert-danger">${formError}</div>
      </#if>

      <#-- novalidate lets the server do the checking, so our own error messages show -->
      <form method="post" action="${formAction}" novalidate>

        <div class="mb-3">
          <label for="name" class="form-label">Full name</label>
          <input id="name" name="name" type="text" maxlength="100"
                 class="form-control ${(fieldError('name')?has_content)?then('is-invalid', '')}"
                 value="${(player.name)!''}">
          <div class="invalid-feedback">${fieldError('name')}</div>
        </div>

        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input id="email" name="email" type="email" maxlength="254"
                 class="form-control ${(fieldError('email')?has_content)?then('is-invalid', '')}"
                 value="${(player.email)!''}">
          <div class="invalid-feedback">${fieldError('email')}</div>
        </div>

        <div class="mb-3">
          <label for="location" class="form-label">Location</label>
          <input id="location" name="location" type="text" maxlength="100" placeholder="Greensboro, NC"
                 class="form-control ${(fieldError('location')?has_content)?then('is-invalid', '')}"
                 value="${(player.location)!''}">
          <div class="invalid-feedback">${fieldError('location')}</div>
        </div>

        <div class="mb-3">
          <label for="preferredSports" class="form-label">Preferred sports</label>
          <input id="preferredSports" name="preferredSports" type="text" maxlength="255" placeholder="Soccer, Basketball"
                 class="form-control ${(fieldError('preferredSports')?has_content)?then('is-invalid', '')}"
                 value="${(player.preferredSports)!''}">
          <div class="invalid-feedback">${fieldError('preferredSports')}</div>
        </div>

        <div class="mb-3">
          <label for="skillLevel" class="form-label">Skill level</label>
          <#assign currentLevel = (player.skillLevel)!''>
          <select id="skillLevel" name="skillLevel"
                  class="form-select ${(fieldError('skillLevel')?has_content)?then('is-invalid', '')}">
            <option value="">Choose...</option>
            <#list skillLevels as level>
              <option value="${level}" <#if currentLevel == level>selected</#if>>${level}</option>
            </#list>
          </select>
          <div class="invalid-feedback">${fieldError('skillLevel')}</div>
        </div>

        <div class="mb-4">
          <label for="availability" class="form-label">Availability <span class="text-muted">(optional)</span></label>
          <input id="availability" name="availability" type="text" maxlength="255" placeholder="Weeknights after 6pm"
                 class="form-control" value="${(player.availability)!''}">
        </div>

        <button type="submit" class="btn bg-navy text-white">Save</button>
        <a href="${(playerLoggedIn!false)?then('/player/profile', '/')}" class="btn btn-outline-secondary ms-2">Cancel</a>
      </form>
    </div>
  </div>

</@l.page>
