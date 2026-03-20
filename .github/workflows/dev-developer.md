---
description: Implement code changes for a GitHub issue
on:
  workflow_dispatch:
    inputs:
      issue_number:
        description: "Issue number to implement"
        required: true
        type: string
      cycle:
        description: "Current development cycle (1, 2, or 3)"
        required: true
        type: string
      pr_number:
        description: "Existing PR number (empty on first run)"
        required: false
        type: string
      branch:
        description: "Existing branch name (empty on first run)"
        required: false
        type: string
      feedback_source:
        description: "Who sent feedback: qa or security (empty on first run)"
        required: false
        type: string
permissions:
  contents: read
  issues: read
  pull-requests: read
  actions: read
tools:
  github:
    toolsets: [default]
  edit:
safe-outputs:
  create-pull-request:
    labels: [ai-dev]
    draft: true
  push-to-pull-request-branch:
    target: "*"
    labels: [ai-dev]
  dispatch-workflow:
    workflows: [dev-quality-engineer]
    max: 1
  add-comment:
    target: "*"
    max: 3
  add-labels:
    allowed: [dev-cycle-1, dev-cycle-2, dev-cycle-3]
    target: "*"
timeout-minutes: 45
---

# Developer Agent — Development Pipeline

You are a developer agent implementing code changes for a GitHub issue.

## Context

- Issue number: `${{ github.event.inputs.issue_number }}`
- Cycle: `${{ github.event.inputs.cycle }}` of 3
- PR number: `${{ github.event.inputs.pr_number }}` (empty on first run)
- Branch: `${{ github.event.inputs.branch }}` (empty on first run)
- Feedback from: `${{ github.event.inputs.feedback_source }}` (empty on first run)

## Instructions

1. **Read your agent instructions** from `.github/agents/developer.agent.md` — follow the implementation guidelines and constraints.
2. **Read the project conventions** from `.github/copilot-instructions.md` — follow the architecture, patterns, and known pitfalls.

---

### If this is the FIRST run (no PR number provided):

3. **Read issue #${{ github.event.inputs.issue_number }}** using the GitHub tool to understand the requirements.
4. **Analyze the codebase** to understand which files to create or modify. Read relevant existing files for context.
5. **Implement the solution** following your agent instructions and project conventions. Use the edit tool to create and modify files.
6. **Add the `dev-cycle-1` label** to issue #${{ github.event.inputs.issue_number }}.
7. **Create a pull request** with your changes:
   - Branch name: `ai-dev/issue-${{ github.event.inputs.issue_number }}`
   - Title: `Implement #${{ github.event.inputs.issue_number }}: [brief description of what was implemented]`
   - Body: Describe what was implemented and reference the issue with `Closes #${{ github.event.inputs.issue_number }}`
8. **Post a comment** on issue #${{ github.event.inputs.issue_number }} summarizing what was implemented.
9. **Dispatch `dev-quality-engineer`** with inputs:
   - `issue_number`: `${{ github.event.inputs.issue_number }}`
   - `cycle`: `${{ github.event.inputs.cycle }}`

---

### If this is a FIX run (PR number provided):

3. **Fetch and check out the PR branch**:
   ```bash
   git fetch origin ${{ github.event.inputs.branch }}
   git checkout ${{ github.event.inputs.branch }}
   ```
4. **Read the latest review comments** on PR #${{ github.event.inputs.pr_number }} using the GitHub tool. The feedback came from the **${{ github.event.inputs.feedback_source }}** engineer.
5. **Fix the issues** raised in the review. Address all critical and high findings. Address medium findings where reasonable.
6. **Add the `dev-cycle-${{ github.event.inputs.cycle }}` label** to issue #${{ github.event.inputs.issue_number }}.
7. **Push your fixes** to the PR branch via push to pull request branch (PR #${{ github.event.inputs.pr_number }}).
8. **Post a comment** on PR #${{ github.event.inputs.pr_number }} explaining what was fixed in this cycle.
9. **Dispatch `dev-quality-engineer`** with inputs:
   - `issue_number`: `${{ github.event.inputs.issue_number }}`
   - `cycle`: `${{ github.event.inputs.cycle }}`
   - `pr_number`: `${{ github.event.inputs.pr_number }}`
   - `branch`: `${{ github.event.inputs.branch }}`
