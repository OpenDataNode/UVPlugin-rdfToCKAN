### Popis

Nahrá metadáta o grafoch do CKAN inštancie

### Konfiguračné parametre

| Meno | Popis |
|:----|:----|
|**Názov zdroja CKAN** | Názov zdroja vytvoreného v CKAN, má prednosť pred vstupom z e-distributionMetadata, a aj v prípade, ak nie je zadaný, použije virtuálnu cestu alebo symbolické meno ako názov zdroja|

### Vstupy a výstupy ###

|Meno |Typ | Dátová hrana | Popis | Povinné |
|:--------|:------:|:------:|:-------------|:---------------------:|
|rdfInput          |vstup| RDFDataUnit | RDF graf/grafy, ktorých metadáta sa majú nahrať do príslušnej CKAN inštancie |áno|
|distributionInput |vstup| RDFDataUnit | Distribučné metadáta vytvorené od e-distributionMetadata | |