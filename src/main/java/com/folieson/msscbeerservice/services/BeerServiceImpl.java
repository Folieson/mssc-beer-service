package com.folieson.msscbeerservice.services;

import com.folieson.msscbeerservice.domain.Beer;
import com.folieson.msscbeerservice.web.controller.NotFoundException;
import com.folieson.msscbeerservice.repositories.BeerRepository;
import com.folieson.msscbeerservice.web.mappers.BeerMapper;
import com.folieson.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
  private final BeerMapper beerMapper;
  private final BeerRepository beerRepository;

  @Override
  public BeerDto getById(UUID beerId) {
    return beerRepository.findById(beerId)
        .map(beerMapper::beerToBeerDto).orElseThrow(NotFoundException::new);
  }

  @Override
  public BeerDto saveNewBeer(BeerDto beerDto) {
    return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
  }

  @Override
  public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
    Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

    beer.setBeerName(beerDto.getBeerName());
    beer.setBeerStyle(beerDto.getBeerStyle().name());
    beer.setPrice(beerDto.getPrice());
    beer.setUpc(beerDto.getUpc());

    return beerMapper.beerToBeerDto(beerRepository.save(beer));
  }
}
