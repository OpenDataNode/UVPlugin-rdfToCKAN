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
|**dpu.uv-l-rdfToCkan.secret.token**    |Token used to authenticate to CKAN, has to be set in backend.properties  |
|**dpu.uv-l-rdfToCkan.catalog.api.url** | URL where CKAN api is located, has to be set in backend.properties |

***

### Inputs and outputs ###

|Name                |Type       |DataUnit                         |Description                        |
|--------------------|-----------|---------------------------------|-----------------------------------|
|filesInput |i |FilesDataUnit |File loaded to specified CKAN instance  |

***

### Version history ###

|Version            |Release notes                                   |
|-------------------|------------------------------------------------|
|1.1.0              | Changes in DPU API v 2.1.0, new actor ID parameter is sent to CKAN if available |
|1.0.1              | bug fixes and update in build dependencies |
|1.0.0              |First release                                   |


***

### Developer's notes ###

|Author            |Notes                 |
|------------------|----------------------|
|N/A               |N/A                   |

