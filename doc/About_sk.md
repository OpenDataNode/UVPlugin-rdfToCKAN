### Popis

Pekný slovenský popis

### Konfiguračné parametre dialógu

| Name | Description |
|:----|:----|
|**Názov zdroja CKAN** |Názov zdroja vytvoreného v CKAN, má prednosť pred vstupom z e-distributionMetadata, a aj v prípade, ak nie je zadaný, použije virtuálnu cestu alebo symbolické meno ako názov zdroja|

### Konfiguračné parametre

| Meno | Popis |
|:----|:----|
|**org.opendatanode.CKAN.secret.token**|Reťazec použitý pre autentifikáciu do CKAN, nastavuje sa v backend.properties  |
|**org.opendatanode.CKAN.api.url** | URL kde sa nachádza CKAN API, nastavuje sa v backend.properties |
|**org.opendatanode.CKAN.http.header.[key]** | Aktuálna HTTP hlavička pridávaná k požiadavkam na CKAN |

#### Zastarané parametre

Nasledujúce parametre sú zastarané a uchované iba z dôvodu spätnej kompatibility s verziou 1.0.X.
Budú odstránené od verzie DPU 1.1.0.

| Meno | Popis |
|-----|-----|
|**dpu.uv-l-filesToCkan.secret.token**| alias k _org.opendatanode.CKAN.secret.token_  |
|**dpu.uv-l-filesToCkan.catalog.api.url** | alias k _org.opendatanode.CKAN.api.url_ |

#### Príklady
```INI
org.opendatanode.CKAN.secret.token = 12345678901234567890123456789012
org.opendatanode.CKAN.api.url = ﻿http://localhost:9080/internalcatalog/api/action/internal_api
org.opendatanode.CKAN.http.header.X-Forwarded-Host = www.myopendatanode.org
org.opendatanode.CKAN.http.header.X-Forwarded-Proto = https
```

### Vstupy a výstupy ###

|Meno |Typ | Dátová hrana | Popis | Povinné |
|:--------|:------:|:------:|:-------------|:---------------------:|
|filesInput|i|FilesDataUnit |Súbor nahraný do príslušnej CKAN inštancie|x|
|distributionInput|i(optional)|RDFDataUnit| Distribučné metadáta vytvorené od e-distributionMetadata||