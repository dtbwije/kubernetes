Kubenetes
--
This is repo has a simple spring boot app done in kotlin which we use to learn kubernetes.

### How to build

```
gradle bootRun
```

### How to build the jar

```
gradle bootJar
```

### How to build the docker image

```
gradle clean build dockerBuildImage
```

### How to build the docker push

```
gradle clean build dockerBuildImage dockerPushImage
```


## Sources

* https://bmuschko.github.io/gradle-docker-plugin/#spring_boot_application_plugin
* https://hub.docker.com/repository/docker/dtbwije/kubernetes
