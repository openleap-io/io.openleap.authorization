# Authorization service

Part of IAM's core domain. Authorization domain. Service that provides implementations of the OAuth 2.1 and OpenID
Connect 1.0 specifications and other related specifications. It is built on top of Spring Security to provide a secure,
light-weight, and customizable foundation for building OpenID Connect 1.0 Identity Providers and OAuth2 Authorization
Server products.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes.

## Prerequisites

* [Maven](https://maven.apache.org/) is used as a build tool. Install it and set MAVEN_HOME to your system variables
* [Java 21](http://www.oracle.com) is present. Install it and set JAVA_HOME to your system variables
* [GIT](https://git-scm.com/) as a source distributed version control system
* [Docker](https://www.docker.com/)

### Build and Run Locally

#### checkout

Clone the project locally

```
git clone git@github.com:kirilarsov470/io.openleap.iam.git
```

#### build

```
mvn clean install
```

to skip the tests from the build use

```
mvn clean install -DskipTests
```

#### run

```
mvn spring-boot:run
```

## Resources

[![Build status](https://github.com/openleap-io/io.openleap.config/actions/workflows/main-build.yml/badge.svg)](https://github.com/openleap-io/io.openleap.config/actions/workflows/main-build.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Maven central](https://img.shields.io/maven-central/v/org.openwms/io.openleap.config)](https://search.maven.org/search?q=a:io.openleap.config)
[![Docker pulls](https://img.shields.io/docker/pulls/openleap/openleap-config)](https://hub.docker.com/r/openleap/openleap-config)