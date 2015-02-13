package eu.unifiedviews.plugins.loader.rdftockan;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openrdf.rio.UnsupportedRDFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unifiedviews.dataunit.DataUnit;
import eu.unifiedviews.dataunit.DataUnitException;
import eu.unifiedviews.dataunit.files.FilesDataUnit;
import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUContext;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dataunit.fileshelper.FilesHelper;
import eu.unifiedviews.helpers.dataunit.resourcehelper.Resource;
import eu.unifiedviews.helpers.dataunit.resourcehelper.ResourceConverter;
import eu.unifiedviews.helpers.dataunit.resourcehelper.ResourceHelpers;
import eu.unifiedviews.helpers.dataunit.virtualpathhelper.VirtualPathHelpers;
import eu.unifiedviews.helpers.dpu.config.AbstractConfigDialog;
import eu.unifiedviews.helpers.dpu.config.ConfigDialogProvider;
import eu.unifiedviews.helpers.dpu.config.ConfigurableBase;

@DPU.AsLoader
public class RdfToCkan extends ConfigurableBase<RdfToCkanConfig_V1> implements ConfigDialogProvider<RdfToCkanConfig_V1> {
    public static final String PROXY_API_ACTION = "action";

    public static final String PROXY_API_PIPELINE_ID = "pipeline_id";

    public static final String PROXY_API_USER_ID = "user_id";

    public static final String PROXY_API_TOKEN = "token";

    public static final String PROXY_API_TYPE = "type";

    public static final String PROXY_API_TYPE_FILE = "FILE";

    public static final String PROXY_API_TYPE_RDF = "RDF";

    public static final String PROXY_API_STORAGE_ID = "storage_id";

    public static final String PROXY_API_DATA = "data";

    public static final String PROXY_API_ATTACHMENT_NAME = "upload";

    public static final String CKAN_API_PACKAGE_SHOW = "package_show";

    public static final String CKAN_API_RESOURCE_UPDATE = "resource_update";

    public static final String CKAN_API_RESOURCE_CREATE = "resource_create";

    private static final Logger LOG = LoggerFactory.getLogger(RdfToCkan.class);

    @DataUnit.AsInput(name = "filesInput")
    public FilesDataUnit filesInput;

    public RdfToCkan() {
        super(RdfToCkanConfig_V1.class);
    }

    @Override
    public void execute(DPUContext dpuContext) throws DPUException, InterruptedException {
        String shortMessage = this.getClass().getSimpleName() + " starting.";
        String longMessage = String.valueOf(config);
        dpuContext.sendMessage(DPUContext.MessageType.INFO, shortMessage, longMessage);

        if (filesInput == null) {
            throw new DPUException("No input data unit for me, exiting");
        }

        CloseableHttpResponse response = null;
        Map<String, String> existingResources = new HashMap<>();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            URIBuilder uriBuilder;
            uriBuilder = new URIBuilder(config.getCatalogApiLocation());

            uriBuilder.setPath(uriBuilder.getPath());
            HttpPost httpPost = new HttpPost(uriBuilder.build().normalize());
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody(PROXY_API_ACTION, CKAN_API_PACKAGE_SHOW, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                    .addTextBody(PROXY_API_PIPELINE_ID, String.valueOf(config.getPipelineId()), ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                    .addTextBody(PROXY_API_USER_ID, config.getUserId(), ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                    .addTextBody(PROXY_API_TOKEN, config.getToken(), ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                    .build();
            httpPost.setEntity(entity);
            response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new DPUException("Could not obtain Dataset entity from CKAN. Request: " + EntityUtils.toString(entity) + " Response:" + EntityUtils.toString(response.getEntity()));
            }

            JsonReaderFactory readerFactory = Json.createReaderFactory(Collections.<String, Object> emptyMap());
            JsonReader reader = readerFactory.createReader(response.getEntity().getContent());
            JsonObject dataset = reader.readObject();
            JsonArray resources = dataset.getJsonArray("resources");
            for (JsonObject resource : resources.getValuesAs(JsonObject.class)) {
                existingResources.put(resource.getString("name"), resource.getString("id"));
            }
        } catch (URISyntaxException | IllegalStateException | IOException ex) {
            throw new DPUException("Cannot obtain dataset from CKAN", ex);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    LOG.warn("Error in close", ex);
                }
            }
        }

        JsonBuilderFactory factory = Json.createBuilderFactory(Collections.<String, Object> emptyMap());
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            Set<FilesDataUnit.Entry> files;
            try {
                files = FilesHelper.getFiles(filesInput);
            } catch (DataUnitException ex1) {
                throw new DPUException("Could not iterate files input", ex1);
            }
            for (FilesDataUnit.Entry file : files) {
                CloseableHttpResponse responseUpdate = null;
                try {
                    String storageId = VirtualPathHelpers.getVirtualPath(filesInput, file.getSymbolicName());
                    if (storageId == null || storageId.isEmpty()) {
                        storageId = file.getSymbolicName();
                    }
                    Resource resource = ResourceHelpers.getResource(filesInput, file.getSymbolicName());
                    resource.setName(storageId);
                    JsonObjectBuilder resourceBuilder = buildResource(factory, resource);
                    if (existingResources.containsKey(storageId)) {
                        resourceBuilder.add("id", existingResources.get(storageId));
                    }

                    URIBuilder uriBuilder = new URIBuilder(config.getCatalogApiLocation());
                    uriBuilder.setPath(uriBuilder.getPath());
                    HttpPost httpPost = new HttpPost(uriBuilder.build().normalize());
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                            .addTextBody(PROXY_API_PIPELINE_ID, String.valueOf(config.getPipelineId()), ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_USER_ID, config.getUserId(), ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_TOKEN, config.getToken(), ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_TYPE, PROXY_API_TYPE_FILE, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_STORAGE_ID, storageId, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_DATA, resourceBuilder.build().toString(), ContentType.APPLICATION_JSON.withCharset("UTF-8"));

                    if (existingResources.containsKey(storageId)) {
                        builder.addTextBody(PROXY_API_ACTION, CKAN_API_RESOURCE_UPDATE, ContentType.TEXT_PLAIN.withCharset("UTF-8"));
                    } else {
                        builder.addTextBody(PROXY_API_ACTION, CKAN_API_RESOURCE_CREATE, ContentType.TEXT_PLAIN.withCharset("UTF-8"));
                    }
                    builder.addBinaryBody(PROXY_API_ATTACHMENT_NAME, new File(URI.create(file.getFileURIString())), ContentType.DEFAULT_BINARY, storageId);
                    HttpEntity entity = builder.build();
                    httpPost.setEntity(entity);

                    responseUpdate = client.execute(httpPost);
                    if (responseUpdate.getStatusLine().getStatusCode() == 200) {
                        LOG.info("Response:" + EntityUtils.toString(responseUpdate.getEntity()));
                    } else {
                        LOG.error("Response:" + EntityUtils.toString(responseUpdate.getEntity()));
                    }
                } catch (UnsupportedRDFormatException | DataUnitException | IOException | URISyntaxException ex) {
                    throw new DPUException("Error exporting metadata", ex);
                } finally {
                    if (responseUpdate != null) {
                        try {
                            responseUpdate.close();
                        } catch (IOException ex) {
                            LOG.warn("Error in close", ex);
                        }
                    }
                }
            }
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                LOG.warn("Error in close", ex);
            }
        }
    }

    @Override
    public AbstractConfigDialog<RdfToCkanConfig_V1> getConfigurationDialog() {
        return new RdfToCkanVaadinDialog();
    }

    private JsonObjectBuilder buildResource(JsonBuilderFactory factory, Resource resource) {
        JsonObjectBuilder resourceExtrasBuilder = factory.createObjectBuilder();
        for (Map.Entry<String, String> mapEntry : ResourceConverter.extrasToMap(resource.getExtras()).entrySet()) {
            resourceExtrasBuilder.add(mapEntry.getKey(), mapEntry.getValue());
        }

        JsonObjectBuilder resourceBuilder = factory.createObjectBuilder();
        for (Map.Entry<String, String> mapEntry : ResourceConverter.resourceToMap(resource).entrySet()) {
            resourceBuilder.add(mapEntry.getKey(), mapEntry.getValue());
        }
        resourceBuilder.add("extras", resourceExtrasBuilder);

        return resourceBuilder;
    }

    public static String appendNumber(long number) {
        String value = String.valueOf(number);
        if (value.length() > 1) {
            // Check for special case: 11 - 13 are all "th".
            // So if the second to last digit is 1, it is "th".
            char secondToLastDigit = value.charAt(value.length() - 2);
            if (secondToLastDigit == '1') {
                return value + "th";
            }
        }
        char lastDigit = value.charAt(value.length() - 1);
        switch (lastDigit) {
            case '1':
                return value + "st";
            case '2':
                return value + "nd";
            case '3':
                return value + "rd";
            default:
                return value + "th";
        }
    }
}
