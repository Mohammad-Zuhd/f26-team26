# Requirements – RecLeague

**Project Name:** RecLeague

**Team:** Mohammad Zuhd - Provider, Goran Ali - Customer, Faiyha Dafalla

**Course:** CSC 340

**Version:** 1.0

**Date:** 2026-09-17

---

## 1. Overview

**Vision.** RecLeague is an app designed to help players find and join local recreational sports teams and help teams find substitute players when needed. The system supports players looking for teams based on sport, skill level, location, and availability, as well as team captains who want to manage their teams and fill open roster spots.

**Glossary:** Terms used in the project

- **Player:** A person who uses RecLeague to find and join recreational sports teams.
- **Team Captain:** A person who manages a team and looks for players to fill open roster spots.
- **Team Profile:** Information about a recreational team, including its sport, skill level, location, and schedule.
- **Roster:** A list of players who are members of a team.
- **Roster Spot:** An available position on a team that a player can request to join.
- **Substitute Player:** A player who fills an open spot for a team when needed.

**Primary Users and Roles:**

- **Customer (Player):** Find and join recreational sports teams.
- **Provider (Team Captain):** Manage teams and find players to fill open roster spots.
- **SysAdmin:** Manage user access, moderate content, and monitor platform activity.

**Scope (this semester):**

- Player and team captain profiles
- Search and browse teams by sport, skill level, location, and availability
- Team and roster management
- Requests to join teams
- Games and substitute player postings
- Reviews and responses
- User and content management

**Out of scope (deferred):**

> This document is **requirements-level** and solution-neutral; design decisions (UI layouts, API endpoints, schemas) are documented separately.

---

## 2. Functional Requirements (User Stories)

### 2.1 Customer (Player) Stories

**US-1 - Create and manage player profile**

*Story:* As a player, I want to create and update my profile with my location, preferred sports, skill level, and availability so that captains can tell whether I am a good fit for their team.

*Acceptance:*

```text
Scenario: Create a player profile
  Given I am registered and logged in as a player
  When I enter my name, location, preferred sports, skill level, and availability
  Then my profile should be saved
  And team captains should be able to view my profile
```

```text
Scenario: Update a player profile
  Given I am logged in as a player
  And I have an existing profile
  When I change my skill level or availability
  Then my updated information should be saved
  And the changes should be reflected anywhere my profile is shown
```

**US-2 - Browse teams with open roster spots**

*Story:* As a player, I want to browse teams with open roster spots filtered by sport, skill level, location, and schedule so that I only see teams I could realistically join.

*Acceptance:*

```text
Scenario: Browse teams by filter
  Given I am logged in as a player
  When I select a sport, skill level, location, and schedule
  Then I should see a list of teams with open roster spots matching those filters
  And each team should display its sport, skill level, location, and schedule
```

```text
Scenario: No teams match the selected filters
  Given I am logged in as a player
  When I apply filters that no team matches
  Then I should see a message telling me no teams were found
```

**US-3 - Request to join a team**

*Story:* As a player, I want to request to join a team with an open roster spot so that I can be added to the roster once the captain approves me.

*Acceptance:*

```text
Scenario: Request to join a team
  Given I am logged in as a player
  And I am viewing a team with an open roster spot
  When I submit a request to join that team
  Then my request should be sent to the team captain
  And I should see the request listed as pending
```

```text
Scenario: Captain approves my request
  Given I have a pending request to join a team
  When the team captain approves my request
  Then I should be added to that team's roster
  And the team should appear in my list of teams
```

**US-4 - Write a review for a team**

*Story:* As a player, I want to write a review of a team I have played with so that other players know what the team's organization and competitiveness are like.

*Acceptance:*

```text
Scenario: Write a review for a team
  Given I am logged in as a player
  And I am on the roster of a team
  When I submit a review with a rating and comments
  Then my review should be saved
  And it should be visible to other players viewing that team
```

### 2.2 Provider (Team Captain) Stories

**US-5 - Create and manage team profile**

*Story:* As a team captain, I want to create, update, or remove my team profile so that I can keep my team's information accurate.

*Acceptance:*

```text
Scenario: Create and manage team profile
  Given I am logged in as a team captain
  When I create or update my team profile
  Then my team information should be saved
  And players should be able to see the updated team profile
```

```text
Scenario: Remove team profile
  Given I am logged in as a team captain
  And I have an existing team profile
  When I remove my team profile
  Then the team profile should no longer be visible to players
```

**US-6 - Create teams and post open spots**

*Story:* As a team captain, I want to create teams and post available roster spots or games so that I can find players or substitutes when my team needs them.

*Acceptance:*

```text
Scenario: Create teams and post open spots
  Given I am logged in as a team captain
  When I create a team or post an available roster spot or game
  Then the information should be saved
  And players should be able to view the posting
```

**US-7 - View customer statistics**

*Story:* As a team captain, I want to view my roster and information about players who join or request to join my team so that I can manage my team.

*Acceptance:*

```text
Scenario: View customer statistics
  Given I am logged in as a team captain
  When I view my team's roster
  Then I should see the players currently on my team
  And I should see players who have requested to join
```

**US-8 - Reply to reviews**

*Story:* As a team captain, I want to reply to player reviews so that I can respond to their feedback.

*Acceptance:*

```text
Scenario: Reply to a review
  Given I am logged in as a team captain
  When a player leaves a review for my team
  Then I should be able to submit a response to the review
```


### 2.3 SysAdmin Stories


## 3. Non-Functional Requirements
- **Performance:** 95% of team search and browse requests should return results in under 2 seconds under typical load.
- **Availability/Reliability:** The system should be available 99.5% of the time, with planned maintenance windows communicated to users in advance.
- **Security/Privacy:** The system must use secure authentication and authorization. Player location and contact information must be encrypted in transit and at rest, and visible only to captains of teams the player has requested to join.
- **Usability:** A new player should be able to register, complete a profile, and submit a request to join a team within 5 minutes without outside help.
- **Scalability:** The system should support at least 500 concurrent users without degraded search performance.

## 4. Assumptions, Constraints, and Policies


## 5. Milestones (course-aligned)


## 6. Change Management

Stories are living artifacts; changes are tracked via repository issues and linked pull requests.

Major changes should update this SRS.