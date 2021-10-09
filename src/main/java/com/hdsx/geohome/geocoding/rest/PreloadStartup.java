package com.hdsx.geohome.geocoding.rest;

import com.hdsx.geohome.geocoding.api.IndexDao;
import com.hdsx.geohome.geocoding.api.ShapefileService;
import com.hdsx.geohome.geocoding.vo.DIRECTORYTYPE;
import com.hdsx.geohome.geocoding.vo.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


/**
 * Created by jzh on 2017/7/4.
 */
@Service
@ConfigurationProperties(prefix="lucene")
public class PreloadStartup implements ApplicationListener<ContextRefreshedEvent> {

    private String filepath;

    private boolean mode;

    @Autowired
    private IndexDao indexDao;

    @Autowired
    private ShapefileService  shapefileService;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() != null){
            return;
        }
        new Runnable(){
            @Override
            public void run() {
                if(mode){
                    return;
                    //indexDao.deleteAll(DIRECTORYTYPE.FILE);
                }
                List<Element> elementList = shapefileService.read(filepath);
                try {
                    indexDao.save(elementList,"POI", DIRECTORYTYPE.FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("数据加载完毕.....");
            }
        }.run();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }
}

