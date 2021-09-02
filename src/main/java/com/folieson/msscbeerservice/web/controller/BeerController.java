package com.folieson.msscbeerservice.web.controller;

import com.folieson.msscbeerservice.repositories.BeerRepository;
import com.folieson.msscbeerservice.web.mappers.BeerMapper;
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
  private final BeerMapper beerMapper;
  private final BeerRepository beerRepository;

  @GetMapping("/{beerId}")
  public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId) {
    return beerRepository.findById(beerId)
        .map(beer -> ResponseEntity.status(HttpStatus.OK).body(beerMapper.beerToBeerDto(beer)))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PostMapping
  public ResponseEntity<String> saveNewBeer(@Validated @RequestBody BeerDto beerDto) {
    beerRepository.save(beerMapper.beerDtoToBeer(beerDto));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{beerId}")
  public ResponseEntity<String> updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {
    beerRepository.findById(beerId).ifPresent(beer -> {
      beer.setBeerName(beerDto.getBeerName());
      beer.setBeerStyle(beerDto.getBeerStyle().name());
      beer.setPrice(beerDto.getPrice());
      beer.setUpc(beerDto.getUpc());

      beerRepository.save(beer);
    });

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
