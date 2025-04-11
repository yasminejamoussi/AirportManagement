package com.example.Airport_Management.feign;

import com.example.Airport_Management.livraisonbagages.ObjetPerduDTO;
import com.example.Airport_Management.passager.Passager;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "objetperdu-service",url = "http://localhost:8081")
public interface ObjetPerduClient {
    @GetMapping("/objetperdu/{id}")
    ObjetPerduDTO getObjetPerduById(@PathVariable("id") int id);

}
