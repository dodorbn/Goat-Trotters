package goat.trotters.controller;

import goat.trotters.model.Response;
import goat.trotters.repository.ResponseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reponses")
@CrossOrigin(origins = "*") // permet au frontend Vue de se connecter
public class ResponseController {

    private final ResponseRepository repository;

    public ResponseController(ResponseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Response> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Response create(@RequestBody Response response) {
        return repository.save(response);
    }
}
