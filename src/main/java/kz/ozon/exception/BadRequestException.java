package kz.ozon.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BadRequestException extends RuntimeException {
}
