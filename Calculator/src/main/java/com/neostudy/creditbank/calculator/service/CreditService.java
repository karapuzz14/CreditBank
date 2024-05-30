package com.neostudy.creditbank.calculator.service;

import com.neostudy.creditbank.calculator.dto.*;
import com.neostudy.creditbank.calculator.enums.EmploymentStatus;
import com.neostudy.creditbank.calculator.enums.Gender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditService
{
    @Value("${rate}")
    private BigDecimal rate;

    public CreditDto calculateCredit(ScoringDataDto scoringDataDto)
    {
        if (isDenied(scoringDataDto))
            return null;

        int term = scoringDataDto.getTerm();
        BigDecimal requestedAmount = scoringDataDto.getAmount();
        BigDecimal rate = calculateScoredRate(scoringDataDto);
        OfferService offerService = new OfferService();
        BigDecimal monthlyPayment = offerService.calculateMonthlyPayment(rate, requestedAmount, term);
        BigDecimal psk = monthlyPayment.multiply(BigDecimal.valueOf(term)).setScale(2, RoundingMode.HALF_EVEN);
        List<PaymentScheduleElementDto> schedule = createSchedule(requestedAmount, monthlyPayment, rate);

        return CreditDto.builder()
                .paymentSchedule(schedule)
                .monthlyPayment(monthlyPayment)
                .psk(psk)
                .rate(rate)
                .term(scoringDataDto.getTerm())
                .amount(scoringDataDto.getAmount())
                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                .build();
    }

    private List<PaymentScheduleElementDto> createSchedule(BigDecimal requestedAmount, BigDecimal monthlyPayment, BigDecimal rate)
    {
        ZoneId z = ZoneId.of("Europe/Moscow");
        LocalDate date = LocalDate.now(z);
        BigDecimal remainingDebt = requestedAmount;
        BigDecimal interestPayment, debtPayment;
        monthlyPayment = monthlyPayment.setScale(2, RoundingMode.HALF_EVEN);
        int num = 1;

        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        while(remainingDebt.compareTo(monthlyPayment) >= 0)
        {
            date = date.plusMonths(1);
            interestPayment = remainingDebt.multiply(rate)
                    .multiply(BigDecimal.valueOf(date.lengthOfMonth()))
                    .divide(BigDecimal.valueOf(date.lengthOfYear()), 2, RoundingMode.HALF_EVEN);
            debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_EVEN);
            debtPayment = debtPayment.setScale(2, RoundingMode.HALF_EVEN);
            schedule.add(
                            PaymentScheduleElementDto.builder()
                            .number(num)
                            .date(date)
                            .totalPayment(monthlyPayment)
                            .interestPayment(interestPayment)
                            .debtPayment(debtPayment)
                            .remainingDebt(remainingDebt)
                            .build()
            );
            ++num;
        }

        if(remainingDebt.compareTo(BigDecimal.valueOf(0)) != 0)
        {
            date = date.plusMonths(1);
            interestPayment = remainingDebt.multiply(rate)
                    .multiply(BigDecimal.valueOf(date.lengthOfMonth()))
                    .divide(BigDecimal.valueOf(date.lengthOfYear()), RoundingMode.HALF_EVEN);
            debtPayment = remainingDebt;
            BigDecimal totalPayment = debtPayment.add(interestPayment).setScale(2, RoundingMode.HALF_EVEN);
            remainingDebt = BigDecimal.valueOf(0);
            interestPayment = interestPayment.setScale(2, RoundingMode.HALF_EVEN);
            schedule.add(
                    PaymentScheduleElementDto.builder()
                            .number(num)
                            .date(date)
                            .totalPayment(totalPayment)
                            .interestPayment(interestPayment)
                            .debtPayment(debtPayment)
                            .remainingDebt(remainingDebt)
                            .build()
            );
        }

        return schedule;
    }
    private BigDecimal calculateScoredRate(ScoringDataDto scoringDataDto)
    {
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        BigDecimal creditRate = rate;
        EmploymentDto employmentDto = scoringDataDto.getEmploymentDto();

        switch (employmentDto.getEmploymentStatus()) {
            case EMPLOYED -> creditRate = creditRate.add(BigDecimal.valueOf(0.01));
            case SELF_EMPLOYED -> creditRate = creditRate.add(BigDecimal.valueOf(0.02));
            case EMPLOYER -> creditRate = creditRate.add(BigDecimal.valueOf(0.03));
        }
        switch (employmentDto.getPosition()) {
            case ORDINARY -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.01));
            case LOWER_MANAGER -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.02));
            case MIDDLE_MANAGER -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.03));
            case TOP_MANAGER -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.04));
        }
        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED -> creditRate = creditRate.subtract(BigDecimal.valueOf(0.03));
            case DIVORCED -> creditRate = creditRate.add(BigDecimal.valueOf(0.01));
        }

        if(scoringDataDto.getGender() == Gender.FEMALE
                && age >= 32
                && age <= 60)
            creditRate = creditRate.subtract(BigDecimal.valueOf(0.03));

        if(scoringDataDto.getGender() == Gender.MALE
                && age >= 30
                && age <= 55)
            creditRate = creditRate.subtract(BigDecimal.valueOf(0.03));

        return creditRate;
    }
    private boolean isDenied(ScoringDataDto scoringDataDto)
    {
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        EmploymentDto employmentDto = scoringDataDto.getEmploymentDto();

        return employmentDto.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED
                || employmentDto.getSalary().multiply(BigDecimal.valueOf(25)).compareTo(scoringDataDto.getAmount()) < 0
                || age < 20
                || age > 65
                || employmentDto.getWorkExperienceCurrent() < 3
                || employmentDto.getWorkExperienceTotal() < 18;
    }
}
