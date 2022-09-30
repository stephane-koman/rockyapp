package com.rockyapp.rockyappbackend.users.controller;

import com.rockyapp.rockyappbackend.common.AbstractControllerTest;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.users.builder.UserBuilder;
import com.rockyapp.rockyappbackend.users.dto.SimpleUserDTO;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.dto.UserUpdateDTO;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.mapper.SimpleUserMapper;
import com.rockyapp.rockyappbackend.users.service.UserService;
import com.rockyapp.rockyappbackend.utils.CustomUtils;
import com.rockyapp.rockyappbackend.utils.mappers.UserGlobalMapper;
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

public class UserControllerTest extends AbstractControllerTest {
    
    private static final String ENDPOINT_URL = "/api/v1/user";

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
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
    public void searchUsersByPageAndCriteria_shouldReturn_resultPagineOfUserDTO() throws Exception {
        Page<User> page = new PageImpl<>(Collections.singletonList(UserBuilder.getEntity()));
        UserGlobalMapper userGlobalMapper = new UserGlobalMapper();

        SimpleUserMapper mapper = new SimpleUserMapper(userGlobalMapper);

        ResultPagine<SimpleUserDTO> resultPagine = mapper.mapFromEntity(page);

        Mockito.when(userService.search(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(resultPagine);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].name").value("ADMIN"));

        Mockito.verify(userService, Mockito.times(1)).search(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(userService);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findUserById_shouldReturn_userDTO() throws Exception {
        Mockito.when(userService.findById(ArgumentMatchers.anyLong())).thenReturn(UserBuilder.getDto());

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)));
        Mockito.verify(userService, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void createUser_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(userService).create(ArgumentMatchers.any(UserCreaDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(UserBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).create(ArgumentMatchers.any(UserCreaDTO.class));
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void updateUser_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(userService).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(UserUpdateDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(UserBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(UserUpdateDTO.class));
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUser_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(userService).delete(ArgumentMatchers.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(ENDPOINT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).delete(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userService);
    }
}