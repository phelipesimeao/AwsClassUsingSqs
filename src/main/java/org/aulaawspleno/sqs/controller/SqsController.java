package org.aulaawspleno.sqs.controller;

import org.aulaawspleno.sqs.model.User;
import org.aulaawspleno.sqs.service.SqsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sqs")
public class SqsController {
    private final SqsService service;

    public SqsController(SqsService service) {
        this.service = service;
    }

    @PostMapping("/send-use")
    public ResponseEntity<String> send(@RequestBody User user) {
        return ResponseEntity.ok(service.sendUser(user));
    }

    @GetMapping("/process")
    public ResponseEntity<List<Map<String, String>>> process() {
        return ResponseEntity.ok(service.receiveAndProcess());
    }

    @GetMapping("/peek")
    public ResponseEntity<List<String>> peek() {
        return ResponseEntity.ok(service.peekMessages());
    }
}
