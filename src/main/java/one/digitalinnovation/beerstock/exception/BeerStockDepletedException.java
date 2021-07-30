package one.digitalinnovation.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockDepletedException extends RuntimeException {

	public BeerStockDepletedException(Long id, int quantityToDecrement) {
		super(String.format("There's no available stock for beer ID %d to decrement %d units.", id,
							quantityToDecrement));
	}
}
