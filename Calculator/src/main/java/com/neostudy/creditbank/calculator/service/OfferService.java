package com.neostudy.creditbank.calculator.service;

import com.neostudy.creditbank.calculator.dto.*;
import com.neostudy.creditbank.calculator.exception.LaterBirthdateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OfferService
{
    @Value("${rate}")
    private BigDecimal rate;

    public void isDateLate(LocalDate date) throws LaterBirthdateException
    {
        LocalDate checkpoint = LocalDate.now().minusYears(18);
        if(date.isAfter(checkpoint))
            throw new LaterBirthdateException();
    }

    public List<LoanOfferDto> getOfferList(LoanStatementRequestDto request)
    {
        List<LoanOfferDto> offerList = new ArrayList<>(List.of(
                calculateOffer(request, false, false),
                calculateOffer(request, true, false),
                calculateOffer(request, false, true),
                calculateOffer(request, true, true)));
        offerList.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());

        return offerList;

    }

    public BigDecimal calculateMonthlyPayment(BigDecimal offerRate, BigDecimal offerRequestedAmount, int term)
    {
        BigDecimal monthRate = offerRate
                .divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_EVEN);
        BigDecimal addition = (monthRate.add(BigDecimal.valueOf(1))).pow(term);

        BigDecimal coefficient = monthRate
                .multiply(addition)
                .divide(addition.subtract(BigDecimal.valueOf(1)), 8, RoundingMode.HALF_EVEN);

        return offerRequestedAmount.multiply(coefficient).setScale(2, RoundingMode.HALF_EVEN);
    }

    private LoanOfferDto calculateOffer(LoanStatementRequestDto loanStatementRequestDto, boolean isInsuranceEnabled, boolean isSalaryClient)
    {
        BigDecimal offerRate = rate;
        BigDecimal offerRequestedAmount = loanStatementRequestDto.getAmount();
        int term = loanStatementRequestDto.getTerm();

        if(isInsuranceEnabled)
        {
            offerRate = offerRate.subtract(BigDecimal.valueOf(0.03));
            offerRequestedAmount = offerRequestedAmount.multiply(BigDecimal.valueOf(1.05));
        }
        if(isSalaryClient)
            offerRate = offerRate.subtract(BigDecimal.valueOf(0.01));

        BigDecimal monthlyPayment = calculateMonthlyPayment(offerRate, offerRequestedAmount, term);

        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).setScale(2, RoundingMode.HALF_EVEN);
        monthlyPayment = monthlyPayment.setScale(2, RoundingMode.HALF_EVEN);

        return LoanOfferDto.builder()
                .rate(offerRate)
                .requestedAmount(offerRequestedAmount)
                .monthlyPayment(monthlyPayment)
                .totalAmount(totalAmount)
                .term(term)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }

}
