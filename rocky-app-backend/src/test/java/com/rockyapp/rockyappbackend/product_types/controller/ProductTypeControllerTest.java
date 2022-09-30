package com.rockyapp.rockyappbackend.product_types.controller;

import com.rockyapp.rockyappbackend.common.AbstractControllerTest;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.product_types.builder.ProductTypeBuilder;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import com.rockyapp.rockyappbackend.product_types.mapper.ProductTypeMapper;
import com.rockyapp.rockyappbackend.product_types.service.ProductTypeService;
import com.rockyapp.rockyappbackend.utils.CustomUtils;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.Locale;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductTypeControllerTest  extends AbstractControllerTest {
    private static final String ENDPOINT_URL = "/api/v1/product-type";
    @InjectMocks
    private ProductTypeController productTypeController;

    @Mock
    private ProductTypeService productTypeService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(productTypeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                })
                .build();
    }

    @Test
    public void searchProductTypesByPageAndCriteria_shouldReturn_resultPagineOfProductTypeDTO() throws Exception {
        Page<ProductType> page = new PageImpl<>(Collections.singletonList(ProductTypeBuilder.getEntity()));

        ProductTypeMapper productTypeMapper = new ProductTypeMapper();

        ResultPagine<ProductTypeDTO> resultPagine = productTypeMapper.mapFromEntity(page);

        Mockito.when(productTypeService.search(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(resultPagine);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].description").value("test"));

        Mockito.verify(productTypeService, Mockito.times(1)).search(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(productTypeService);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findProductTypeById_shouldReturn_productTypeDTO() throws Exception {
        Mockito.when(productTypeService.findById(ArgumentMatchers.anyLong())).thenReturn(ProductTypeBuilder.getDto());

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)));
        Mockito.verify(productTypeService, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(productTypeService);
    }

    @Test
    public void createProductType_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(productTypeService).create(ArgumentMatchers.any(ProductTypeDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(ProductTypeBuilder.getDto())))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(productTypeService, Mockito.times(1)).create(ArgumentMatchers.any(ProductTypeDTO.class));
        Mockito.verifyNoMoreInteractions(productTypeService);
    }

    @Test
    public void updateProductType_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(productTypeService).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any());

        mockMvc.perform(
                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(ProductTypeBuilder.getDto())))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(productTypeService, Mockito.times(1)).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(ProductTypeDTO.class));
        Mockito.verifyNoMoreInteractions(productTypeService);
    }

    @Test
    public void deleteProductType_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(productTypeService).delete(ArgumentMatchers.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders.delete(ENDPOINT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(productTypeService, Mockito.times(1)).delete(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(productTypeService);
    }
}