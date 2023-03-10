package com.posomo.saltit.domain.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Schema(description = "예외 정보")
public class ErrorResponse {
    @Schema(description = "오류 메세지", example = "Invalid Argument(user Latitude,user Longitude cannot be null)")
    private String message;
    @Schema(description = "에러 코드", example = "400")
    private Integer ErrorCode;
}
