# How to contribute

Please contribute to this repository if any of the following is true:

* You have expertise in community development, communication, or education
* You want open source communities to be more collaborative and inclusive
* You want to help lower the burden to first time contributors

### Issues

Before you submit your issue search the archive, maybe your question was already answered.

Because we create a generator you should be specific as possible in your requests. Providing the following information will increase the chances of your issue being dealt with quickly:
* Any sample code **SHOULD BE** included in [code blocks](https://help.github.com/articles/github-flavored-markdown/#fenced-code-blocks)
* **Overview of the Issue** - if an error is being thrown a non-minified stack trace helps
* **Motivation for or Use Case** - explain why this is a bug for you
* **Browsers and Operating System** - is this a problem with all browsers or only IE8?
* **Reproduce the Error** - provide a live example (using [Plunker](http://plnkr.co/edit/) or
  [JSFiddle](http://jsfiddle.net/)) or a unambiguous set of steps.
* **Related Issues** - has a similar issue been reported before?
* **Suggest a Fix** - if you can't fix the bug yourself, perhaps you can point to what might be
  causing the problem (line of code or commit)

### Pull requests

Before you submit your pull request consider the following guidelines:

**Test**
Please add unit tests for every new feature or bug fix and make sure all tests pass before submitting pull requests. The tests are written in [Mocha](http://mochajs.org).  [Karma](http://karma-runner.github.io/0.12/index.html) and [Protractor](http://angular.github.io/protractor) are used to run unit tests and e2e tests on the application.
* Run `./node_modules/mocha/bin/_mocha ./test/*.js` to execute all tests instead of `npm test`. Currently all protractor tests in (2) are excluded from `npm test` due to Travis issue.
* Feel free to create new test file for new features.

**Style Guide**
Please brief yourself on [Idiomatic.js](https://github.com/rwldrn/idiomatic.js) and [Google JS](http://google.github.io/styleguide/javascriptguide.xml) style guide with `four` space indent.

**Documentation**
Add documentation for every new feature, directory structure change. Feel free to send corrections or better docs!

**Branch**
Must be one of the following:

* feat: A new feature
* fix: A bug fix
* docs: Documentation only changes
* style: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
* refactor: A code change that neither fixes a bug or adds a feature
* perf: A code change that improves performance
* test: Adding missing tests
* chore: Changes to the build process or auxiliary tools and libraries such as documentation generation

### Donations

If you like this project, if you want a faster development, if you feel generous, donation is a great way to motivate us more.

[![Gratipay](http://img.shields.io/gratipay/Swiip.svg?style=flat)]()
[![Paypal](http://img.shields.io/badge/paypal-donate-yellow.svg?style=flat)]()