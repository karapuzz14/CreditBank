package com.neostudy.creditbank.calculator.controller;

import com.neostudy.creditbank.calculator.dto.CreditDto;
import com.neostudy.creditbank.calculator.dto.LoanOfferDto;
import com.neostudy.creditbank.calculator.dto.LoanStatementRequestDto;
import com.neostudy.creditbank.calculator.dto.ScoringDataDto;
import com.neostudy.creditbank.calculator.exception.LaterBirthdateException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculator")
public class CalculatorController
{
    @PostMapping("/offers")
    public List<LoanOfferDto> calculateLoanOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) throws LaterBirthdateException
    {
        isDateLate(loanStatementRequestDto.getBirthdate());

        return new ArrayList<LoanOfferDto>();
    }

    @PostMapping("/calc")
    public CreditDto calculateCreditOffer(@RequestBody ScoringDataDto scoringDataDto)
    {

        return new CreditDto();
    }

    private void isDateLate(LocalDate date) throws LaterBirthdateException
    {
        LocalDate checkpoint = LocalDate.now().minusYears(18);
        if(date.isAfter(checkpoint))
            throw new LaterBirthdateException();
    }
}
