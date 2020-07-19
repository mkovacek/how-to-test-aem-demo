# About

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/26149e8228d5439f9689b05fb4f84ddc)](https://app.codacy.com/manual/kc.kb.matija/how-to-test-aem-demo?utm_source=github.com&utm_medium=referral&utm_content=mkovacek/how-to-test-aem-demo&utm_campaign=Badge_Grade_Dashboard)

[Test behaviour, not implementation](https://devz.life/blog/test-behaviour-not-implementation/)

Example based on Product details API implementation in AEM

AEM 6.5, Junit 5 & AEM Mocks

# Sample AEM project template

This is a project template for AEM-based applications. It is intended as a best-practice set of examples as well as a potential starting point to develop your own functionality.

## Modules

The main parts of the template are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, templates, runmode specific configs as well as Hobbes-tests
* ui.content: contains sample content using the components from the ui.apps

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

## Testing

There are three levels of testing contained in the project:

* unit test in core: this show-cases classic unit testing of the code contained in the bundle. To test, execute:

    mvn clean test

## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html
