
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Open Spots | RecLeague</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar bg-white border-bottom shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold fs-4" href="/captains/${captainId?c}/teams" style="color:#123C73;">RecLeague</a>
        <div class="d-flex gap-4">
            <a href="/captains/${captainId?c}/teams" class="text-decoration-none text-secondary">My Teams</a>
            <a href="/captains/${captainId?c}/spots" class="text-decoration-none fw-bold" style="color:#123C73;">Open Spots</a>
        </div>
    </div>
</nav>

<header class="py-5 text-white" style="background:#123C73;">
    <div class="container">
        <span class="badge mb-3" style="background:#F15A29;">TEAM CAPTAIN</span>
        <h1 class="fw-bold">Post Open Spots</h1>
        <p>Find new players and substitutes for your recreational sports teams.</p>
    </div>
</header>

<main class="container my-5">
    <#if error??><div class="alert alert-danger">${error}</div></#if>
    <h2 class="fw-bold mb-4" style="color:#123C73;">Current Open Spots</h2>

    <div class="row g-4 mb-5">
        <#list spots as spot>
        <div class="col-md-4">
            <div class="card h-100 border-0 shadow-sm rounded-4">
                <div class="card-body p-4">
                    <h4 class="fw-bold" style="color:#123C73;">
                        <#list teams as team><#if team.id() == spot.teamId()>${team.teamName()}</#if></#list>
                    </h4>

                    <#if (spot.gameId())??>
                        <span class="badge bg-primary mb-3">Game</span>
                    <#else>
                        <span class="badge bg-secondary mb-3">Open Spot</span>
                    </#if>

                    <p>${spot.spotType()}</p>
                    <p>${spot.gameDate().toString()} at ${spot.gameTime().toString()}</p>
                    <p>${spot.location()}</p>
                    <p><strong>${spot.availableSpots()?c} spots open</strong></p>
                    <p>Status: ${spot.status()}</p>

                    <#if spot.status() == "OPEN">
                    <details class="mb-3">
                        <summary class="btn btn-outline-primary btn-sm">Edit Spot</summary>
                        <form action="/captains/${captainId?c}/spots/${spot.teamId()?c}/${spot.id()?c}/edit" method="post" class="mt-3">
                            <select name="spotType" class="form-select mb-2" required>
                                <option value="Permanent Player" <#if spot.spotType() == "Permanent Player">selected</#if>>Permanent Player</option>
                                <option value="Substitute Player" <#if spot.spotType() == "Substitute Player">selected</#if>>Substitute Player</option>
                            </select>
                            <input type="number" name="availableSpots" value="${spot.availableSpots()?c}" min="0" class="form-control mb-2" required>
                            <input type="date" name="gameDate" value="${spot.gameDate().toString()}" class="form-control mb-2" required>
                            <input type="time" name="gameTime" value="${spot.gameTime().toString()}" class="form-control mb-2" required>
                            <input type="text" name="location" value="${spot.location()}" maxlength="150" class="form-control mb-2" required>
                            <textarea name="description" class="form-control mb-2" maxlength="2000">${spot.description()!''}</textarea>
                            <button class="btn btn-primary btn-sm">Save Changes</button>
                        </form>
                    </details>
                    <form action="/captains/${captainId?c}/spots/${spot.teamId()?c}/${spot.id()?c}/close" method="post">
                        <button class="btn btn-outline-danger btn-sm">Close Spot</button>
                    </form>
                    </#if>
                </div>
            </div>
        </div>
        <#else>
            <p>No open spots posted yet.</p>
        </#list>
    </div>

    <div class="card border-0 shadow-sm rounded-4 mb-5">
        <div class="card-body p-4">
            <h2 class="fw-bold mb-3" style="color:#123C73;">Post a New Open Spot or Game</h2>
            <form action="/captains/${captainId?c}/spots" method="post" class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Select Team</label>
                    <select name="teamId" class="form-select" required>
                        <option value="">Select your team</option>
                        <#list teams as team><option value="${team.id()?c}">${team.teamName()}</option></#list>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Spot Type</label>
                    <select name="spotType" class="form-select" required>
                        <option value="">Select spot type</option>
                        <option value="Permanent Player">Permanent Player</option>
                        <option value="Substitute Player">Substitute Player</option>
                    </select>
                </div>
                <div class="col-md-6"><label class="form-label">Number of Spots</label><input type="number" name="availableSpots" min="1" class="form-control" required></div>
                <div class="col-md-6"><label class="form-label">Game Date</label><input type="date" name="gameDate" class="form-control" required></div>
                <div class="col-md-6"><label class="form-label">Game Time</label><input type="time" name="gameTime" class="form-control" required></div>
                <div class="col-md-6"><label class="form-label">Location</label><input type="text" name="location" maxlength="150" class="form-control" required></div>
                <div class="col-12"><label class="form-label">Additional Details</label><textarea name="description" maxlength="2000" class="form-control" rows="3"></textarea></div>
                <div class="col-12 d-flex gap-2">
                    <button class="btn text-white" style="background:#F15A29;">Post Open Spot</button>
                    <button formaction="/captains/${captainId?c}/games" class="btn text-white" style="background:#123C73;">Post Game Needing Players</button>
                </div>
            </form>
        </div>
    </div>
</main>

<footer class="py-4 text-white text-center" style="background:#123C73;">
    <p class="mb-0">&copy; 2026 RecLeague. All rights reserved.</p>
</footer>
</body>
</html>
