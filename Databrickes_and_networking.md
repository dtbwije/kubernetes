# Databrickes and networking

## Plan for the databricks Network

Resource Group
│
├── Virtual Network
│   ├── Databricks Subnet
│   └── Private Endpoint Subnet
│
├── NSG
│
├── ADLS Gen2
│   └── data container
│
├── Private Endpoint (blob)
├── Private Endpoint (dfs)
│
├── Private DNS Zones
│
├── Databricks Workspace
│
└── Managed Identity / Access Connector

## Network diagram

                VNet 10.0.0.0/16
                      |
+---------------------+---------------------+
|                                           |
|                                           |
SubnetDatabricks                SubnetPrivateEndpoints
10.0.1.0/24                             10.0.2.0/27
|                                           |
|                                           |
Azure Databricks                    Private Endpoint
                                            |
                                            |
                                    ADLS Gen2 Storage
                                    Public Access Disabled

## Challenges

- Having the network private makes it a it difficult to upload the data to the container. We need to follow follow steps

## Learning plan

### Day 1

VNet
ADLS Gen2
Private Endpoints
Private DNS
Test connectivity

### Day 2

Databricks
Managed Identity
Unity Catalog
Read CSV from storage
Write results back to storage
Disable public network access
Verify everything still works

That would be a very realistic Azure data-platform exercise and something worth discussing in a senior-level review.

 ### Some Commands

 show storage credintials
 describe storage credintial