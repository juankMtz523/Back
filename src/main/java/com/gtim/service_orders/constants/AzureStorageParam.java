package com.gtim.service_orders.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AzureStorageParam {
    private String defaultEndpointsProtocol = "";
    private String accountName = "";
    private String accountKey = "";
    private String container = "";
    
    public String getStorageConnectionString(){
        String storageConnectionString = 
                String.format("DefaultEndpointsProtocol=%s;AccountName=%s;AccountKey=%s;EndpointSuffix=core.windows.net", 
                        defaultEndpointsProtocol, accountName, accountKey);
        return storageConnectionString;
    }    
    
}
