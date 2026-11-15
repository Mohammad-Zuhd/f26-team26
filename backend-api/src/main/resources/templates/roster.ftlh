
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Team Roster | RecLeague</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<!-- Navigation -->
<nav class="navbar bg-white border-bottom shadow-sm py-2">
    <div class="container d-flex flex-wrap align-items-center justify-content-between gap-3">
        <a class="navbar-brand d-flex align-items-center gap-2" href="/">
            <img src="/images/recleague-logo.png" alt="RecLeague Logo" width="48" height="48">
            <span class="fw-bold fs-4" style="color:#123C73;">RecLeague</span>
        </a>

        <div class="d-flex flex-wrap align-items-center gap-3 gap-lg-4">
            <a href="/" class="text-decoration-none text-secondary">Dashboard</a>
            <a href="/captains/${captainId?c}/teams" class="text-decoration-none text-secondary">My Teams</a>
            <a href="/captains/${captainId?c}/spots" class="text-decoration-none text-secondary">Open Spots</a>
            <a href="/captains/${captainId?c}/roster" class="text-decoration-none fw-bold" style="color:#123C73;" aria-current="page">Roster</a>
            <span class="text-secondary opacity-50" title="Not implemented for Milestone 4">Reviews</span>
            <span class="btn btn-sm text-white opacity-50" style="background:#123C73;" title="Login/logout is not implemented">Log Out</span>
        </div>
    </div>
</nav>

<!-- Page Header -->
<header class="py-5 text-white" style="background:#123C73;">
    <div class="container">
        <span class="badge mb-3" style="background:#F15A29;">TEAM CAPTAIN</span>
        <h1 class="fw-bold">Team Roster</h1>
        <p class="mb-0">View your players and manage requests to join your team.</p>
    </div>
</header>

<main class="container my-5">

    <!-- Select Team -->
    <div class="card border-0 shadow-sm rounded-4 mb-5">
        <div class="card-body p-4">
            <h2 class="fw-bold mb-3" style="color:#123C73;">Select a Team</h2>

            <div class="d-flex flex-wrap gap-3">
                <#list teams as team>
                    <a href="/captains/${captainId?c}/roster?teamId=${team.id()?c}"
                       class="btn text-white"
                       style="background:#123C73;">
                        ${team.teamName()}
                    </a>
                <#else>
                    <p>No teams available.</p>
                </#list>
            </div>
        </div>
    </div>

    <#if selectedTeam??>

        <!-- Team Information -->
        <div class="card border-0 shadow-sm rounded-4 mb-5">
            <div class="card-body p-4">
                <span class="badge mb-2" style="background:#F15A29;">YOUR TEAM</span>
                <h3 class="fw-bold" style="color:#123C73;">
                    ${selectedTeam.teamName()}
                </h3>
                <p>${selectedTeam.sport()} | ${selectedTeam.skillLevel()}</p>
                <p class="mb-0">
                    <strong>Schedule:</strong> ${selectedTeam.schedule()}
                </p>
            </div>
        </div>

        <!-- Current Team Members -->
        <h2 class="fw-bold mb-3" style="color:#123C73;">
            Current Team Members
        </h2>

        <div class="card border-0 shadow-sm rounded-4 mb-5">
            <div class="card-body p-4 table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th>Player Name</th>
                            <th>Position</th>
                            <th>Skill Level</th>
                            <th>Status</th>
                        </tr>
                    </thead>

                    <tbody>
                        <#list members as member>
                            <tr>
                                <td>${member.playerName()}</td>
                                <td>${member.preferredPosition()}</td>
                                <td>${member.skillLevel()}</td>
                                <td>
                                    <span class="badge bg-success">
                                        ${member.status()}
                                    </span>
                                </td>
                            </tr>
                        <#else>
                            <tr>
                                <td colspan="4">No approved players yet.</td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Pending Join Requests -->
        <h2 class="fw-bold mb-3" style="color:#123C73;">
            Pending Join Requests
        </h2>

        <div class="card border-0 shadow-sm rounded-4 mb-5">
            <div class="card-body p-4 table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th>Player Name</th>
                            <th>Preferred Position</th>
                            <th>Skill Level</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>
                        <#list requests as request>
                            <tr>
                                <td>${request.playerName()}</td>
                                <td>${request.preferredPosition()}</td>
                                <td>${request.skillLevel()}</td>
                                <td>
                                    <form action="/captains/${captainId?c}/teams/${selectedTeam.id()?c}/requests/${request.id()?c}/approve"
                                          method="post" class="d-inline">
                                        <button type="submit" class="btn btn-success btn-sm">
                                            Approve
                                        </button>
                                    </form>

                                    <form action="/captains/${captainId?c}/teams/${selectedTeam.id()?c}/requests/${request.id()?c}/decline"
                                          method="post" class="d-inline">
                                        <button type="submit" class="btn btn-outline-danger btn-sm">
                                            Decline
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        <#else>
                            <tr>
                                <td colspan="4">No pending join requests.</td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>

    <#else>
        <div class="alert alert-info">
            Create a team to view its roster.
        </div>
    </#if>

</main>

<footer class="py-4 text-white text-center" style="background:#123C73;">
    <p class="fw-bold mb-1">RecLeague</p>
    <p class="mb-0 small">&copy; 2026 RecLeague. All rights reserved.</p>
</footer>

</body>
</html>
