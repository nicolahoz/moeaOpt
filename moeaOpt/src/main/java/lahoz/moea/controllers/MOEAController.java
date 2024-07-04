package lahoz.moea.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lahoz.moea.services.MOEAService;

@RestController
public class MOEAController {
    private final MOEAService service;

    public MOEAController(MOEAService service) {
        this.service = service;
    }

    @GetMapping(path = "moea/test", produces = "application/json")
	public String test(){
        this.init();
        return this.run();
	}

    @GetMapping("moea/init")
    public void init() {
        service.initializeStops();
        service.initializeRequirements();
        service.initializeTrunks();
        service.initializeFrames();
    }

    @GetMapping(path = "moea/run", produces = "application/json")
    public String run() {
        try{
            return service.runOptimization();
        }catch (InterruptedException e){
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\" }";
        }
    }
}