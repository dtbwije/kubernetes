```mermaid
sequenceDiagram
    autonumber
    actor Dev as Developer
    participant Repo as GitHub repo (main)
    participant Build as Actions: build job
    participant Push as Actions: push-image job
    participant Env as GitHub Environment<br/>(secrets and variables)
    participant Entra as Microsoft Entra ID<br/>(managed identity + federated credential)
    participant ACR as Azure Container Registry
    participant App as Azure App Service<br/>(Web App for Containers)

    Dev->>Repo: git push to main
    Repo->>Build: trigger workflow
    Build->>Build: gradlew build (compile and tests)
    Build-->>Push: success (needs: build)

    rect rgba(100, 150, 255, 0.12)
    Note over Push,Env: Secrets stay in GitHub, never in the repo
    Push->>Env: read AZURE_CLIENT_ID, AZURE_TENANT_ID,<br/>AZURE_SUBSCRIPTION_ID (secrets) and ACR_NAME (variable)
    Env-->>Push: values injected at run time
    end

    rect rgba(100, 200, 150, 0.12)
    Note over Push,Entra: Passwordless login with OIDC
    Push->>Entra: azure/login presents GitHub OIDC token<br/>subject: repo:OWNER/REPO:environment:ENV_NAME
    Entra->>Entra: match federated credential on the managed identity
    Entra-->>Push: short-lived Azure access token
    end

    rect rgba(255, 180, 100, 0.12)
    Note over Push,ACR: Build and publish the image
    Push->>ACR: az acr login
    Push->>Push: docker build (multi-stage Dockerfile)
    Push->>ACR: docker push with tags GIT_SHA and latest<br/>(identity has AcrPush on the registry)
    ACR-->>Push: image stored
    end

    Note over App,ACR: Deployment (configured in Azure, not by this pipeline yet)
    App->>ACR: pull image (identity has AcrPull)
    Note right of App: App settings supply the database<br/>and Event Hubs configuration
```

- Create the github actions file in the .github/workflow as a yaml file
- in order to create the azure integration we need following information declared as a environment. It is possible to set these values without an environment but it is more onvenient when we need to deploy to multiple environments. 
 
 Secrets 

 - Client Id
 - tennanet Id
 - Subscription id

 variables
 - ACR name
 - repositoru name

 From azure side

 - Create a application in the application registry
 - Assign a AcrPush Role in the application on the container registry
 

 When running console commands use the following prefix so that it would rewrite the windows path as a prefix
 ```bash
 export MSYS_NO_PATHCONV=1
 ```
 ## References 
 - https://github.com/Azure/login#login-with-a-service-principal-secret

 ## Commands
```bash
- az acr show -n learnstuff --query id -o tsv
- az role assignment create --assignee e82d58cd-970c-407d-90f6-6501f1c88f38 --role AcrPush --scope "/subscriptions/1f681932-af34-40e4-be5a-858701c64d48/resourceGroups/loginappresources/providers/Microsoft.ContainerRegistry/registries/learnstuff"

- $acrId = az acr show -n learnstuff --query id -o tsv
- az role assignment create --subscription 1f681932-af34-40e4-be5a-858701c64d48 --assignee e82d58cd-970c-407d-90f6-6501f1c88f38 --role AcrPush --scope "/subscriptions/1f681932-af34-40e4-be5a-858701c64d48/resourceGroups/loginappresources/providers/Microsoft.ContainerRegistry/registries/learnstuff" 
- $ MSYS_NO_PATHCONV=1 az role assignment create --assignee e82d58cd-970c-407d-90f6-6501f1c88f38 --role AcrPush --scope "/subscriptions/1f681932-af34-40e4-be5a-858701c64d48/resourceGroups/loginappresources/providers/Microsoft.ContainerRegistry/registries/learnstuff"
- https://api.github.com/users/dtbwije
- https://api.github.com/repos/dtbwije/kubernetes

- az role assignment list --assignee <client-id-of-appuserassiggned1> --scope (az acr show -n learnstuff --query id -o tsv) -o table

```



