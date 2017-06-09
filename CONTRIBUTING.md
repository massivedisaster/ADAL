Contributing to ADAL
======================

This document describes the contribution guidelines for ADAL. 

Before starting a new feature or bug fix
------------

When a new bug is detected or a new feature needs to be added there are some steps that need to be followed.

1. Create a new [issue](https://github.com/massivedisaster/ADAL/issues/new) with the full description.
2. [If needed] Create the sample for that feature, or update the sample if the bug fix changes de sample status.
3. When the feature is done, please run (`./gradlew check`) to ensure the code respects the ADAL style guidelines.
4. Create PR when you're done.

* See [Adding new module](https://github.com/massivedisaster/ADAL/wiki/Adding-a-new-Module) when theres is a need to add a new module.

DO and DON'Ts
--------------------

* **DO** include tests when adding new features. When fixing bugs, start with adding a test that highlights how the current behavior is broken.  
* **DO** keep the discussions focused. When a new or related topic comes up it's often better to create new issue than to side track the discussion.
* **DO** submit all code changes via pull requests (PRs) rather than through a direct commit. PRs will be reviewed and potentially merged by the repo maintainers after a peer review that includes at least one maintainer.
* **DO** give PRs short-but-descriptive names (e.g. "Improve code coverage for xpto by 10%", not "Fix #23")
* **DO** tag any users that should know about and/or review the change.
* **DO** ensure each commit successfully builds.
* **DO** run check validator (`./gradlew check`) when submiting PRs.
* **DO** fix merge conflicts using a merge commit in public branches. Prefer `git rebase` to get new commits from develop to local feature.
* **DO NOT** submit "work in progress" PRs.  A PR should only be submitted when it is considered ready for review and subsequent merging by the contributor.
* **DO NOT** mix independent, unrelated changes in one PR. Separate unrelated fixes into separate PRs.
* **DO NOT** send PRs for style changes. 
* **DO NOT** surprise us with big pull requests. Instead, file an issue and start a discussion so we can agree on a direction before you invest a large amount of time.
* **DO NOT** commit code that you didn't write. If you find code that you think is a good fit to add to ADAL, file an issue and start a discussion before proceeding.
* **DO NOT** submit PRs that alter licensing related files or headers. If you believe there's a problem with them, file an issue and we'll be happy to discuss it.
