package com.ZerodaySolution.Billing.controller;

import com.ZerodaySolution.Billing.dto.AuthDTO;
import com.ZerodaySolution.Billing.dto.ProfileDTO;
import com.ZerodaySolution.Billing.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor

public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile( @RequestBody ProfileDTO profileDTO){

        ProfileDTO registeredProfile = profileService.registerProfile(profileDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO){
        try{

          Map<String,Object>  response = profileService.autheniticateAndGenerateToken(authDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));
        }


    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getUseProfile() {
        ProfileDTO profileDTO = profileService.getPublicProfile(null);
        return ResponseEntity.ok(profileDTO);


    }


    //testing jwt token
  /*  @GetMapping("/test")
    public String test(){
        return "testing successful";
    }

   */

}
