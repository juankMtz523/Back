package com.gtim.service_orders.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AzureStorageParam {
    private String defaultEndpointsProtocol = "https";
    private String accountName = "capexdevjc";
    private String accountKey = "Z8NaxREDqbZYCwfHJXBGRiCPIhpJpBrFN2ba4Uq2gkv6nCXmYuWtA7Xpo3cLE+Mlh8SkuMOy2TQk+ASt6wwIUw==";
    private String container = "somgtim";
    
    public String getStorageConnectionString(){
        String storageConnectionString = 
                String.format("DefaultEndpointsProtocol=%s;AccountName=%s;AccountKey=%s;EndpointSuffix=core.windows.net", 
                        defaultEndpointsProtocol, accountName, accountKey);
        return storageConnectionString;
    }    
    
}
