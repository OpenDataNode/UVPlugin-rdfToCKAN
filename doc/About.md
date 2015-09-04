### Description

Loads graphs metadata to the specified CKAN instance

### Configuration parameters

|Parameter|Description|
|:----|:----|
|**CKAN resource name** | Resource name to create in CKAN, this has precedence over input from e-distributionMetadata, and if even that is not set, it will use VirtualPath or symbolic name as resource name |

### Inputs and outputs

|Name |Type | DataUnit | Description | Mandatory |
|:--------|:------:|:------:|:-------------|:---------------------:|
|rdfInput          |i| RDFDataUnit | RDF graph/graphs, whose metadata should be loaded into CKAN |x|
|distributionInput |i| RDFDataUnit | Distribution metadata produced by e-distributionMetadata | |