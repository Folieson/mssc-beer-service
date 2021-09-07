package com.folieson.msscbeerservice.web.controller;

import com.folieson.msscbeerservice.services.BeerService;
import com.folieson.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/beer")
@RestController
public class BeerController {
  private final BeerService beerService;

  @GetMapping("/{beerId}")
  public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId) {
    return ResponseEntity.status(HttpStatus.OK).body(beerService.getById(beerId));
  }

  @PostMapping
  public ResponseEntity<BeerDto> saveNewBeer(@Validated @RequestBody BeerDto beerDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(beerService.saveNewBeer(beerDto));
  }

  @PutMapping("/{beerId}")
  public ResponseEntity<BeerDto> updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(beerService.updateBeer(beerId, beerDto));
  }
}
