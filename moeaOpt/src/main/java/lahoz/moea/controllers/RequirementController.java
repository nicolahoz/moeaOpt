package lahoz.moea.controllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lahoz.moea.infrastructure.dto.RequirementDTO;
import lahoz.moea.services.RequirementsService;

@RestController
public class RequirementController {
    private final RequirementsService service;

    public RequirementController(RequirementsService service) {
        this.service = service;
    }

    @GetMapping(value = "requirements")
    public Page<RequirementDTO> getRequirements(
        @RequestParam(value = "orderField", defaultValue="id") String orderField,
        @RequestParam(value = "orderCriterial", defaultValue="DESC") String orderCriterial,
        @RequestParam(value = "page", defaultValue="0") Integer page,
        @RequestParam(value = "pageSize", defaultValue="30") Integer pageSize
    ) {
        return service.get(orderField, orderCriterial, page, pageSize);
    }

}