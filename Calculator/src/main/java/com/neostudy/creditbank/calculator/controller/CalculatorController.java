package com.neostudy.creditbank.calculator.controller;

import com.neostudy.creditbank.calculator.dto.CreditDto;
import com.neostudy.creditbank.calculator.dto.LoanOfferDto;
import com.neostudy.creditbank.calculator.dto.LoanStatementRequestDto;
import com.neostudy.creditbank.calculator.dto.ScoringDataDto;
import com.neostudy.creditbank.calculator.exception.LaterBirthdateException;
import com.neostudy.creditbank.calculator.interfaces.ICalculate;
import com.neostudy.creditbank.calculator.service.CreditService;
import com.neostudy.creditbank.calculator.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculator")
public class CalculatorController implements ICalculate
{
    private final CreditService creditService;
    private final OfferService offerService;

    @PostMapping("/offers")
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) throws LaterBirthdateException
    {
        offerService.isDateLate(loanStatementRequestDto.getBirthdate());
        return offerService.getOfferList(loanStatementRequestDto);
    }

    @PostMapping("/calc")
    public CreditDto calculateCreditOffer(ScoringDataDto scoringDataDto)
    {
        return creditService.calculateCredit(scoringDataDto);
    }

}
