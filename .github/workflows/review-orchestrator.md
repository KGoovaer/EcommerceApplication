---
description: Start the PR review pipeline when a PR is labeled ai-review-requested
on:
  pull_request:
    types: [labeled]
if: "${{ github.event.label.name == 'ai-review-requested' }}"
permissions:
  contents: read
  pull-requests: read
  issues: read
  actions: read
safe-outputs:
  dispatch-workflow:
    workflows: [review-quality-engineer, review-security-engineer]
    max: 2
  add-labels:
    allowed: [ai-review]
timeout-minutes: 5
---

# PR Review Orchestrator

A pull request has been labeled `ai-review-requested`, requesting an AI-powered code review by a quality engineer and a security engineer.

## Your Task

1. **Add the `ai-review` label** to PR #${{ github.event.pull_request.number }}. This label is required for downstream review agents to post their findings.
2. **Dispatch both review workflows in parallel:**
   - **`review-quality-engineer`** — Reviews code quality, patterns, and maintainability
   - **`review-security-engineer`** — Reviews security posture against the secure development policy

   Pass these inputs to both:
   - `pr_number`: `${{ github.event.pull_request.number }}`
   - `branch`: Use the GitHub tool to look up PR #${{ github.event.pull_request.number }} and get the head branch name.

Both engineers run independently and post their own review comments on the PR. You do not need to wait for either to finish. Your only job is to add the label and dispatch both workflows.
