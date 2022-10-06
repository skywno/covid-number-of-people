package com.example.covid.service;

import com.example.covid.constant.ErrorCode;
import com.example.covid.constant.LocationType;
import com.example.covid.domain.Location;
import com.example.covid.dto.LocationDto;
import com.example.covid.exception.GeneralException;
import com.example.covid.repository.LocationRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 장소")
@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @InjectMocks
    private LocationService sut;
    @Mock
    private LocationRepository locationRepository;

    @DisplayName("장소를 검색하면, 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingLocations_thenReturnsEntireLocationList() {
        // Given
        given(locationRepository.findAll(any(Predicate.class)))
                .willReturn(List.of(
                        createLocation(LocationType.COMMON, "레스토랑"),
                        createLocation(LocationType.SPORTS, "체육관")
                ));

        // When
        List<LocationDto> list = sut.getLocations(new BooleanBuilder());

        // Then
        assertThat(list).hasSize(2);
        then(locationRepository).should().findAll(any(Predicate.class));
    }

    @DisplayName("장소를 검색하는데 에러가 발생한 경우, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenSearchingLocations_thenThrowsGeneralException() {
        // Given
        RuntimeException e = new RuntimeException("This is test.");
        given(locationRepository.findAll(any(Predicate.class))).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.getLocations(new BooleanBuilder()));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(locationRepository).should().findAll(any(Predicate.class));
    }

    @DisplayName("장소 ID로 존재하는 장소를 조회하면, 해당 장소 정보를 출력하여 보여준다.")
    @Test
    void givenLocationId_whenSearchingExistingLocation_thenReturnsLocation() {
        // Given
        long locationId = 1L;
        Location location = createLocation(LocationType.SPORTS, "체육관");
        given(locationRepository.findById(locationId)).willReturn(Optional.of(location));

        // When
        Optional<LocationDto> result = sut.getLocation(locationId);

        // Then
        assertThat(result).hasValue(LocationDto.of(location));
        then(locationRepository).should().findById(locationId);
    }

    @DisplayName("장소 ID로 장소를 조회하면, 빈 정보를 출력하여 보여준다.")
    @Test
    void givenLocationId_whenSearchingNonexistentLocation_thenReturnsEmptyOptional() {
        // Given
        long locationId = 2L;
        given(locationRepository.findById(locationId)).willReturn(Optional.empty());

        // When
        Optional<LocationDto> result = sut.getLocation(locationId);

        // Then
        assertThat(result).isEmpty();
        then(locationRepository).should().findById(locationId);
    }

    @DisplayName("장소 ID로 장소를 조회하는데 데이터 관련 에러가 발생한 경우, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenSearchingLocation_thenThrowsGeneralException() {
        // Given
        RuntimeException e = new RuntimeException("This is test.");
        given(locationRepository.findById(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.getLocation(null));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(locationRepository).should().findById(any());
    }

    @DisplayName("장소 정보를 주면, 장소를 생성하고 결과를 true 로 보여준다.")
    @Test
    void givenLocation_whenCreating_thenCreatesLocationAndReturnsTrue() {
        // Given
        LocationDto locationDto = createLocationDto(LocationType.SPORTS, "체육관");
        Location location = createLocation(LocationType.SPORTS, "체육관");
        given(locationRepository.save(any(Location.class))).willReturn(location);

        // When
        boolean result = sut.createLocation(locationDto);

        // Then
        assertThat(result).isTrue();
        then(locationRepository).should().save(any(Location.class));
    }


    @DisplayName("장소 정보를 주지 않으면, 생성 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenCreating_thenAbortCreatingAndReturnsFalse() {
        // Given

        // When
        boolean result = sut.createLocation(null);

        // Then
        assertThat(result).isFalse();
        then(locationRepository).shouldHaveNoInteractions();
    }

    @DisplayName("장소 생성 중 데이터 예외가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다")
    @Test
    void givenDataRelatedException_whenCreating_thenThrowsGeneralException() {
        // Given
        Location location = createLocation(LocationType.SPORTS, "체육관");
        RuntimeException e = new RuntimeException("This is test.");
        given(locationRepository.save(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.createLocation(LocationDto.of(location)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(locationRepository).should().save(any());
    }

    @DisplayName("장소 ID와 정보를 주면, 장소 정보를 변경하고 결과를 true 로 보여준다.")
    @Test
    void givenLocationIdAndItsInfo_whenModifying_thenModifiesLocationAndReturnsTrue() {
        // Given
        long locationId = 1L;
        Location originalLocation = createLocation(LocationType.SPORTS, "체육관");
        Location changedLocation = createLocation(LocationType.PARTY, "무도회장");
        given(locationRepository.findById(locationId)).willReturn(Optional.of(originalLocation));
        given(locationRepository.save(changedLocation)).willReturn(changedLocation);

        // When
        boolean result = sut.modifyLocation(locationId, LocationDto.of(changedLocation));

        // Then
        assertThat(result).isTrue();
        then(locationRepository).should().findById(locationId);
        then(locationRepository).should().save(changedLocation);
    }

    @DisplayName("장소 ID를 주지 않으면, 장소 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNoLocationId_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        Location location = createLocation(LocationType.SPORTS, "체육관");

        // When
        boolean result = sut.modifyLocation(null, LocationDto.of(location));

        // Then
        assertThat(result).isFalse();
        then(locationRepository).shouldHaveNoInteractions();
    }

    @DisplayName("장소 ID만 주고 변경할 정보를 주지 않으면, 장소 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenLocationIdOnly_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        long locationId = 1L;

        // When
        boolean result = sut.modifyLocation(locationId, null);

        // Then
        assertThat(result).isFalse();
        then(locationRepository).shouldHaveNoInteractions();
    }

    @DisplayName("장소 변경 중 데이터 오류가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenModifying_thenThrowsGeneralException() {
        // Given
        long locationId = 1L;
        Location originalLocation = createLocation(LocationType.SPORTS, "체육관");
        Location wrongLocation = createLocation(null, null);
        RuntimeException e = new RuntimeException("This is test.");
        given(locationRepository.findById(locationId)).willReturn(Optional.of(originalLocation));
        given(locationRepository.save(any())).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.modifyLocation(locationId, LocationDto.of(wrongLocation)));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(locationRepository).should().findById(locationId);
        then(locationRepository).should().save(any());
    }

    @DisplayName("장소 ID를 주면, 장소 정보를 삭제하고 결과를 true 로 보여준다.")
    @Test
    void givenLocationId_whenDeleting_thenDeletesLocationAndReturnsTrue() {
        // Given
        long locationId = 1L;
        Location originalLocation = createLocation(LocationType.SPORTS, "체육관");
        given(locationRepository.findById(locationId)).willReturn(Optional.of(originalLocation));
        willDoNothing().given(locationRepository).deleteById(locationId);

        // When
        boolean result = sut.removeLocation(locationId);

        // Then
        assertThat(result).isTrue();
        then(locationRepository).should().deleteById(locationId);
    }

    @DisplayName("장소 ID를 주지 않으면, 삭제 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenDeleting_thenAbortsDeletingAndReturnsFalse() {
        // Given

        // When
        boolean result = sut.removeLocation(null);

        // Then
        assertThat(result).isFalse();
        then(locationRepository).shouldHaveNoInteractions();
    }

    @DisplayName("장소 삭제 중 데이터 오류가 발생하면, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenDeleting_thenThrowsGeneralException() {
        // Given
        long locationId = 1L;
        Location originalLocation = createLocation(LocationType.SPORTS, "체육관");
        given(locationRepository.findById(locationId)).willReturn(Optional.of(originalLocation));        RuntimeException e = new RuntimeException("This is test.");
        willThrow(e).given(locationRepository).deleteById(locationId);

        // When
        Throwable thrown = catchThrowable(() -> sut.removeLocation(locationId));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());
        then(locationRepository).should().deleteById(locationId);
    }

    private LocationDto createLocationDto(LocationType locationType, String locationName) {
        return LocationDto.of(
                null,
                locationType,
                locationName,
                "주소 테스트",
                "010-1234-5678",
                24,
                null,
                null
                );
    }

    private Location createLocation(
            LocationType locationType,
            String locationName
    ) {
        Location location = Location.of(
                locationType,
                locationName,
                "주소 테스트",
                "010-1234-5678",
                24
        );
        ReflectionTestUtils.setField(location, "id", 1L);
        return location;
    }
}