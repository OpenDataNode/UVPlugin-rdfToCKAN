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


|Parameter                        |Description                             |                                                        
|---------------------------------|----------------------------------------|
|**secret_token** |Token used to authenticate to CKAN, has to be set in backend.properties  |
|**catalogApiLocation** | URL where CKAN api is located, has to be set in backend.properties |

***

### Inputs and outputs ###

|Name                |Type       |DataUnit                         |Description                        |
|--------------------|-----------|---------------------------------|-----------------------------------|
|filesInput |i |FilesDataUnit |File loaded to specified CKAN instance  |

***

### Version history ###

|Version            |Release notes                                   |
|-------------------|------------------------------------------------|
|1.0.0-SNAPSHOT     |N/A                                             |
|1.0.1-SNAPSHOT     |Migration to DPU 2.0 helpers                    |                                


***

### Developer's notes ###

|Author            |Notes                 |
|------------------|----------------------|
|N/A               |N/A                   | 

