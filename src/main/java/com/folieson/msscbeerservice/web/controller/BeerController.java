package com.folieson.msscbeerservice.web.controller;

import com.folieson.msscbeerservice.web.model.BeerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/api/v1/beer")
@RestController
public class BeerController {

  @GetMapping("/{beerId}")
  public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId) {
    //TODO: impl
    return ResponseEntity.status(HttpStatus.OK).body(BeerDto.builder().build());
  }

  @PostMapping
  public ResponseEntity<String> saveNewBeer(@Valid @RequestBody BeerDto beerDto) {
    //TODO: impl
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{beerId}")
  public ResponseEntity<String> updateBeerById(@PathVariable("beerId") UUID beerId, @Valid@RequestBody BeerDto beerDto) {
    //TODO: impl
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
