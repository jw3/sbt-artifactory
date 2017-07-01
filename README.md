# SBT Artifactory Publisher #

### Configuration

The plugin can be configured through the environment or SBT setting keys

#### Environment
- `ARTIFACTORY_DEPLOY_HOST`: String; hostname of artifactory server
- `ARTIFACTORY_DEPLOY_USER`: String; username to deploy artifacts with
- `ARTIFACTORY_DEPLOY_PASS`: String; api token belonging to user specified with `ARTIFACTORY_DEPLOY_USER`
- `ARTIFACTORY_DEPLOY_TOKEN`: String; alias used for password if `ARTIFACTORY_DEPLOY_PASS` is not set

#### Setting Keys
- `hostname in Artifactory`: String; required
- `username in Artifactory`: String; optional
- `password in Artifactory`: String; optional

## Disclaimer ##

I dont yet know how to correctly write SBT plugins, so buyer beware.

## Bugs and Feedback

For bugs, questions, and discussions please use the [Github Issues](https://github.com/jw3/sbt-artifactory/issues).
