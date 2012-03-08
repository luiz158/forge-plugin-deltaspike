package org.apache.deltaspike.forge.resource;

import com.google.common.collect.Sets;
import org.jboss.forge.project.services.ResourceFactory;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceHandles;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 *
 */
@ResourceHandles(value = "*.properties")
public class PropertiesResource extends FileResource<PropertiesResource> {

    private Properties properties;
    
    @Inject
    public PropertiesResource(final ResourceFactory factory)
    {
        super(factory, null);
    }

    public PropertiesResource(ResourceFactory factory, File file) {
        super(factory, file);
        loadProperties(file); 
    }

    private void loadProperties(File someFile) {
        if (someFile.exists()) {
            properties = new Properties();
            try {
                FileInputStream stream = new FileInputStream(someFile);
                properties.load(stream);
                stream.close();
            } catch (IOException e) {
                // FIXME
                e.printStackTrace();
            }
        }
    }

    public boolean containsKey(String keyValue) {
        return properties.keySet().contains(keyValue);
    }
    
    public Set<String> keyValues() {
        // Don't want to use 1.6 functionality here (yet)
        Set<String> result = Sets.newHashSet();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            result.add((String) enumeration.nextElement());
        }
        return result;
    }
    
    public String getValue(String key) {
        return properties.getProperty(key);
    }
    
    public void setValue(String key, String value) {
        properties.setProperty(key, value);
        storeProperties();
    }

    private void storeProperties() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            properties.store(stream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContents(stream.toString());

    }

    @Override
    public PropertiesResource createFrom(File file) {
        return new PropertiesResource(resourceFactory, file);
    }

    @Override
    public List<Resource<?>> listResources() {
       return Collections.emptyList();
    }


}
