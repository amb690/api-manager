package com.markettrender.apimanager.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.markettrender.apimanager.model.entity.Api;
import com.markettrender.apimanager.service.ApiService;

//@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/apis")
public class ApiRestController {

	@Autowired
	public ApiService apiService;

	@GetMapping("")
	public List<Api> index() {
		return apiService.findAll();
	}

	@GetMapping("/{name}")
	public ResponseEntity<?> show(@PathVariable String name) {

		Api api = null;
		Map<String, Object> response = new HashMap<>();
		try {
			api = apiService.findByName(name);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (api == null) {
			response.put("mensaje", "La API ".concat(name).concat(" no existe en la base de datos"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(api, HttpStatus.OK);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Api api, BindingResult result) {

		Api apiNew = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			apiNew = apiService.save(api);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la inserción en la base de datos!");

			if (null != e.getMessage()) {
				String errMessage = e.getMessage();
				response.put("error", errMessage.concat(": ").concat(e.getMostSpecificCause().getMessage()));
			}

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La api ha sido creada con éxito!");
		response.put("api", apiNew);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Api api, BindingResult result, @PathVariable Long id) {

		Api apiActual = apiService.findById(id);
		Api apiUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (apiActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el cliente ID: ".concat(id.toString())
					.concat(" no existe en la base de datos!"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			apiActual.setName(api.getName());
			apiActual.setMaxPetitions(api.getMaxPetitions());
			apiService.save(apiActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la actualización en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente ha sido actualizado con éxito!");
		response.put("cliente", apiUpdated);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/petitions/{id}")
	public ResponseEntity<?> updateNumberOfPetitions(@PathVariable Long id,
			@RequestParam(name = "numberOfPetitions", required = true) Long numberOfPetitions) {

		Api apiActual = apiService.findById(id);
		Api apiUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if (apiActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el cliente ID: ".concat(id.toString())
					.concat(" no existe en la base de datos!"));
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		try {
			apiActual.setNumberOfPetitions(numberOfPetitions);
			apiActual.setLastPetition(new Date());

			apiUpdated = apiService.save(apiActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la actualización en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente ha sido actualizado con éxito!");
		response.put("cliente", apiUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			apiService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el cliente en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente eliminado con éxito!");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
