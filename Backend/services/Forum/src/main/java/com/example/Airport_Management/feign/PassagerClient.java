package com.example.Airport_Management.feign;

//import com.example.Airport_Management.passager.Passager;
import com.example.Airport_Management.forum.PassagerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "passager-service", url = "http://localhost:8091")
public interface PassagerClient {
    @GetMapping("/api/passager/{id}")
    PassagerDTO getPassagerById(@PathVariable("id") String id);
}
