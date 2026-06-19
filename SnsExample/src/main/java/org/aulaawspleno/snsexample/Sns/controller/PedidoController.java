package org.aulaawspleno.snsexample.Sns.controller;

import org.aulaawspleno.snsexample.Sns.model.Product;
import org.aulaawspleno.snsexample.Sns.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sns")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Product product) {
        return ResponseEntity.ok(service.publish(product));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestParam String email) {
        return ResponseEntity.ok(service.subscribeEmail(email));
    }

    @PostMapping("/subscribe-app")
    public ResponseEntity<String> subscribeApp(@RequestParam String endpointUrl) {
        return ResponseEntity.ok(service.subscribeHttp(endpointUrl));
    }
}
