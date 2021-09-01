package com.folieson.msscbeerservice.web.mappers;

import com.folieson.msscbeerservice.domain.Beer;
import com.folieson.msscbeerservice.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {
  BeerDto beerToBeerDto(Beer beer);
  Beer beerDtoToBeer(BeerDto beerDto);
}
