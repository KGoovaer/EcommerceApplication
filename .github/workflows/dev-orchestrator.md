---
description: Start the development pipeline when an issue is labeled ai-develop
on:
  issues:
    types: [labeled]
if: "${{ github.event.label.name == 'ai-develop' }}"
permissions:
  contents: read
  issues: read
  actions: read
safe-outputs:
  dispatch-workflow:
    workflows: [dev-developer]
    max: 1
  add-labels:
    allowed: [ai-dev-in-progress, dev-cycle-1]
    target: "*"
  add-comment:
    target: "*"
    max: 1
timeout-minutes: 5
---

# Development Pipeline Orchestrator

An issue has been labeled `ai-develop`, requesting AI-powered development.

## Your Task

1. **Add the `ai-dev-in-progress` label** to issue #${{ github.event.issue.number }}.
2. **Add the `dev-cycle-1` label** to issue #${{ github.event.issue.number }}.
3. **Add a comment** to issue #${{ github.event.issue.number }}:
   > 🚀 Development pipeline started. A developer agent will implement this issue, followed by quality and security reviews. Maximum 3 development cycles.
4. **Dispatch the `dev-developer` workflow** to begin development. Pass these inputs:
   - `issue_number`: `${{ github.event.issue.number }}`
   - `cycle`: `1`

Do not perform any code analysis yourself. Your only job is to add labels, comment, and start the chain.
