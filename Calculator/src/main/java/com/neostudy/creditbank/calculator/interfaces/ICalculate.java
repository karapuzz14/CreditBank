package com.neostudy.creditbank.calculator.interfaces;

import com.neostudy.creditbank.calculator.dto.CreditDto;
import com.neostudy.creditbank.calculator.dto.LoanOfferDto;
import com.neostudy.creditbank.calculator.dto.LoanStatementRequestDto;
import com.neostudy.creditbank.calculator.dto.ScoringDataDto;
import com.neostudy.creditbank.calculator.exception.LaterBirthdateException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

public interface ICalculate
{
    List<LoanOfferDto> calculateLoanOffers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) throws LaterBirthdateException;
    CreditDto calculateCreditOffer(@Valid @RequestBody ScoringDataDto scoringDataDto);
}
