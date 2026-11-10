<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Teams | RecLeague</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

    <!-- Navbar -->
    <nav class="navbar bg-white border-bottom shadow-sm">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center" href="dashboard.html">
                <img src="../images/recleague-logo.png" alt="RecLeague Logo" width="100" height="100" class="me-2">
                <span class="fw-bold fs-4" style="color: #123C73;">RecLeague</span>
            </a>

            <div class="d-flex align-items-center gap-4">
                <a href="dashboard.html" class="text-decoration-none" style="color: #123C73;">Dashboard</a>
                <a href="/captains/${captainId?c}/teams" class="text-decoration-none fw-bold" style="color: #123C73;">My Teams</a>
                <a href="post-open-spots.html" class="text-decoration-none text-secondary">Open Spots</a>
                <a href="roster.html" class="text-decoration-none text-secondary">Roster</a>
                <a href="reviews.html" class="text-decoration-none text-secondary">Reviews</a>
                <a href="../index.html" class="btn text-white" style="background-color: #123C73;">Log Out</a>
            </div>
        </div>
    </nav>

    <!-- Page Header -->
    <section class="py-5 text-white" style="background-color: #123C73;">
        <div class="container">
            <span class="badge mb-3" style="background-color: #F15A29;">TEAM MANAGEMENT</span>
            <h1 class="fw-bold">Manage Your Teams</h1>
            <p class="mb-0">Create, update, and manage your recreational sports teams.</p>
        </div>
    </section>

    <main id="myTeams" class="container my-5">

        <!-- Error Message -->
        <#if error??>
            <div class="alert alert-danger">
                ${error}
            </div>
        </#if>

        <!-- My Teams -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold" style="color: #123C73;">My Teams</h2>
            <a href="#createTeam" class="btn text-white" style="background-color: #F15A29;">+ Create Team</a>
        </div>

        <!-- Display Teams from Database -->
        <#if teams?has_content>
            <#list teams as team>
                <div class="card border-0 shadow-sm rounded-4 mb-4">
                    <div class="card-body p-4">

                        <h3 class="fw-bold" style="color: #123C73;">
                            ${team.teamName()}
                        </h3>

                        <p class="text-muted">
                            ${team.sport()} | ${team.skillLevel()}
                        </p>

                        <p>
                            <strong>Location:</strong> ${team.location()}
                        </p>

                        <p>
                            <strong>Schedule:</strong> ${team.schedule()}
                        </p>

                        <a href="#editTeam-${team.id()?c}"
                           class="btn text-white"
                           style="background-color: #123C73;">
                            Edit Team
                        </a>

                        <a href="#removeTeam" class="btn btn-outline-danger">
                            Remove Team
                        </a>

                    </div>
                </div>
            </#list>
        <#else>
            <div class="alert alert-info">
                You have not created any teams yet.
            </div>
        </#if>


        <!-- Create Team -->
        <div id="createTeam" class="card border-0 shadow-sm rounded-4 mb-5">
            <div class="card-body p-4">

                <h2 class="fw-bold mb-3" style="color: #123C73;">
                    Create a New Team
                </h2>

                <form action="/captains/${captainId?c}/teams" method="post">
                    <div class="row g-3">

                        <div class="col-md-6">
                            <label for="teamName" class="form-label">Team Name</label>
                            <input type="text" id="teamName" name="teamName"
                                   class="form-control"
                                   maxlength="100"
                                   placeholder="Enter team name" required>
                        </div>

                        <div class="col-md-6">
                            <label for="sport" class="form-label">Sport</label>
                            <select id="sport" name="sport" class="form-select" required>
                                <option value="">Select a sport</option>
                                <option value="Basketball">Basketball</option>
                                <option value="Soccer">Soccer</option>
                                <option value="Volleyball">Volleyball</option>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label for="skillLevel" class="form-label">Skill Level</label>
                            <select id="skillLevel" name="skillLevel" class="form-select" required>
                                <option value="">Select skill level</option>
                                <option value="Beginner">Beginner</option>
                                <option value="Intermediate">Intermediate</option>
                                <option value="Advanced">Advanced</option>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label for="location" class="form-label">Location</label>
                            <input type="text" id="location" name="location"
                                   class="form-control"
                                   maxlength="150"
                                   placeholder="Enter location" required>
                        </div>

                        <!-- Team Schedule -->
                        <div class="col-md-6">
                            <label for="schedule" class="form-label">Schedule</label>
                            <input type="text" id="schedule" name="schedule"
                                   class="form-control"
                                   maxlength="255"
                                   placeholder="Example: Saturday at 3 PM" required>
                        </div>

                        <div class="col-12">
                            <label for="description" class="form-label">Team Description</label>
                            <textarea id="description" name="description"
                                      class="form-control"
                                      rows="3"
                                      maxlength="2000"
                                      placeholder="Tell players about your team"></textarea>
                        </div>

                        <div class="col-12">
                            <button type="submit" class="btn text-white"
                                    style="background-color: #F15A29;">
                                Create Team
                            </button>
                            <a href="#myTeams" class="btn btn-outline-secondary">
                                Cancel
                            </a>
                        </div>

                    </div>
                </form>

            </div>
        </div>


        <!-- Edit Team -->
        <#list teams as team>
            <div id="editTeam-${team.id()?c}"
                 class="card border-0 shadow-sm rounded-4 mb-5">

                <div class="card-body p-4">

                    <h2 class="fw-bold mb-3" style="color: #123C73;">
                        Edit Team: ${team.teamName()}
                    </h2>

                    <form action="/captains/${captainId?c}/teams/${team.id()?c}/edit" method="post">
                        <div class="row g-3">

                            <div class="col-md-6">
                                <label class="form-label">Team Name</label>
                                <input type="text" name="teamName"
                                       class="form-control"
                                       value="${team.teamName()}"
                                       maxlength="100" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Sport</label>
                                <select name="sport" class="form-select" required>
                                    <option value="Basketball"
                                        <#if team.sport() == "Basketball">selected</#if>>
                                        Basketball
                                    </option>
                                    <option value="Soccer"
                                        <#if team.sport() == "Soccer">selected</#if>>
                                        Soccer
                                    </option>
                                    <option value="Volleyball"
                                        <#if team.sport() == "Volleyball">selected</#if>>
                                        Volleyball
                                    </option>
                                    <#if team.sport() != "Basketball" && team.sport() != "Soccer" && team.sport() != "Volleyball">
                                        <option value="${team.sport()}" selected>${team.sport()}</option>
                                    </#if>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Skill Level</label>
                                <select name="skillLevel" class="form-select" required>
                                    <option value="Beginner"
                                        <#if team.skillLevel() == "Beginner">selected</#if>>
                                        Beginner
                                    </option>
                                    <option value="Intermediate"
                                        <#if team.skillLevel() == "Intermediate">selected</#if>>
                                        Intermediate
                                    </option>
                                    <option value="Advanced"
                                        <#if team.skillLevel() == "Advanced">selected</#if>>
                                        Advanced
                                    </option>
                                    <#if team.skillLevel() != "Beginner" && team.skillLevel() != "Intermediate" && team.skillLevel() != "Advanced">
                                        <option value="${team.skillLevel()}" selected>${team.skillLevel()}</option>
                                    </#if>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Location</label>
                                <input type="text" name="location"
                                       class="form-control"
                                       value="${team.location()}"
                                       maxlength="150" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Schedule</label>
                                <input type="text" name="schedule"
                                       class="form-control"
                                       value="${team.schedule()}"
                                       maxlength="255" required>
                            </div>

                            <div class="col-12">
                                <label class="form-label">Team Description</label>
                                <textarea name="description"
                                          class="form-control"
                                          rows="3"
                                          maxlength="2000">${(team.description())!''}</textarea>
                            </div>

                            <div class="col-12">
                                <button type="submit" class="btn text-white"
                                        style="background-color: #123C73;">
                                    Save Changes
                                </button>

                                <a href="#myTeams" class="btn btn-outline-secondary">
                                    Cancel
                                </a>
                            </div>

                        </div>
                    </form>

                </div>
            </div>
        </#list>


        <!-- Remove Team -->
        <div id="removeTeam" class="card border-0 shadow-sm rounded-4 mb-5">
            <div class="card-body p-4">

                <h2 class="fw-bold text-danger">Remove Team</h2>
                <p>Select the team you want to remove.</p>

                <form action="/captains/${captainId?c}/teams/delete" method="post">

                    <label for="teamToRemove" class="form-label">
                        Choose Team
                    </label>

                    <select id="teamToRemove" name="teamToRemove"
                            class="form-select mb-3" required>
                        <option value="">Select a team</option>

                        <#list teams as team>
                            <option value="${team.id()?c}">
                                ${team.teamName()}
                            </option>
                        </#list>

                    </select>

                    <button type="submit" class="btn btn-danger">
                        Confirm Remove
                    </button>

                    <a href="#myTeams" class="btn btn-outline-secondary">
                        Cancel
                    </a>

                </form>

            </div>
        </div>

    </main>

    <!-- Footer -->
    <footer class="py-4 text-white text-center" style="background-color: #123C73;">
        <p class="fw-bold mb-1">RecLeague</p>
        <p class="mb-0 small">&copy; 2026 RecLeague. All rights reserved.</p>
    </footer>

</body>
</html>
