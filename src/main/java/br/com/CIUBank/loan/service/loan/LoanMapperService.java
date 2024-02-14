package br.com.CIUBank.loan.service.loan;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.loan.LoanDTO;
import br.com.CIUBank.loan.entity.loan.Loan;
import br.com.CIUBank.loan.mapper.DozerMapper;

@Service
public class LoanMapperService {

    public LoanDTO toLoanDTO(Loan loan) {
        return DozerMapper.parseObject(loan, LoanDTO.class);
    }

    public Loan toLoanEntity(LoanDTO loanDTO) {
        return DozerMapper.parseObject(loanDTO, Loan.class);
    }

    public List<LoanDTO> toLoanDTOList(List<Loan> loans) {
        return DozerMapper.parseListObjects(loans, LoanDTO.class);
    }
}
