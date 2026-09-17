Requirements – RecLeague

Project Name: RecLeague
Team: Mohammad Zuhd - Provider, Goran Ali, Faiyha Dafalla
Course: CSC 340
Version: 1.0
Date: 2026-09-17

1. Overview

Vision. RecLeague is an app designed to help players find and join local recreational sports teams and help teams find substitute players when needed. The system supports players looking for teams based on sport, skill level, location, and availability, as well as team captains who want to manage their teams and fill open roster spots.

Glossary: Terms used in the project

Player: A person who uses RecLeague to find and join recreational sports teams.
Team Captain: A person who manages a team and looks for players to fill open roster spots.
Team Profile: Information about a recreational team, including its sport, skill level, location, and schedule.
Roster: A list of players who are members of a team.
Roster Spot: An available position on a team that a player can request to join.
Substitute Player: A player who fills an open spot for a team when needed.

Primary Users and Roles:

Customer (Player) - Find and join recreational sports teams.
Provider (Team Captain) - Manage teams and find players to fill open roster spots.
SysAdmin - Manage user access, moderate content, and monitor platform activity.

Scope (this semester):

Player and team captain profiles
Search and browse teams by sport, skill level, location, and availability
Team and roster management
Requests to join teams
Games and substitute player postings
Reviews and responses
User and content management

Out of scope (deferred):


This document is requirements-level and solution-neutral; design decisions (UI layouts, API endpoints, schemas) are documented separately.


2. Functional Requirements (User Stories)

2.1 Customer Stories


2.2 Provider (Team Captain) Stories

US-5 - Create and manage team profile

Story: As a team captain, I want to create, update, or remove my team profile so that I can keep my team's information accurate.

Acceptance:

Scenario: Create and update team profile
  Given I am logged in as a team captain
  When I create or update my team profile
  Then my team information should be saved
  And players should be able to see the updated team profile

Scenario: Remove team profile
  Given I am logged in as a team captain
  And I have an existing team profile
  When I remove my team profile
  Then the team profile should no longer be visible to players


US-6 - Create teams and post open spots

Story: As a team captain, I want to create teams and post available roster spots or games so that I can find players or substitutes when my team needs them.

Acceptance:

Scenario: Create teams and post open spots
  Given I am logged in as a team captain
  When I create a team or post an available roster spot or game
  Then the information should be saved
  And players should be able to view the posting


US-7 - View customer statistics

Story: As a team captain, I want to view my roster and information about players who join or request to join my team so that I can manage my team.

Acceptance:

Scenario: View customer statistics
  Given I am logged in as a team captain
  When I view my team's roster
  Then I should see the players currently on my team
  And I should see players who have requested to join


US-8 - Reply to reviews

Story: As a team captain, I want to reply to player reviews so that I can respond to their feedback.

Acceptance:

Scenario: Reply to a review
  Given I am logged in as a team captain
  When a player leaves a review for my team
  Then I should be able to submit a response to the review


2.3 SysAdmin Stories


3. Non-Functional Requirements


4. Assumptions, Constraints, and Policies


5. Milestones (course-aligned)


6. Change Management

Stories are living artifacts; changes are tracked via repository issues and linked pull requests.

Major changes should update this SRS.
