package com.ecommerce.service;

import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserCleanupServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCleanupService userCleanupService;

    @BeforeEach
    public void setup() {
        // Reset mocks before each test
        reset(userRepository);
    }

    @Test
    public void shouldDeleteUnverifiedUsersOlderThanThreshold() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        
        // Mock unverified users that should be deleted
        when(userRepository.findUnverifiedUserIdsCreatedBefore(any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(1L, 2L, 3L));

        // Act
        userCleanupService.deleteUnverifiedUsers();

        // Assert
        verify(userRepository, times(1)).findUnverifiedUserIdsCreatedBefore(any(LocalDateTime.class));
        verify(userRepository, times(1)).deleteAllById(Arrays.asList(1L, 2L, 3L));
    }

    @Test
    public void shouldNotDeleteAnythingWhenNoUnverifiedUsersFound() {
        // Arrange
        when(userRepository.findUnverifiedUserIdsCreatedBefore(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // Act
        userCleanupService.deleteUnverifiedUsers();

        // Assert
        verify(userRepository, times(1)).findUnverifiedUserIdsCreatedBefore(any(LocalDateTime.class));
        verify(userRepository, never()).deleteAllById(any());
    }
} 