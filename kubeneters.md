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
````
* minikube start
* minikube dashboard

``

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
 
* list exposed service and notice the he "pending" status
 kubectl get services

* get an ip for the service
  minikube service hello-node
  ``


## Sources

* https://bmuschko.github.io/gradle-docker-plugin/#spring_boot_application_plugin
* https://hub.docker.com/repository/docker/dtbwije/kubernetes
* https://kubernetes.io/docs/tutorials/hello-minikube/
* https://kubernetes.io/docs/concepts/overview/working-with-objects/kubernetes-objects/
* https://kubernetes.io/docs/concepts/architecture/

  "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#servicePrincipals/$entity",
  "accountEnabled": true,
  "addIns": [],
  "alternativeNames": [],
  "appDescription": null,
  "appDisplayName": "loginapp",
  "appId": "e82d58cd-970c-407d-90f6-6501f1c88f38",
  "appOwnerOrganizationId": "64084efa-2e8f-4960-a25f-87adcaea6f52",
  "appRoleAssignmentRequired": false,
  "appRoles": [],
  "applicationTemplateId": null,
  "createdByAppId": "04b07795-8ddb-461a-bbee-02f9e1bf7b46",
  "createdDateTime": null,
  "deletedDateTime": null,
  "description": null,
  "disabledByMicrosoftStatus": null,
  "displayName": "loginapp",
  "homepage": null,
  "id": "4450d339-06b6-438b-a371-3261b9174161",
  "info": {
    "logoUrl": null,
    "marketingUrl": null,
    "privacyStatementUrl": null,
    "supportUrl": null,
    "termsOfServiceUrl": null
  },
  "isDisabled": null,
  "keyCredentials": [],
  "loginUrl": null,
  "logoutUrl": null,
  "notes": null,
  "notificationEmailAddresses": [],
  "oauth2PermissionScopes": [],
  "passwordCredentials": [],
  "preferredSingleSignOnMode": null,
  "preferredTokenSigningKeyThumbprint": null,
  "replyUrls": [],
  "resourceSpecificApplicationPermissions": [],
  "samlSingleSignOnSettings": null,
  "servicePrincipalNames": [
    "e82d58cd-970c-407d-90f6-6501f1c88f38"
  ],
  "servicePrincipalType": "Application",
  "signInAudience": "AzureADMyOrg",
  "tags": [],
  "tokenEncryptionKeyId": null,
  "verifiedPublisher": {
    "addedDateTime": null,
    "displayName": null,
    "verifiedPublisherId": null
  }
}

az acr show -n learnstuff --query id -o tsv
az role assignment create --assignee e82d58cd-970c-407d-90f6-6501f1c88f38 --role AcrPush --scope "/subscriptions/1f681932-af34-40e4-be5a-858701c64d48/resourceGroups/loginappresources/providers/Microsoft.ContainerRegistry/registries/learnstuff"

$acrId = az acr show -n learnstuff --query id -o tsv
az role assignment create --subscription 1f681932-af34-40e4-be5a-858701c64d48 --assignee e82d58cd-970c-407d-90f6-6501f1c88f38 --role AcrPush --scope "/subscriptions/1f681932-af34-40e4-be5a-858701c64d48/resourceGroups/loginappresources/providers/Microsoft.ContainerRegistry/registries/learnstuff" 
$ MSYS_NO_PATHCONV=1 az role assignment create --assignee e82d58cd-970c-407d-90f6-6501f1c88f38 --role AcrPush --scope "/subscriptions/1f681932-af34-40e4-be5a-858701c64d48/resourceGroups/loginappresources/providers/Microsoft.ContainerRegistry/registries/learnstuff"