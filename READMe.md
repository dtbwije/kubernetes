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

## minikube commands
* minikube start
* minikube dashboard
* 

## kubectl commands

 ```
* create Deployment
 kubectl create deployment hello-node --image=dtbwije/kubernetes:1.0.3

* check deployment
 kubectl get pods
 kubectl get deployments
 
* check configuration
 kubectl config view
 
* expose the service to be accessible from outside 
 kubectl expose deployment hello-node --type=LoadBalancer --port=8080
 
* list exponsed service
 kubectl get services

* get an ip for the service
  minikube service hello-node
  ``


## Sources

* https://bmuschko.github.io/gradle-docker-plugin/#spring_boot_application_plugin
* https://hub.docker.com/repository/docker/dtbwije/kubernetes
* https://kubernetes.io/docs/tutorials/hello-minikube/
