Contributing to ADAL
======================

This document describes the contribution guidelines for ADAL. 

Before starting a new feature or bug fix
------------

When a new bug is detected or a new feature needs to be added there are some steps that need to be followed.

1. Create a new [issue](https://github.com/massivedisaster/ADAL/issues/new) with the full description.
2. Create the sample for that feature, or update the sample if the bug fix changes de sample status.
3. Create a wiki section for the feature, or update the existing section if the bug fix changes wiki status.
4. Work on feature branch and PR when you're done.

File Headers
------------

The following file header is the used for ADAL.

```
ADAL - A set of Android libraries to help speed up Android development.
Copyright (C) 2017 ADAL.

ADAL is free software; you can redistribute it and/or modify it under
the terms of the GNU Lesser General Public License as published by the Free
Software Foundation; either version 3 of the License, or any later version.

ADAL is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with ADAL. If not, see http://www.gnu.org/licenses/.
```

See [Configuring file header](https://github.com/massivedisaster/ADAL/wiki#configuring-file-header) for more detail in how to configure Android Studio to use the header.

DO and DON'Ts
--------------------

* **DO** give priority to the current style of the project or file you're changing even if it diverges from the general guidelines.
* **DO** include tests when adding new features. When fixing bugs, start with
  adding a test that highlights how the current behavior is broken.  
* **DO** keep the discussions focused. When a new or related topic comes up
  it's often better to create new issue than to side track the discussion.
* **DO** submit all code changes via pull requests (PRs) rather than through a direct commit. PRs will be reviewed and potentially merged by the repo maintainers after a peer review that includes at least one maintainer.
* **DO** give PRs short-but-descriptive names (e.g. "Improve code coverage for xpto by 10%", not "Fix #23")
* **DO** tag any users that should know about and/or review the change.
* **DO** ensure each commit successfully builds.
* **DO NOT** submit "work in progress" PRs.  A PR should only be submitted when it is considered ready for review and subsequent merging by the contributor.
* **DO NOT** fix merge conflicts using a merge commit. Prefer `git rebase`.
* **DO NOT** mix independent, unrelated changes in one PR. Separate real product/test code changes from larger code formatting/dead code removal changes. Separate unrelated fixes into separate PRs, especially if they are in different packages.
* **DO NOT** send PRs for style changes. 
* **DO NOT** surprise us with big pull requests. Instead, file an issue and start
  a discussion so we can agree on a direction before you invest a large amount
  of time.
* **DO NOT** commit code that you didn't write. If you find code that you think is a good fit to add to ADAL, file an issue and start a discussion before proceeding.
* **DO NOT** submit PRs that alter licensing related files or headers. If you believe there's a problem with them, file an issue and we'll be happy to discuss it.
