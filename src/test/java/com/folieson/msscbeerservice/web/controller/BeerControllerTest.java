package com.folieson.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.folieson.msscbeerservice.domain.Beer;
import com.folieson.msscbeerservice.repositories.BeerRepository;
import com.folieson.msscbeerservice.web.model.BeerDto;
import com.folieson.msscbeerservice.web.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.folieson.msscbeerservice.web.mappers")
class BeerControllerTest {

  private static final String URL = "/api/v1/beer/";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private BeerRepository beerRepository;

  @Test
  void getBeerById() throws Exception {
    given(beerRepository.findById(any())).willReturn(Optional.of(Beer.builder().build()));
    mockMvc.perform(get(URL + UUID.randomUUID()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void saveNewBeer() throws Exception {
    BeerDto beerDto = getValidBeerDto();
    String beerDtoJson = objectMapper.writeValueAsString(beerDto);

    mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(beerDtoJson))
        .andExpect(status().isCreated());
  }

  @Test
  void updateBeerById() throws Exception {
    BeerDto beerDto = getValidBeerDto();
    String beerDtoJson = objectMapper.writeValueAsString(beerDto);

    mockMvc.perform(put(URL + UUID.randomUUID())
        .contentType(MediaType.APPLICATION_JSON)
        .content(beerDtoJson))
        .andExpect(status().isNoContent());
  }

  BeerDto getValidBeerDto() {
    return BeerDto.builder()
        .beerName("My Beer")
        .beerStyle(BeerStyle.ALE)
        .price(new BigDecimal("2.99"))
        .upc(123123123123L)
        .build();
  }
}