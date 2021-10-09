package com.hdsx.geohome.geocoding.service;


import com.hdsx.geohome.geocoding.api.IndexDao;
import com.hdsx.geohome.geocoding.vo.DIRECTORYTYPE;
import com.hdsx.geohome.geocoding.vo.Element;
import com.hdsx.toolkit.number.NumberUtile;
import com.hdsx.toolkit.uuid.UUIDGenerator;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jzh on 2016/10/22.
 */
public class IndexCallable implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(IndexCallable.class);
    private IndexDao indexDao;
    private String typeName;
    private DataStore store;
    private SimpleFeatureSource featureSource;
    private SimpleFeatureCollection featureCollection;

    public IndexCallable(DataStore store, String typeName, IndexDao indexDao)
    {
        this.store = store;
        this.typeName = typeName;
        this.indexDao = indexDao;
        try {
            this.featureSource = store.getFeatureSource(typeName);
            this.featureCollection = this.featureSource.getFeatures();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        SimpleFeatureIterator iterator = this.featureCollection.features();
        List elements = new ArrayList(this.featureCollection.size());

        WKTReader wktReader = new WKTReader();
        while (iterator.hasNext()) {
            Element element = new Element();
            SimpleFeature feature = iterator.next();
            Object code = feature.getAttribute("AREA_CODE");
            Object name = feature.getAttribute("CORP_NAME");
            Object address = feature.getAttribute("ADRESS");
            
            Object fr= feature.getAttribute("FR");
            Object tel = feature.getAttribute("TEL");
            Object cusccode = feature.getAttribute("CUSC_CODE");  
            Object iszdy = feature.getAttribute("IS_ZDY");
            Object isxkz= feature.getAttribute("IS_XKZ");
            Object isonline = feature.getAttribute("IS_ONLINE");
            Object corptype = feature.getAttribute("CORP_TYPE");
            
            Object order = feature.getAttribute("order");
            Object table = feature.getAttribute("table");
            Object district = feature.getAttribute("ADMINCODE0");
            try {
                if ((feature.getDefaultGeometry() instanceof Point)) {
                    Geometry geometry = wktReader.read(feature.getDefaultGeometryProperty().getValue().toString());
                    if (geometry != null)
                        element.setGeometry(geometry);
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
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
            
            
           
          
            if (fr != null) {
            	element.setFr(fr.toString());
            }
            if (tel != null) {
            	element.setTel(tel.toString());
            }
            if (cusccode != null) {
            	element.setCusccode(cusccode.toString());
            }
            if (iszdy != null) {
            	element.setIszdy(Double.valueOf(iszdy.toString()));
            }
            if (isxkz != null) {
            	element.setIsxkz(Double.valueOf(isxkz.toString()));
            }
            
            if (isonline != null) {
            	element.setIsonline(Double.valueOf(isonline.toString()));
            }
            if (corptype != null) {
            	element.setCorptype(Double.valueOf(corptype.toString()) );
            }
            
            Object oid = feature.getAttribute("OBJECTID");
            if(oid!=null) {
                element.setId(oid.toString());
            }else{
                element.setId(UUIDGenerator.randomUUID());
            }
            elements.add(element);
        }
        try {
            this.indexDao.save(elements, this.typeName, DIRECTORYTYPE.FILE);
        } catch (IOException e) {
            logger.info("{}加载失败，数据总计:{}", this.typeName);
            e.printStackTrace();
        }
        logger.info("{}加载完毕，数据总计:{}", this.typeName, Integer.valueOf(this.featureCollection.size()));
    }
}
