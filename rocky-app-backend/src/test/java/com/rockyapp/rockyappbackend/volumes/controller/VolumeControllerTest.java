package com.rockyapp.rockyappbackend.volumes.controller;

import com.rockyapp.rockyappbackend.common.AbstractControllerTest;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.utils.CustomUtils;
import com.rockyapp.rockyappbackend.utils.enums.MesureEnum;
import com.rockyapp.rockyappbackend.volumes.builder.VolumeBuilder;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
import com.rockyapp.rockyappbackend.volumes.entity.Volume;
import com.rockyapp.rockyappbackend.volumes.mapper.VolumeMapper;
import com.rockyapp.rockyappbackend.volumes.service.VolumeService;
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

public class VolumeControllerTest extends AbstractControllerTest {

    private static final String ENDPOINT_URL = "/api/v1/volume";

    @InjectMocks
    private VolumeController volumeController;

    @Mock
    private VolumeService volumeService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(volumeController)
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
    public void searchVolumesByPageAndCriteria_shouldReturn_resultPagineOfVolumeDTO() throws Exception {
        Page<Volume> page = new PageImpl<>(Collections.singletonList(VolumeBuilder.getEntity()));

        VolumeMapper mapper = new VolumeMapper();

        ResultPagine<VolumeDTO> resultPagine = mapper.mapFromEntity(page);

        Mockito.when(volumeService.search(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(resultPagine);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].mesure").value(MesureEnum.ML.name()));

        Mockito.verify(volumeService, Mockito.times(1)).search(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(volumeService);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findVolumeById_shouldReturn_volumeDTO() throws Exception {
        Mockito.when(volumeService.findById(ArgumentMatchers.anyLong())).thenReturn(VolumeBuilder.getDto());

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)));

        Mockito.verify(volumeService, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(volumeService);
    }

    @Test
    public void createVolume_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(volumeService).create(ArgumentMatchers.any(VolumeDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(VolumeBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(volumeService, Mockito.times(1)).create(ArgumentMatchers.any(VolumeDTO.class));
        Mockito.verifyNoMoreInteractions(volumeService);
    }

    @Test
    public void updateVolume_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(volumeService).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any());

        mockMvc.perform(
                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(VolumeBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(volumeService, Mockito.times(1)).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(VolumeDTO.class));
        Mockito.verifyNoMoreInteractions(volumeService);
    }

    @Test
    public void deleteVolume_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(volumeService).delete(ArgumentMatchers.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(ENDPOINT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Mockito.verify(volumeService, Mockito.times(1)).delete(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(volumeService);
    }
}