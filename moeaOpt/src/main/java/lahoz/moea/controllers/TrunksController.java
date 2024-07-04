package lahoz.moea.controllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lahoz.moea.infrastructure.dto.TrunkDTO;
import lahoz.moea.services.TrunkService;

@RestController
public class TrunksController {
    private final TrunkService service;

    public TrunksController(TrunkService service) {
        this.service = service;
    }

    @GetMapping(value = "trunks")
    public Page<TrunkDTO> getTrunks(
        @RequestParam(value = "orderField", defaultValue="id") String orderField,
        @RequestParam(value = "orderCriterial", defaultValue="DESC") String orderCriterial,
        @RequestParam(value = "page", defaultValue="0") Integer page,
        @RequestParam(value = "pageSize", defaultValue="30") Integer pageSize
    ) {
        return service.get(orderField, orderCriterial, page, pageSize);
    }

}