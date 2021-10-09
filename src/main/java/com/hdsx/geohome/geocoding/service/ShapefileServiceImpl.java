package com.hdsx.geohome.geocoding.service;


import com.hdsx.geohome.geocoding.api.IndexDao;
import com.hdsx.geohome.geocoding.api.ShapefileService;
import com.hdsx.geohome.geocoding.vo.Element;
import com.hdsx.toolkit.jts.JTSTools;
import com.hdsx.toolkit.number.NumberUtile;
import com.hdsx.toolkit.uuid.UUIDGenerator;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDirectoryFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jzh on 2016/9/22.
 */
@Service
public class ShapefileServiceImpl implements ShapefileService {

    private static Logger logger = LoggerFactory.getLogger(ShapefileServiceImpl.class);

    @Autowired
    private TaskExecutor taskExecutor;

    public DataStore getDataStore(String filepath){
        DataStore store = null;
        try{
            Map<String,Serializable> params = new HashMap<>();
            File file = new File(filepath);
            file.setReadOnly();
            params.put( "url", file.toURI().toURL() );
            params.put( "create spatial index", true );
            params.put( "memory mapped buffer", false );
            params.put( "charset", "GBK" );
            ShapefileDirectoryFactory factory = new ShapefileDirectoryFactory();
            store = factory.createDataStore(params);
        }catch (Exception e){
            logger.error("DataStore 加载失败{}",e.getCause());
            e.printStackTrace();
        }
        return store;
    }


    @Override
    public List<Element> read(String filepath) {
        DataStore store = getDataStore(filepath);
        List<Element> elementList = new ArrayList<>();
        try{
            String[] typeNames = store.getTypeNames();
            IndexDao indexDao = new IndexDaoImpl();
            for(int i = 0 ; i < typeNames.length ;i++){
                taskExecutor.execute(new IndexCallable(store,typeNames[i],indexDao));
            }
        }catch (Exception e){
            logger.error("shp资源加载失败{}",e.getCause());
            e.printStackTrace();
        }
        finally {
            try{
                if(store != null){
                    store.dispose();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return elementList;
    }

    public void convert(DataStore store, String typeName, List<Element> elements) throws Exception {
        SimpleFeatureSource featureSource = store.getFeatureSource(typeName);
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        SimpleFeatureIterator iterator = featureCollection.features();

        while (iterator.hasNext()) {
            Element element = new Element();
            SimpleFeature feature = iterator.next();
            Object code = feature.getAttribute("CODE");
            Object name = feature.getAttribute("NAME");
            Object order = feature.getAttribute("FORDER");
            Object address = feature.getAttribute("ADDRESS");
            Object table = feature.getAttribute("TYPE");
            Object district = feature.getAttribute("ADMINCODE");
            Geometry geometry = JTSTools.getInstance().toGeometry(feature.getDefaultGeometryProperty().getValue().toString());
            if (code != null) {
                element.setCode(code.toString());
            }
            if (name != null) {
                element.setName(name.toString());
            }
            if (order != null) {
                element.setOrder(NumberUtile.string2float(order.toString()));
            }
            if (table != null) {
                element.setTable(table.toString());
            }
            if (address != null) {
                element.setAddress(address.toString());
            }
            if (district != null) {
                element.setDistrict(district.toString());
            }
            if (geometry != null) {
                element.setGeometry(geometry);
            }
            element.setId(UUIDGenerator.randomUUID());
            elements.add(element);
        }
    }
}
