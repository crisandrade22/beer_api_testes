package one.digitalinnovation.beerstock.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.entity.BeerEntity;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerStockDepletedException;
import one.digitalinnovation.beerstock.exception.BeerStockExceededException;
import one.digitalinnovation.beerstock.mapper.BeerMapper;
import one.digitalinnovation.beerstock.repository.BeerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        BeerEntity beerEntity = beerMapper.toModel(beerDTO);
        BeerEntity savedBeerEntity = beerRepository.save(beerEntity);
        return beerMapper.toDTO(savedBeerEntity);
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        BeerEntity foundBeerEntity = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));
        return beerMapper.toDTO(foundBeerEntity);
    }

    public List<BeerDTO> listAll() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<BeerEntity> optSavedBeer = beerRepository.findByName(name);
        if (optSavedBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    private BeerEntity verifyIfExists(Long id) throws BeerNotFoundException {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }

    public BeerDTO increment(Long id, int quantityToIncrement) throws BeerNotFoundException, BeerStockExceededException {
        BeerEntity beerEntityToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + beerEntityToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= beerEntityToIncrementStock.getMax()) {
            beerEntityToIncrementStock.setQuantity(beerEntityToIncrementStock.getQuantity() + quantityToIncrement);
            BeerEntity incrementedBeerEntityStock = beerRepository.save(beerEntityToIncrementStock);
            return beerMapper.toDTO(incrementedBeerEntityStock);
        }
        throw new BeerStockExceededException(id, quantityToIncrement);
    }

    public BeerDTO decrement(Long id, int quantityToDecrement) throws BeerNotFoundException {
        BeerEntity beerEntityToDecrementStock = verifyIfExists(id);
        int quantityAfterDecrement =  beerEntityToDecrementStock.getQuantity() - quantityToDecrement;
        if (quantityAfterDecrement >= 0) {
            beerEntityToDecrementStock.setQuantity(quantityAfterDecrement);
            BeerEntity decrementedBeerEntityStock = beerRepository.save(beerEntityToDecrementStock);
            return beerMapper.toDTO(decrementedBeerEntityStock);
        }
        throw new BeerStockDepletedException(id, quantityToDecrement);
    }
}
