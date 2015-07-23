L-RdfToCkan
----------

### Documentation

Loads graphs metadata to the specified CKAN instance. |

### Dialog configuration parameters

|Parameter                        |Description                             |
|---------------------------------|----------------------------------------|
|**CKAN resource name** |Resource name to create in CKAN, this has precedence over input from e-distributionMetadata, and if even that is not set, it will use VirtualPath or symbolic name as resource name. |

### Configuration parameters
|Parameter                             |Description                             |
|--------------------------------------|----------------------------------------|
|**org.opendatanode.CKAN.secret.token**    |Token used to authenticate to CKAN, has to be set in backend.properties  |
|**org.opendatanode.CKAN.api.url** | URL where CKAN api is located, has to be set in backend.properties |
|**org.opendatanode.CKAN.http.header.[key]** | Custom HTTP header added to requests on CKAN |

#### Deprecated parameters

These parameters are deprecated and kept only for backward compatibility with version 1.0.X.
They will be removed in 1.1.0 of DPU.

|Parameter                             |Description                             |
|--------------------------------------|----------------------------------------|
|**dpu.uv-l-rdfToCkan.secret.token**    | alias to _org.opendatanode.CKAN.secret.token_  |
|**dpu.uv-l-rdfToCkan.catalog.api.url** | alias to _org.opendatanode.CKAN.api.url_ |

#### Examples
```INI
org.opendatanode.CKAN.secret.token = 12345678901234567890123456789012
org.opendatanode.CKAN.api.url = ﻿http://localhost:9080/internalcatalog/api/action/internal_api
org.opendatanode.CKAN.http.header.X-Forwarded-Host = www.myopendatanode.org
org.opendatanode.CKAN.http.header.X-Forwarded-Proto = https
```

### Inputs and outputs

|Name                |Type       |DataUnit                         |Description                        |
|--------------------|-----------|---------------------------------|-----------------------------------|
|filesInput |i |FilesDataUnit |File loaded to specified CKAN instance  |
|distributionInput |i (optional) |RDFDataUnit | Distribution metadata produced by e-distributionMetadata  |

### Version history

#### v1.1.1
* resource name configuration value added
* DPU sends resource format RDF to CKAN
* DPU code / artifact moved to org.opendatanode.plugins

#### v1.1.0
* Changes in DPU API v 2.1.0
* new actor ID parameter is sent to CKAN if available
* Input from e-distributionMetadata introduced

#### v1.0.2
* Added possibility to define custom HTTP headers and unification of config parameters

#### v1.0.1
* bug fixes and update in build dependencies

#### v1.0.0
* First release                                   |

### Developer's notes
