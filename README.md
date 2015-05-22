# L-RdfToCkan #
----------

###General###

|                              |                                                               |
|------------------------------|---------------------------------------------------------------|
|**Name:**                     |L-RdfToCkan                                             |
|**Description:**              |Loads graphs metadata to the specified CKAN instance. |
|                              |                                                               |
|**DPU class name:**           |RdfToCkan     |
|**Configuration class name:** |RdfToCkanConfig_V1                           |
|**Dialogue class name:**      |RdfToCkanVaadinDialog |

***

###Configuration parameters###
|Parameter                             |Description                             |
|--------------------------------------|----------------------------------------|
|**org.opendatanode.CKAN.secret.token**    |Token used to authenticate to CKAN, has to be set in backend.properties  |
|**org.opendatanode.CKAN.api.url** | URL where CKAN api is located, has to be set in backend.properties |
|**org.opendatanode.CKAN.http.header.[key]** | Custom HTTP header added to requests on CKAN |

####Deprecated parameters###

These parameters are deprecated and kept only for backward compatibility with version 1.0.X.
They will be removed in 1.1.0 of DPU.

|Parameter                             |Description                             |
|--------------------------------------|----------------------------------------|
|**dpu.uv-l-rdfToCkan.secret.token**    | alias to _org.opendatanode.CKAN.secret.token_  |
|**dpu.uv-l-rdfToCkan.catalog.api.url** | alias to _org.opendatanode.CKAN.api.url_ |
|**dpu.uv-l-rdfToCkan.http.header.[key]** | alias to _org.opendatanode.CKAN.http.header.[key]_ |


####Examples####
```INI
org.opendatanode.CKAN.secret.token = 12345678901234567890123456789012
org.opendatanode.CKAN.api.url = ﻿http://localhost:9080/internalcatalog/api/action/internal_api
```

***

### Inputs and outputs ###

|Name                |Type       |DataUnit                         |Description                        |
|--------------------|-----------|---------------------------------|-----------------------------------|
|filesInput |i |FilesDataUnit |File loaded to specified CKAN instance  |

***

### Version history ###

|Version            |Release notes                                   |
|-------------------|------------------------------------------------|
|1.0.2              | unification of config parameters |
|1.0.1              | bug fixes and update in build dependencies |
|1.0.0              | First release                                   |


***

### Developer's notes ###

|Author            |Notes                 |
|------------------|----------------------|
|N/A               |N/A                   |

