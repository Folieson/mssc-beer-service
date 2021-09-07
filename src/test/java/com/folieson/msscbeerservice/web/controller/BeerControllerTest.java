package com.folieson.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.folieson.msscbeerservice.domain.Beer;
import com.folieson.msscbeerservice.repositories.BeerRepository;
import com.folieson.msscbeerservice.services.BeerService;
import com.folieson.msscbeerservice.web.model.BeerDto;
import com.folieson.msscbeerservice.web.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.folieson.com", uriPort = 80)
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.folieson.msscbeerservice.web.mappers")
class BeerControllerTest {

  private static final String URL = "/api/v1/beer/";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private BeerService beerService;

  @Test
  void getBeerById() throws Exception {
    given(beerService.getById(any())).willReturn(BeerDto.builder().build());
    mockMvc.perform(get(URL + "{beerId}", UUID.randomUUID()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("v1/beer-get",
            pathParameters(
                parameterWithName("beerId").description("UUID of described beer to get.")
            ),
            responseFields(
                fieldWithPath("id").description("Id of Beer"),
                fieldWithPath("version").description("Version number"),
                fieldWithPath("createdDate").description("Date Created"),
                fieldWithPath("lastModifiedDate").description("Date Updated"),
                fieldWithPath("beerName").description("Beer Name"),
                fieldWithPath("beerStyle").description("Beer Style"),
                fieldWithPath("upc").description("UPC of Beer"),
                fieldWithPath("price").description("Price"),
                fieldWithPath("quantityOnHand").description("Quantity On hand")
            )));
  }

  @Test
  void saveNewBeer() throws Exception {
    BeerDto beerDto = getValidBeerDto();
    String beerDtoJson = objectMapper.writeValueAsString(beerDto);

    ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

    mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(beerDtoJson))
        .andExpect(status().isCreated())
        .andDo(document("v1/beer-new",
            requestFields(
                fields.withPath("id").ignored(),
                fields.withPath("version").ignored(),
                fields.withPath("createdDate").ignored(),
                fields.withPath("lastModifiedDate").ignored(),
                fields.withPath("beerName").description("Name of the beer"),
                fields.withPath("beerStyle").description("Style of beer"),
                fields.withPath("upc").description("Beer UPC"),
                fields.withPath("price").description("Beer price"),
                fields.withPath("quantityOnHand").ignored()
            )));
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

  private BeerDto getValidBeerDto() {
    return BeerDto.builder()
        .beerName("My Beer")
        .beerStyle(BeerStyle.ALE)
        .price(new BigDecimal("2.99"))
        .upc(123123123123L)
        .build();
  }

  private static class ConstrainedFields {

    private final ConstraintDescriptions constraintDescriptions;

    ConstrainedFields(Class<?> input) {
      this.constraintDescriptions = new ConstraintDescriptions(input);
    }

    private FieldDescriptor withPath(String path) {
      return fieldWithPath(path).attributes(key("constraints").value(StringUtils
          .collectionToDelimitedString(this.constraintDescriptions
              .descriptionsForProperty(path), ". ")));
    }
  }
}